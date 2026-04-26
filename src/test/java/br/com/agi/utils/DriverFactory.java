package br.com.agi.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

/**
 * Factory para criar e gerenciar instâncias do WebDriver.
 * Suporta Chrome e Firefox com opcões headless.
 */
public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    public static WebDriver create() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        String headless = System.getProperty("headless", "false").toLowerCase();

        WebDriver driver = createBrowser(browser, Boolean.parseBoolean(headless));

        // Configurações gerais
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();

        logger.info("WebDriver criado: {} (headless: {})", browser, headless);
        return driver;
    }

    private static WebDriver createBrowser(String browser, boolean headless) {
        if ("firefox".equals(browser)) {
            return createFirefox(headless);
        }
        return createChrome(headless);
    }

    private static WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
                "--window-size=1920,1080",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions"
        );

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--width=1920", "--height=1080");
        return new FirefoxDriver(options);
    }
}
