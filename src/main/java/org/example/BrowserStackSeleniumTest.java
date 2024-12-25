package org.example;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowserStackSeleniumTest {
    public static void main(String[] args) throws IOException {
        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");

        // Initialize WebDriver with options
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Run in headless mode for automation
        WebDriver driver = new ChromeDriver(options);

        // List to store translated titles for word analysis
        Map<String, Integer> wordCountMap = new HashMap<>();

        try {
            // Navigate to El País website
            driver.get("https://elpais.com/");
            driver.manage().window().maximize();

            // Wait for the page to load completely
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Try to find the "Accept Cookies" button and click if it is present and not accepted yet
            try {
                // Check if the button is present and not already clicked (accepted)
                WebElement acceptCookiesButton = driver.findElement(By.cssSelector("button[id='didomi-notice-agree-button'] span"));

                // If the button is found and not accepted, click it
                if (acceptCookiesButton.isDisplayed() && acceptCookiesButton.isEnabled()) {
                    acceptCookiesButton.click();
                    System.out.println("Cookies accepted.");
                } else {
                    System.out.println("Cookies already accepted or button not present.");
                }
            } catch (Exception e) {
                // If the button is not found, print a message
                System.out.println("Accept Cookies button not found.");
            }

            // Verify the language attribute of the <html> tag
            WebElement htmlTag = driver.findElement(By.tagName("html"));
            String lang = htmlTag.getAttribute("lang");
            if ("es-ES".equals(lang)) {
                System.out.println("The website is displayed in Spanish (lang attribute is 'es-ES').");
            } else {
                System.out.println("The website is not displayed in Spanish (lang attribute is '" + lang + "').");
            }

            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Opinión")));

            // Navigate to the Opinion section
            WebElement opinionSection = driver.findElement(By.linkText("Opinión"));
            opinionSection.click();

            // Wait for the Opinion section to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//main//section//div/article")));

            // Select the first 5 articles
            List<WebElement> articles = driver.findElements(By.xpath("//main//section//div/article[position() <= 5]"));

            // Directory to save images
            String saveDirectory = "src/main/resources/images";

            // Print out the title and paragraph text for the first 5 articles
            System.out.println("First 5 Articles' Details:");
            int count = 0;
            for (WebElement article : articles) {
                if (count >= 5) break; // Stop after processing 5 articles

                // Extract title (Assuming title is under header/h2/a)
                String title = "No Title Available";
                try {
                    WebElement titleElement = article.findElement(By.xpath(".//header/h2/a"));
                    title = titleElement.getText();
                } catch (Exception e) {
                    System.out.println("Title not found for Article " + (count + 1));
                }

                // Translate titles to English using Rapid API
                System.out.println("Translated Titles:");

                String translatedTitle = translateWithRapidAPI(title, "es", "en");
                System.out.println("Original: " + title);
                System.out.println("Translated: " + translatedTitle);
                System.out.println();

                // Add the translated title words to wordCountMap
                processWords(translatedTitle, wordCountMap);

                // Try to find the cover image for the article
                try {
                    WebElement imageElement = article.findElement(By.tagName("img"));
                    String imageUrl = imageElement.getAttribute("src");

                    // Download and save the image
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        String imageName = "article_" + (count + 1) + "_cover.jpg";  // Naming image as article_4_cover.jpg (or article_n_cover.jpg)
                        System.out.println("Downloading image: " + imageUrl);
                        downloadImage(imageUrl, saveDirectory, imageName);
                    }
                } catch (Exception e) {
                    System.out.println("No image found for article " + (count + 1));
                }

                count++;
            }

            // After processing all articles, print repeated words
            printRepeatedWords(wordCountMap);

        } finally {
            // Close the browser
            driver.quit();
        }
    }

    // Function to process words and count their occurrences
    public static void processWords(String text, Map<String, Integer> wordCountMap) {
        // Split the text into words
        String[] words = text.toLowerCase().split("[\\W_]+");

        // Count each word's occurrence
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
    }

    // Function to print words that appear more than twice
    public static void printRepeatedWords(Map<String, Integer> wordCountMap) {
        System.out.println("Words repeated more than twice:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public static String translateWithRapidAPI(String textToTranslate, String fromLanguage, String toLanguage) throws IOException {
        // URL of the translation API
        String url = "https://rapid-translate-multi-traduction.p.rapidapi.com/t";

        // Create HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Prepare the POST request
        HttpPost request = new HttpPost(url);

        // Set the necessary headers
        request.addHeader("Content-Type", "application/json");
        request.addHeader("x-rapidapi-host", "rapid-translate-multi-traduction.p.rapidapi.com");
        request.addHeader("x-rapidapi-key", "e8927a3851msh7c1aeaf2f4d6b5ep15d82ejsn541a17908e2a");

        // Prepare the JSON payload
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("from", fromLanguage);
        jsonPayload.put("to", toLanguage);
        jsonPayload.put("q", textToTranslate);

        // Set the entity (body) of the request with the JSON payload
        StringEntity entity = new StringEntity(jsonPayload.toString());
        request.setEntity(entity);

        // Execute the request
        HttpResponse response = httpClient.execute(request);

        // Get the response body as a string
        String responseString = EntityUtils.toString(response.getEntity());

        // Print the raw response for inspection
        System.out.println("Raw Response: " + responseString);

        // Parse the response to extract the translated text
        String translatedText = "";
        try {
            // Since the response might be a JSON array, handle it as such
            JSONArray jsonResponseArray = new JSONArray(responseString);

            // Check if the response array has any elements
            if (jsonResponseArray.length() > 0) {
                translatedText = jsonResponseArray.getString(0); // Get the first element of the array
            } else {
                System.out.println("The translation response array is empty.");
            }
        } catch (Exception e) {
            System.out.println("Error while parsing response: " + e.getMessage());
            System.out.println("Response is not valid JSON: " + responseString);
        }

        // Close the HTTP client
        httpClient.close();

        return translatedText;
    }
    /**
     * Downloads an image from the given URL and saves it to the specified directory.
     */
    public static void downloadImage(String imageUrl, String saveDirectory, String imageName) {
        try {
            URL url = new URL(imageUrl);
            try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(Paths.get(saveDirectory, imageName).toString())) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println("Image saved successfully: " + imageName);
            }
        } catch (IOException e) {
            System.out.println("Error downloading image: " + e.getMessage());
        }
    }
}
