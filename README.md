# 🎯 Blog Agi — Automação de Testes Web

[![Java Version](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)
[![Selenium Version](https://img.shields.io/badge/Selenium-4.18+-green)](https://www.selenium.dev/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red)](https://maven.apache.org/)

Projeto de automação de testes do [Blog do Agi](https://blogdoagi.com.br/) com **Selenium WebDriver**, **JUnit 5**, **AssertJ**, **Allure Report** e **Maven**.

> Observação: os badges/links do GitHub usam placeholders porque o repositório não informa o usuário/organização final.

---

## 📋 Índice

- [Visão geral](#-visão-geral)
- [Pré-requisitos](#-pré-requisitos)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Executar os testes](#-executar-os-testes)
- [Allure Report](#-allure-report)
- [CI/CD](#-cicd)
- [Troubleshooting](#-troubleshooting)
- [Contribuindo](#-contribuindo)

---

## 🎯 Visão geral

O projeto cobre estes cenários principais:

- pesquisa com termo válido;
- pesquisa com termo inexistente;
- pesquisa com múltiplos termos;
- carregamento da página inicial.

Os testes ficam em `src/test/java/br/com/agi/` e usam Page Objects para manter o fluxo organizado.

---

## 🖥️ Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Google Chrome** ou **Firefox**

Para conferir:

```bash
java -version
mvn -version
```

---

## 📁 Estrutura do projeto

```text
desafio-web/
├── .github/
│   └── workflows/
│       ├── ci.yml
│       └── allure-report.yml
├── allure.properties
├── allure-results/                 # saída do Allure gerada pelos testes
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/example/Main.java   # código exemplo gerado pelo IntelliJ
│   └── test/
│       └── java/br/com/agi/
│           ├── base/BaseTest.java
│           ├── pages/HomePage.java
│           ├── pages/SearchResultsPage.java
│           ├── tests/SearchTest.java
│           └── utils/DriverFactory.java
└── target/                          # build gerado pelo Maven
```

Observação: não existe arquivo `LICENSE` neste repositório no momento.

---

## 🚀 Executar os testes

### Todos os testes

```bash
mvn test
```

### Headless

```bash
mvn test -Dheadless=true
```

### Browser alternativo

```bash
mvn test -Dbrowser=firefox
```

### Um teste específico

```bash
mvn test -Dtest=SearchTest
```

### Um método específico

```bash
mvn test -Dtest=SearchTest#deveRetornarResultadosAoPesquisarTermoValido
```

### Limpar e executar

```bash
mvn clean test -Dheadless=true
```

---

## 📊 Allure Report

### Onde os resultados são gerados

O projeto grava os resultados em `allure-results/` na raiz do repositório. Isso está alinhado com:

- `allure.properties`
- configuração do plugin `allure-maven` no `pom.xml`

### Gerar relatório

```bash
mvn allure:report
```

O relatório estático é gerado em:

```text
target/site/allure-maven-plugin-report/index.html
```

### Abrir o relatório

```bash
open target/site/allure-maven-plugin-report/index.html
```

### Servir em modo local

```bash
mvn allure:serve
```

---

## 🔄 CI/CD

O repositório possui estes workflows em `.github/workflows/`:

- `ci.yml` — executa testes em headless e publica os artefatos do Allure;
- `allure-report.yml` — baixa o artefato do último `ci.yml`, gera o relatório com Maven e publica o HTML.

Pontos importantes:

- o job de CI salva `allure-results/` e `target/site/allure-maven-plugin-report/` como artefatos;
- o relatório Allure é publicado a partir de `target/site/allure-maven-plugin-report/`.

Se você for publicar no GitHub Pages, ajuste o repositório/branch para o seu usuário.

---

## 🐛 Troubleshooting

### Chrome não encontrado

No macOS, verifique se o Chrome está instalado em `/Applications/Google Chrome.app`.

### Maven não encontrado

No macOS, se estiver usando Homebrew:

```bash
brew install maven
```

### A porta 4040 do Allure já está em uso

```bash
lsof -ti:4040 | xargs kill -9
```

### Testes passam localmente, mas falham no CI

Execute localmente em headless para reproduzir o cenário do CI:

```bash
mvn clean test -Dheadless=true
```

---

## 🔗 Links úteis

- [Blog do Agi](https://blogdoagi.com.br/)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Allure Report](https://allurereport.org/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## 👥 Contribuindo

1. Faça um fork do projeto.
2. Crie uma branch para sua alteração.
3. Faça commit das mudanças.
4. Abra um Pull Request.

---

## 📜 Licença

Este repositório ainda não possui um arquivo `LICENSE`.

---

## 📞 Suporte

Abra uma issue no repositório ou ajuste os placeholders de GitHub conforme o seu usuário/organização.

---

Última atualização: abril de 2026
