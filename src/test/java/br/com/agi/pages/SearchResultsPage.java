package br.com.agi.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Page Object para a página de resultados de pesquisa.
 */
public class SearchResultsPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(SearchResultsPage.class);

    // Localizadores
    private final By noResults = By.cssSelector(".no-results, .not-found, .no-posts, .search-no-results");
    private final By resultTitle = By.cssSelector("article h2.entry-title a, article h3.entry-title a, .entry-title a, h2.post-title a, h3.post-title a, a.post-title");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public int countResults() {
        int total = getAllResultTitles().size();
        logger.info("Total de resultados encontrados: {}", total);
        Allure.step("Contagem de resultados: " + total);
        return total;
    }

    public boolean hasNoResultsMessage() {
        boolean hasMessage = !driver.findElements(noResults).isEmpty();
        logger.info("Mensagem de 'nenhum resultado': {}", hasMessage);
        Allure.step("Verificação de mensagem de nenhum resultado: " + hasMessage);
        return hasMessage;
    }

    public String getFirstResultTitle() {
        List<String> titles = getAllResultTitles();
        if (titles.isEmpty()) {
            logger.warn("Nenhum resultado encontrado");
            return "";
        }
        String title = titles.get(0);
        logger.info("Título do primeiro resultado: {}", title);
        Allure.step("Primeiro resultado: " + title);
        return title;
    }

    public List<String> getAllResultTitles() {
        return driver.findElements(resultTitle)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isBlank())
                .toList();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public int getResultsCount() {
        return countResults();
    }
}
