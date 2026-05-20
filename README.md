# 💰 Gerenciador de Finanças Pessoais

![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

É uma aplicação completa de gerenciamento de finanças pessoais desenvolvida para ajudar utilizadores a controlarem os seus gastos, receitas, definirem metas de poupança e visualizarem a saúde financeira por meio de gráficos intuitivos.

---

## 🚀 Funcionalidades Principais

* **Controle de Fluxo de Caixa:** Registo de receitas (salário, investimentos) e despesas (moradia, alimentação, lazer).
* **Painel de Indicadores (Dashboard):** Gráficos interativos mostrando a distribuição dos gastos por categoria e evolução mensal.
* **Categorização Inteligente:** Criação e vínculo de transações a categorias personalizadas.
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

Quando executado via Docker, o ecossistema do projeto é isolado numa rede virtual própria:

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
