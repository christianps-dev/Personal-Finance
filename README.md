---

# 💰 Gerenciador de Finanças Pessoais

É uma aplicação completa de gerenciamento de finanças pessoais desenvolvida para ajudar utilizadores a controlarem os seus gastos, receitas, definirem metas de poupança e visualizarem a saúde financeira por meio de gráficos intuitivos.

---

## 🚀 Funcionalidades Principais

* **Controle de Fluxo de Caixa:** Registo de receitas (salário, investimentos) e despesas (moradia, alimentação, lazer).
* **Painel de Indicadores (Dashboard):** Gráficos interativos mostrando a distribuição dos gastos por categoria e evolução mensal.
* **Categorização Inteligente:** Criação e vínculo de transações a categorias personalizadas.
* **Limites de Gastos por Categoria:** Definição de orçamentos e tetos de gastos mensais para cada categoria, permitindo um controle rigoroso para não estourar o orçamento planejado.
* **Histórico de Transações:** Filtros avançados por data, tipo de transação e categoria.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

### Frontend

* **Angular:** Framework principal para construção da SPA (Single Page Application).
* **Bootstrap:** Estilização responsiva e componentes de interface modernos.
* **Chart.js:** Renderização dos gráficos dinâmicos do dashboard.

### Backend & Banco de Dados

* **Spring Boot (Java):** API RESTful robusta, responsável pelas regras de negócio e segurança.
* **PostgreSQL:** Banco de dados relacional para persistência dos dados financeiros.
* **Flyway:** Ferramenta de migração de banco de dados (Versionamento de Scripts SQL).

### Infraestrutura, Containerização & Deploy

* **Docker & Docker Compose:** Containerização de toda a aplicação para garantir consistência entre os ambientes de desenvolvimento e produção.
* **Nginx:** Servidor web utilizado como Proxy Reverso para a API e para servir os arquivos estáticos do Angular de forma performática.

---

## 📐 Arquitetura em Containers

Quando executado via Docker, o ecossistema do projeto é isolado numa rede virtual própria.

---

## 🐳 Como Executar o Projeto com Docker

A forma mais rápida de rodar a aplicação completa (Frontend, Backend e Banco de Dados) sem precisar de instalar Java, Node ou Postgres localmente é utilizando o **Docker Compose**.

### Pré-requisitos

* [Docker](https://docs.docker.com/get-docker/) instalado.
* [Docker Compose](https://docs.docker.com/compose/install/) instalado.

### Passo Único

Na raiz do projeto (onde se encontra o arquivo `docker-compose.yml`), execute o comando:

```bash
docker-compose up --build -d

```
