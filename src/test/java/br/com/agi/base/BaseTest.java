package br.com.agi.base;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import br.com.agi.utils.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe base para todos os testes automatizados.
 * Responsável por inicializar e finalizar o WebDriver.
 */
public abstract class BaseTest {
    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeEach
    public void setUp() {
        logger.info("Iniciando setup do teste...");
        driver = DriverFactory.create();
        Allure.step("WebDriver inicializado com sucesso");
    }

    @AfterEach
    public void tearDown() {
        logger.info("Finalizando teste...");
        if (driver != null) {
            driver.quit();
            Allure.step("WebDriver finalizado");
        }
    }
}
