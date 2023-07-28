import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.time.Duration;


//additional
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunit {
    private WebDriver driver;
    private List<String> urls;

    @BeforeEach
    public void setup() throws IOException {
        // Read the URL list from the url.json file
        InputStream inputStream = Files.newInputStream(Paths.get("src/test/resources/url.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        urls = objectMapper.readValue(inputStream, new TypeReference<List<String>>() {
        });

        // Set up WebDriver (assuming Chrome)
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7)); //implicit waiter
    }

    @AfterEach
    public void tearDown() {
        // Close the WebDriver after each test case
        if (driver != null) {
            driver.quit();
        }
    }

    @TestFactory
    public List<DynamicTest> dynamicTestsFromJson() {
        return urls.stream().map(url ->
                DynamicTest.dynamicTest("Not Found Page " + url, () -> testUrl(url))
        ).toList();
    }

    public void testUrl(String url) {
        driver.get(url);
        // Find the element with className=notfound
        boolean notFound = !driver.findElements(By.className("css-vqfczw")).isEmpty();

        // Assert "Not Found" message is present
        Assertions.assertTrue(notFound, "Share this page with your friends & family" + url);
    }


    //Close the driver
    @AfterEach
    public void closeDriver() {
        driver.quit();
    }
}
