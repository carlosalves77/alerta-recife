<div align="center">

#  Alerta Recife — Backend API

**Sistema de monitoramento e alerta de alagamentos em tempo real para a cidade do Recife**

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-PostGIS-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/K3s-Deploy-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)

---

*API RESTful que permite aos cidadãos reportar pontos de alagamento geolocalizados, acompanhar a intensidade das ocorrências e visualizar as áreas de risco em um mapa interativo.*

</div>

---

##  Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Endpoints da API](#-endpoints-da-api)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração e Execução](#-configuração-e-execução)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [CI/CD](#-cicd)
- [Licença](#-licença)

---

##  Sobre o Projeto

Recife é uma das capitais brasileiras mais vulneráveis a alagamentos. O **Alerta Recife** nasce como uma plataforma colaborativa onde os próprios cidadãos podem:

-  **Reportar alagamentos** com localização geográfica precisa (latitude/longitude)
-  **Anexar fotos** das ocorrências, armazenadas no Cloudflare R2
-  **Classificar a intensidade** do alagamento (`BAIXA`, `MEDIA`, `ALTA`, `CRITICA`)
-  **Confirmar ocorrências** de outros usuários através de votos de confirmação
-  **Visualizar em mapa** 3D interativo (Mapbox) todos os pontos de risco em tempo real
-  **Autenticação segura** via Google OAuth2 ou credenciais tradicionais com JWT

---

##  Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend (Vue.js)                       │
│              Mapbox GL JS · 3D Terrain · PWA                │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTPS
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                  Spring Boot 4.0.6 (API)                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────┐  │
│  │Controller│→ │ Service  │→ │Repository│→ │  Entities  │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────────┘  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │   Security: JWT + OAuth2 (Google) + Spring Security  │   │
│  └──────────────────────────────────────────────────────┘   │
└─────┬──────────────────────────────────┬────────────────────┘
      │                                  │
      ▼                                  ▼
┌─────────────┐                 ┌─────────────────┐
│ PostgreSQL  │                 │  Cloudflare R2   │
│  + PostGIS  │                 │ (Object Storage) │
│  (SRID 4326)│                 │  via AWS S3 SDK  │
└─────────────┘                 └─────────────────┘
```

---

##  Tecnologias

| Camada          | Tecnologia                          | Propósito                                          |
|-----------------|-------------------------------------|----------------------------------------------------|
| **Linguagem**   | Java 17                             | Linguagem principal                                |
| **Framework**   | Spring Boot 4.0.6                   | Framework base da aplicação                        |
| **API REST**    | Spring Web MVC                      | Exposição dos endpoints REST                       |
| **Persistência**| Spring Data JPA + Hibernate Spatial | ORM com suporte a dados geoespaciais               |
| **Banco**       | PostgreSQL 16 + PostGIS 3.4         | Armazenamento relacional + geoespacial             |
| **Migrações**   | Flyway                              | Versionamento e migração do schema                 |
| **Segurança**   | Spring Security + OAuth2 + JWT      | Autenticação e autorização                         |
| **Storage**     | Cloudflare R2 (AWS S3 SDK)          | Upload e armazenamento de imagens                  |
| **Docs**        | SpringDoc OpenAPI (Swagger)         | Documentação interativa da API                     |
| **Build**       | Maven                               | Gerenciamento de dependências e build              |
| **Container**   | Docker + Docker Compose             | Containerização e orquestração local               |
| **CI/CD**       | GitHub Actions + K3s                | Build, push e deploy automatizado                  |
| **Utilitários** | Lombok                              | Redução de boilerplate Java                        |

---

##  Endpoints da API

###  Autenticação (`/api/v1/auth`)

| Método | Rota             | Descrição                          | Auth |
|--------|------------------|------------------------------------|------|
| POST   | `/register`      | Registrar novo usuário             | ❌    |
| POST   | `/login`         | Login com credenciais (retorna JWT)| ❌    |
| GET    | `/me`            | Obter perfil do usuário autenticado| 🔒    |

###  Pontos de Alagamento (`/api/v1/flooding`)

| Método  | Rota             | Descrição                                  | Auth |
|---------|------------------|--------------------------------------------|------|
| GET     | `/`              | Listar todos os pontos de alagamento       | ❌    |
| POST    | `/`              | Criar novo ponto (multipart: dados + fotos)| 🔒    |
| GET     | `/user`          | Listar pontos do usuário autenticado       | 🔒    |
| PATCH   | `/{id}/intensity`| Atualizar intensidade de um ponto          | ❌    |
| PATCH   | `/{id}/votes`    | Incrementar votos de confirmação           | ❌    |
| DELETE  | `/{id}`          | Deletar ponto (somente o autor)            | 🔒    |
| DELETE  | `/all`           | Deletar todos os pontos (admin)            | ❌    |

###  Upload de Imagens (`/api/v1/image`)

| Método | Rota       | Descrição                         | Auth |
|--------|------------|-----------------------------------|------|
| POST   | `/upload`  | Upload de imagem para o R2        | ❌    |

> 📖 **Documentação Swagger completa:** `http://localhost:4010/swagger-ui.html`

---

##  Pré-requisitos

Certifique-se de ter instalado:

- [Java 17+](https://adoptium.net/) (JDK)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/)

---

##  Configuração e Execução

### 1. Clone o repositório

```bash
git clone https://github.com/carlosalves77/alerta-recife.git
cd alerta-recife
```

### 2. Crie a rede Docker (necessária para comunicação entre containers)

```bash
docker network create internal
```

### 3. Configure as variáveis de ambiente

Crie um arquivo **`.env`** na raiz do projeto:

```env
# ─── Banco de Dados ──────────────────────────────
postgres_user=alerta_admin
postgres_password=sua_senha_segura
postgres_database=alerta_recife

# ─── Credenciais Spring ─────────────────────────
spring_user=alerta_admin
spring_password=sua_senha_segura

# ─── JWT ─────────────────────────────────────────
JWT_SECRET=sua_chave_jwt_256bits_segura

# ─── Cloudflare R2 (Object Storage) ─────────────
R2_ENDPOINT=https://seu-account-id.r2.cloudflarestorage.com
R2_ACCESS_KEY=sua_access_key
R2_SECRET_KEY=sua_secret_key

# ─── Google OAuth2 ──────────────────────────────
GOOGLE_CLIENT_ID=seu_client_id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=seu_client_secret
```

### 4. Suba a aplicação

```bash
docker-compose up -d
```

### 5. Verifique os containers

```bash
docker-compose ps
```

A aplicação estará disponível em:

| Serviço          | URL                                    |
|------------------|----------------------------------------|
| **API**          | `http://localhost:4010`                |
| **Swagger UI**   | `http://localhost:4010/swagger-ui.html`|
| **PostgreSQL**   | `localhost:5005` (porta mapeada)       |

### Executando localmente (sem Docker)

```bash
# Certifique-se de ter o PostgreSQL + PostGIS rodando na porta 5005
mvn spring-boot:run
```

---

##  Variáveis de Ambiente

| Variável                | Obrigatória | Descrição                                       |
|-------------------------|-------------|--------------------------------------------------|
| `postgres_user`         | ✅           | Usuário do PostgreSQL                            |
| `postgres_password`     | ✅           | Senha do PostgreSQL                              |
| `postgres_database`     | ✅           | Nome do banco de dados                           |
| `spring_user`           | ✅           | Usuário do datasource Spring                     |
| `spring_password`       | ✅           | Senha do datasource Spring                       |
| `JWT_SECRET`            | ✅           | Chave secreta para assinatura dos tokens JWT     |
| `R2_ENDPOINT`           | ✅           | Endpoint do Cloudflare R2                        |
| `R2_ACCESS_KEY`         | ✅           | Access Key do R2                                 |
| `R2_SECRET_KEY`         | ✅           | Secret Key do R2                                 |
| `GOOGLE_CLIENT_ID`      | ✅           | Client ID do Google OAuth2                       |
| `GOOGLE_CLIENT_SECRET`  | ✅           | Client Secret do Google OAuth2                   |

---

##  Estrutura do Projeto

```
alerta-recife/
├── 📂 .github/workflows/
│   └── main.yml                    # Pipeline CI/CD (GitHub Actions → K3s)
├── 📂 src/main/java/com/carldev/alerta_recife/
│   ├── 📂 config/                  # Configurações (CORS, Swagger, R2 Client)
│   ├── 📂 controller/              # Controllers REST
│   │   ├── FloodingPointsController.java
│   │   ├── UserAuthController.java
│   │   └── ImageStoreController.java
│   ├── 📂 dto/                     # Data Transfer Objects
│   │   ├── 📂 request/             #   Payloads de entrada
│   │   └── 📂 response/            #   Payloads de saída
│   ├── 📂 entity/                  # Entidades JPA
│   │   ├── FloodingPoints.java     #   Ponto de alagamento (geoespacial)
│   │   ├── FloodingPointImage.java #   Imagem vinculada ao ponto
│   │   └── UserAuth.java           #   Usuário autenticável
│   ├── 📂 exception/               # Handlers de exceção globais
│   ├── 📂 mapper/                  # Mapeadores Entity ↔ DTO
│   ├── 📂 repository/              # Repositórios Spring Data JPA
│   ├── 📂 security/                # Configuração de segurança
│   │   ├── SecurityConfig.java     #   Filtros, CORS, OAuth2
│   │   ├── JwtTokenProvider.java   #   Geração/validação de tokens JWT
│   │   ├── JwtAuthenticationFilter.java
│   │   └── 📂 google_auth/        #   Handlers do Google OAuth2
│   ├── 📂 service/                 # Camada de regras de negócio
│   └── 📂 utils/                   # Enums e utilitários
│       └── IntensityOfTheFlooding  #   BAIXA | MEDIA | ALTA | CRITICA
├── 📂 src/main/resources/
│   ├── application.properties      # Configurações da aplicação
│   └── 📂 db/migration/            # Migrações Flyway (SQL)
│       ├── V1__create_initial_tables.sql
│       ├── V2__add_description_to_flooding_points.sql
│       ├── V3__add_street_to_flooding_points.sql
│       ├── V4__add_flooding_points_pictures_points.sql
│       ├── V5__Create_Table_Flooding_Point_Images.sql
│       └── V6__add_googleauth_authentication_.sql
├── Dockerfile                      # Build multi-stage (Maven → JRE Alpine)
├── docker-compose.yml              # Orquestração: API + PostgreSQL/PostGIS
├── pom.xml                         # Dependências Maven
└── README.md
```

---

##  CI/CD

O projeto utiliza **GitHub Actions** com **self-hosted runner** para deploy automatizado:

```
Push na master → Build da imagem Docker → Push para Docker Hub → Deploy no K3s (Kubernetes)
```

O pipeline (`main.yml`) executa:

1. **Checkout** do código
2. **Geração de tag** baseada no SHA do commit
3. **Build e Push** da imagem Docker para o Docker Hub
4. **Deploy** via `kubectl set image` no cluster K3s
5. **Rollout status** para verificar a saúde do deployment

---

## 🗃️ Modelo de Dados

```
┌──────────────────────────┐       ┌──────────────────────────┐
│    flooding_points       │       │      user_auth           │
├──────────────────────────┤       ├──────────────────────────┤
│ id (PK)                  │       │ id (PK)                  │
│ street                   │  N:1  │ username                 │
│ logger                   │◄──────│ email                    │
│ neighborhood             │       │ password                 │
│ reference_point          │       │ profile_picture          │
│ description              │       │ google_id                │
│ coordinates (POINT 4326) │       └──────────────────────────┘
│ intensity (ENUM)         │
│ active                   │       ┌──────────────────────────┐
│ confirmation_votes       │       │ flooding_point_images    │
│ registry_date            │  1:N  ├──────────────────────────┤
│ actualization_date       │──────►│ id (PK)                  │
│ user_id (FK)             │       │ image_url                │
└──────────────────────────┘       │ flooding_point_id (FK)   │
                                   └──────────────────────────┘
```

---

##  Licença

Este projeto é desenvolvido por **[@carlosalves77](https://github.com/carlosalves77)**.
