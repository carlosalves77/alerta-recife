# Alerta Recife - Backend

Este é o backend do projeto Alerta Recife, uma aplicação Spring Boot para gerenciar e notificar alertas na cidade do Recife.

## Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot**
    *   **Spring Web:** Para a criação de APIs REST.
    *   **Spring Data JPA:** Para a persistência de dados.
    *   **Spring Security:** Para a autenticação e autorização de usuários, com suporte a OAuth2 (Google) e JWT.
*   **PostgreSQL com PostGIS:** Banco de dados para armazenar as informações, com suporte a dados geoespaciais.
*   **Flyway:** Para o versionamento e migração do banco de dados.
*   **Hibernate Spatial:** Para o manuseio de dados geoespaciais.
*   **Cloudflare R2:** Para o armazenamento de arquivos (utilizando o SDK da AWS para S3).
*   **SpringDoc OpenAPI:** Para a documentação da API (Swagger).
*   **Docker:** Para a conteinerização da aplicação e do banco de dados.
*   **Maven:** Para o gerenciamento de dependências e build do projeto.

## Como Executar o Projeto

### Pré-requisitos

*   Java 17
*   Maven
*   Docker e Docker Compose

### Configuração

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    cd alerta-recife
    ```

2.  **Crie um arquivo `.env`** na raiz do projeto, baseado no `docker-compose.yml`, com as seguintes variáveis:
    ```env
    postgres_user=<seu-usuario-postgres>
    postgres_password=<sua-senha-postgres>
    postgres_database=alerta_recife
    spring_user=<seu-usuario-spring>
    spring_password=<sua-senha-spring>
    JWT_SECRET=<seu-segredo-jwt>
    R2_ENDPOINT=<seu-endpoint-r2>
    R2_ACCESS_KEY=<sua-chave-de-acesso-r2>
    R2_SECRET_KEY=<sua-chave-secreta-r2>
    GOOGLE_CLIENT_ID=<seu-google-client-id>
    GOOGLE_CLIENT_SECRET=<seu-google-client-secret>
    ```

### Executando com Docker

Para subir a aplicação e o banco de dados, execute o comando:

```bash
docker-compose up -d
```

A aplicação estará disponível em `http://localhost:4010`.

## API

A documentação da API (Swagger) pode ser acessada em:

`http://localhost:4010/swagger-ui.html`

## Estrutura do Projeto

*   `src/main/java`: Código fonte da aplicação.
*   `src/main/resources`: Arquivos de configuração e migrations do Flyway.
*   `pom.xml`: Arquivo de configuração do Maven.
*   `Dockerfile`: Arquivo para a criação da imagem Docker da aplicação.
*   `docker-compose.yml`: Arquivo para a orquestração dos contêineres.
