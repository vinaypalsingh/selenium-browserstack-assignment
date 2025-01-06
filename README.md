# **Web Scraping, API Integration, and Text Processing Project**

This project demonstrates skills in **web scraping**, **API integration**, and **text processing** using **Selenium in Java**. The task involves scraping articles from the "Opinion" section of the Spanish news outlet **El País**, translating their titles, analyzing them for repeated words, and performing cross-browser testing.

---

## **Features**
1. **Web Scraping with Selenium**: 
   - Scrapes the first five articles from the "Opinion" section of El País.
   - Extracts article titles and content in Spanish.
   - Downloads and saves cover images (if available).

2. **API Integration**: 
   - Integrates with a translation API (e.g., Google Translate API) to translate article titles from Spanish to English.

3. **Text Processing**: 
   - Analyzes translated titles to identify words repeated more than twice across all headers.
   - Outputs each repeated word along with its count.

4. **Cross-Browser Testing**: 
   - Validates functionality locally.
   - Executes the solution across multiple browsers and devices using **BrowserStack** with five parallel threads.

---

## **Installation**

### **1. Clone the Repository**
```bash
git clone https://github.com/yourusername/el-pais-scraper-java.git
cd el-pais-scraper-java
```

### **2. Set Up the Environment**
- **Java Development Kit (JDK)**: Install JDK 11 or later.
- **Maven**: Ensure Maven is installed for dependency management.
- **Browser Drivers**: Ensure the appropriate WebDriver (e.g., ChromeDriver) is installed and added to your system's PATH.  

### **3. API Keys**:
- Obtain an API key for your chosen translation API (e.g., Google Translate API).
- Add the API key to the `application.properties` file:
  ```properties
  translation.api.key=your_api_key_here
  ```

### **4. BrowserStack Configuration**
- Create an account on [BrowserStack](https://www.browserstack.com/).
- Add your credentials to the `application.properties` file:
  ```properties
  browserstack.username=your_username
  browserstack.access_key=your_access_key
  ```

---

## **Usage**

### **1. Local Execution**
Run the application locally to scrape, translate, and process articles:
```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.example.Main"
```

### **2. Cross-Browser Testing**
Run the application on BrowserStack across multiple browsers:
```bash
mvn test -Dtest=CrossBrowserTest
```

---

## **Project Structure**
```
el-pais-scraper-java/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/example/
│   │   │   │   ├── Main.java              # Main application entry point
│   │   │   │   ├── Scraper.java           # Web scraping logic
│   │   │   │   ├── Translator.java        # Translation API integration
│   │   │   │   ├── Analyzer.java          # Text processing and analysis
│   │   │   │   └── BrowserStackHelper.java # BrowserStack setup
│   │   └── resources/
│   │       └── application.properties     # Configuration file for API keys
│   └── test/
│       ├── java/
│       │   └── com/example/CrossBrowserTest.java # Cross-browser testing logic
├── pom.xml               # Maven configuration file
├── README.md             # Project documentation
└── LICENSE               # License file
```

---

## **Step-by-Step Execution**

### **1. Web Scraping**
- **Navigate to El País**: Opens the website and ensures text is displayed in Spanish.
- **Scrape Articles**:
  - Navigates to the "Opinion" section.
  - Fetches the first five articles' titles, content, and cover images.
  - Saves cover images locally in a `/images` directory.

### **2. Translate Article Titles**
- Integrates with a translation API to translate article titles from Spanish to English.
- Prints the original and translated titles.

### **3. Analyze Translated Titles**
- Analyzes the translated titles to find repeated words across all headers.
- Outputs each repeated word with its count.

### **4. Cross-Browser Testing**
- Configures BrowserStack to test the solution on:
  - **Desktop Browsers**: Chrome, Firefox, Safari
  - **Mobile Browsers**: iPhone Safari, Android Chrome
- Runs tests in five parallel threads for efficiency.

---

## **Sample Output**

### **Local Execution**
```
Scraped Articles:
1. [Title in Spanish] El futuro de la democracia
   [Content] ...
   [Image Saved] ./images/article_1.jpg
   [Translated Title] The Future of Democracy

2. ...

Translated Titles:
1. The Future of Democracy
2. ...

Repeated Words in Translated Titles:
- "of": 4 occurrences
- "the": 3 occurrences
```

### **Cross-Browser Testing**
```
BrowserStack Test Summary:
- Passed: 5/5 browsers
- Failed: 0/5 browsers
```

---

## **Dependencies**

- **JDK**: 11+
- **Maven**: For dependency management.
- **Selenium**: For web scraping and automation.
- **HTTP Client (Apache or OkHttp)**: For API calls.
- **Jackson or Gson**: For JSON processing.
- **dotenv-java**: For managing environment variables.
- **BrowserStack Integration**: For cross-browser testing.

Install dependencies using Maven:
```xml
<dependencies>
    <!-- Selenium -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.1.2</version>
    </dependency>
    <!-- HTTP Client -->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>5.1.3</version>
    </dependency>
    <!-- Jackson for JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.13.3</version>
    </dependency>
    <!-- dotenv for environment variables -->
    <dependency>
        <groupId>io.github.cdimascio</groupId>
        <artifactId>dotenv-java</artifactId>
        <version>3.1.0</version>
    </dependency>
</dependencies>
```

---

## **Future Improvements**
- Add support for scraping additional sections.
- Implement caching for API calls to reduce translation costs.
- Enhance error handling for dynamic web elements.
- Perform sentiment analysis on article content.

---

## **License**
This project is licensed under the MIT License. See the `LICENSE` file for details.
