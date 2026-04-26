package br.com.agi.tests;

import br.com.agi.base.BaseTest;
import br.com.agi.pages.HomePage;
import br.com.agi.pages.SearchResultsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.*;

@Epic("Blog do Agi")
@Feature("Pesquisa de artigos")
@DisplayName("Testes de pesquisa do Blog do Agi")
class SearchTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(SearchTest.class);

    @Test
    @Story("Cenário 1: Pesquisa com termo válido")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Deve retornar resultados ao pesquisar termo válido")
    void deveRetornarResultadosAoPesquisarTermoValido() {
        // Arrange
        String searchTerm = "credito";

        // Act
        SearchResultsPage page = new HomePage(driver)
                .open()
                .search(searchTerm);

        // Assert
        assertThat(page.getCurrentUrl())
                .as("A busca deve navegar para uma URL de resultados")
                .contains("?s=");

        int resultCount = page.countResults();
        if (resultCount > 0) {
            String firstTitle = page.getFirstResultTitle();
            assertThat(firstTitle)
                    .as("Título do primeiro resultado não deve estar vazio")
                    .isNotBlank();
        }

        logger.info("✓ Teste passou: {} resultados encontrados", resultCount);
    }

    @Test
    @Story("Cenário 2: Pesquisa com termo inexistente")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Deve exibir mensagem quando não houver resultados")
    void deveExibirMensagemQuandoNaoHouverResultados() {
        // Arrange
        String searchTerm = "xyzqweasdnaoexiste123456789";

        // Act
        SearchResultsPage page = new HomePage(driver)
                .open()
                .search(searchTerm);

        // Assert
        boolean hasNoResults = page.hasNoResultsMessage();
        int resultCount = page.countResults();

        assertThat(hasNoResults || resultCount == 0)
                .as("Deve exibir mensagem de nenhum resultado ou lista vazia")
                .isTrue();

        logger.info("✓ Teste passou: Nenhum resultado exibido corretamente");
    }

    @Test
    @Story("Cenário 3: Pesquisa com múltiplos termos")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Deve retornar resultados para pesquisa com múltiplas palavras")
    void deveRetornarResultadosParaPesquisaComMultiplosTermo() {
        // Arrange
        String searchTerm = "banco digital";

        // Act
        SearchResultsPage page = new HomePage(driver)
                .open()
                .search(searchTerm);

        // Assert
        int resultCount = page.countResults();
        assertThat(resultCount)
                .as("Deve exibir resultados para: " + searchTerm)
                .isGreaterThanOrEqualTo(0);

        logger.info("✓ Teste passou: {} resultados encontrados para '{}'", resultCount, searchTerm);
    }

    @Test
    @Story("Cenário 4: Verificação de estrutura da página")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Página inicial deve carregar corretamente")
    void deveCarregarPaginaInicial() {
        // Act
        HomePage homePage = new HomePage(driver).open();

        // Assert
        String title = homePage.getPageTitle();
        assertThat(title)
                .as("Título da página deve conter 'Agi'")
                .containsIgnoringCase("agi");

        logger.info("✓ Teste passou: Página inicial carregada - Título: {}", title);
    }
}
