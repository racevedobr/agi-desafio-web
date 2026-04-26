package br.com.agi.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Page Object para a página inicial do Blog do Agi.
 */
public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private static final String URL = "https://blogdoagi.com.br/";

    // Localizadores
    private final By searchIcon = By.cssSelector(".ast-search-menu-icon.full-screen .ast-search-icon, .ast-header-search .ast-search-icon, .astra-search-icon");
    private final By searchInput = By.cssSelector("#ast-seach-full-screen-form input.search-field, form.search-form input.search-field, input[name='s']");
    private final By searchButton = By.cssSelector("#search_submit, .search-submit, button[type='submit'], input[type='submit']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public HomePage open() {
        logger.info("Abrindo URL: {}", URL);
        driver.get(URL);
        Allure.step("Página inicial aberta: " + URL);
        return this;
    }

    public SearchResultsPage search(String searchTerm) {
        logger.info("Executando pesquisa por: {}", searchTerm);
        Allure.step("Buscando por: " + searchTerm);

        try {
            String currentUrl = driver.getCurrentUrl();

            if (!ensureSearchInputVisible()) {
                performDirectSearch(searchTerm);
                return new SearchResultsPage(driver);
            }

            typeSearchTermWithRetry(searchTerm);
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));

            // ENTER é mais estável em layouts onde o botão de submit pode ficar oculto.
            input.sendKeys(Keys.ENTER);
            logger.debug("Pesquisa submetida com ENTER");

            try {
                new WebDriverWait(driver, Duration.ofSeconds(4)).until(d -> {
                    String url = d.getCurrentUrl();
                    return !url.equals(currentUrl) || url.contains("?s=") || url.contains("/search");
                });
            } catch (TimeoutException ignored) {
                // Fallback: tenta clicar em um botão de submit visível.
                List<WebElement> visibleButtons = driver.findElements(searchButton)
                        .stream()
                        .filter(WebElement::isDisplayed)
                        .toList();

                if (!visibleButtons.isEmpty()) {
                    wait.until(ExpectedConditions.elementToBeClickable(visibleButtons.get(0))).click();
                    logger.debug("Fallback aplicado: clique no botão de pesquisa");
                }
            }

            // Aguarda transição para página de busca.
            wait.until(d -> {
                String url = d.getCurrentUrl();
                return !url.equals(currentUrl) || url.contains("?s=") || url.contains("/search");
            });

        } catch (Exception e) {
            logger.error("Erro durante a pesquisa: {}", e.getMessage());
            throw new RuntimeException("Falha ao realizar pesquisa", e);
        }

        return new SearchResultsPage(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    private void typeSearchTermWithRetry(String searchTerm) {
        int attempt = 0;
        while (attempt < 3) {
            attempt++;
            try {
                WebElement input = new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(searchInput));
                input.click();
                input.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.DELETE);
                input.sendKeys(searchTerm);
                logger.debug("Termo digitado no campo de pesquisa (tentativa {})", attempt);
                return;
            } catch (StaleElementReferenceException | TimeoutException ex) {
                logger.warn("Campo de busca indisponível na tentativa {}. Tentando abrir busca novamente...", attempt);
                clickSearchIconIfPossible();
            }
        }

        throw new RuntimeException("Campo de busca não disponível após tentativas");
    }

    private boolean ensureSearchInputVisible() {
        if (isSearchInputReady(Duration.ofSeconds(3))) {
            logger.debug("Campo de pesquisa já disponível sem clicar no ícone");
            return true;
        }

        clickSearchIconIfPossible();

        return isSearchInputReady(Duration.ofSeconds(5));
    }

    private void performDirectSearch(String searchTerm) {
        String encodedTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String searchUrl = URL + "?s=" + encodedTerm;
        logger.debug("Fallback aplicado: pesquisa direta por URL: {}", searchUrl);
        driver.get(searchUrl);
    }

    private boolean isSearchInputReady(Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(searchInput));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    private void clickSearchIconIfPossible() {
        try {
            WebElement icon = new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.elementToBeClickable(searchIcon));
            icon.click();
            logger.debug("Ícone de pesquisa clicado");
        } catch (Exception clickFailure) {
            try {
                WebElement icon = new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.presenceOfElementLocated(searchIcon));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
                logger.debug("Ícone de pesquisa clicado via JavaScript");
            } catch (Exception ignored) {
                logger.debug("Ícone de pesquisa não clicável no momento; seguindo com fallback");
            }
        }
    }
}
