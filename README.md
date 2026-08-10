# SGHSS - Sistema de Gestão Hospitalar e de Serviços de Saúde

O SGHSS é uma plataforma acadêmica desenvolvida como Produto Mínimo Viável (MVP) para gerenciar o fluxo operacional e clínico de unidades de saúde. O sistema abrange desde a recepção (check-in, filas) até o atendimento médico (prontuário eletrônico, prescrições) utilizando controle de acesso rigoroso (RBAC) e regras de segurança.

## 🛠️ Tecnologias Utilizadas

**Back-end:**
* Java 21
* Spring Boot 3.x
* Spring Security + Auth0 JWT (JSON Web Tokens)
* Spring Data JPA / Hibernate
* PostgreSQL
* Docker & Docker Compose (Ambiente de Banco de Dados)
* JUnit 5 & Mockito (Testes Automatizados)
* Swagger/OpenAPI (Documentação da API)

**Front-end:**
* React (via Vite)
* TypeScript
* Styled Components
* Axios

---

## ⚙️ Pré-requisitos

Para rodar este projeto localmente, é necessário ter instalado em sua máquina:
1. **Docker e Docker Compose** (Essencial para o banco de dados)
2. **Java Development Kit (JDK) 21** (Apenas se for rodar via terminal sem Docker)
3. **Node.js** (versão 18 ou superior para o front-end)
4. **Git** (Para clonar os repositórios)

---

## 🔐 Configuração de Variáveis de Ambiente (Segurança)

Por adotar as práticas do *12-Factor App*, a chave criptográfica do JWT (e outras variáveis sensíveis) não é enviada para o repositório.

Antes de iniciar a aplicação, você precisa configurar o ambiente:
1. Na pasta raiz do back-end, localize o arquivo de modelo chamado **`._env`**.
2. Crie uma cópia deste arquivo e renomeie-a para **`.env`** (ou simplesmente duplique e renomeie).
3. O arquivo `.env` já possui uma chave de teste configurada (`JWT_SECRET`), mas você pode alterá-la se desejar.

---

## 🚀 Passo a Passo para Execução (Back-end e Banco de Dados)

O projeto já conta com um arquivo `docker-compose.yml` pré-configurado com a imagem do PostgreSQL (`postgres:16-alpine`) e o serviço da aplicação.

### Opção 1: Execução Completa via Docker (Recomendado)
A maneira mais fácil de rodar o ambiente inteiro (Banco de Dados + API). Abra o terminal na raiz do back-end e execute:

> ```bash
> docker-compose up -d
> ```

Isso fará com que o Docker baixe a imagem do PostgreSQL, crie o banco `sghss_db`, o usuário `root` e a senha `rootpassword`. Em seguida, construirá e iniciará o container da aplicação Java na porta `8080`, injetando automaticamente as variáveis do arquivo `.env`.

### Opção 2: Execução para Desenvolvimento (Banco no Docker + API no Gradle)
Se você deseja apenas subir o banco de dados para conseguir debugar e alterar o código Java localmente:

**1. Suba apenas o container do Banco de Dados:**
> ```bash
> docker-compose up -d db
> ```

**2. Inicie a API via Gradle:**
> **Para Windows:**
> ```cmd
> gradlew.bat bootRun
> ```
> **Para Linux/Mac:**
> ```bash
> ./gradlew bootRun
> ```

**💡 Importante (Data Seeder):**
Sempre que a aplicação iniciar pela primeira vez com o banco vazio, o Spring Boot executará a classe `DataSeeder`. Esse processo populará o banco de dados com centenas de registros (médicos, pacientes, escalas e agendas) para garantir que o sistema já nasça com massa de dados pronta para testes de fluxo.

**3. Acessando a Documentação (Swagger)**
Com a API rodando, a documentação de todos os endpoints estará disponível em:
👉 `http://localhost:8080/swagger-ui.html`

---

## 💻 Passo a Passo para Execução (Front-end)

O front-end consome a API rodando na porta 8080. O projeto encontra-se em um repositório separado e precisa ser clonado.

**1. Clonar o Repositório**
Abra um novo terminal (fora da pasta do back-end) e faça o clone do projeto front-end:

> ```bash
> git clone [https://github.com/WeslleiBrito/sgc-front.git](https://github.com/WeslleiBrito/sgc-front.git)
> cd sgc-front
> ```

**2. Instalação de Dependências**
Com o terminal aberto na nova pasta do projeto, instale as bibliotecas necessárias:

> ```bash
> npm install
> ```

**3. Iniciando o Servidor de Desenvolvimento**

> ```bash
> npm run dev
> ```

A aplicação abrirá no seu navegador, por padrão, no endereço:
👉 `http://localhost:5173`

---

## 🔑 Contas de Teste (Massa de Dados)

O `DataSeeder` já cria usuários padrão com diferentes perfis de acesso para testar os bloqueios de segurança e as visões distintas do sistema. **A senha para todos os usuários abaixo é `123456`.**

| Perfil de Acesso | Login (E-mail) | Papel no Sistema |
| :--- | :--- | :--- |
| **Administrador** | `admin.wesllei@sghss.com` | Acesso total, incluindo cadastros. |
| **Recepcionista** | `recepcao.ana@sghss.com` | Opera o Dashboard da recepção (Check-in). |
| **Médico** | `medico@sghss.com` | Possui pacientes agendados. |
| **Paciente** | `paciente@sghss.com` | Acesso apenas aos próprios dados. |

*(Nota: O Seeder também gera dezenas de outros médicos no padrão `medico.[nome].[id]@sghss.com`)*

---

## 🧪 Rodando os Testes Automatizados

O projeto possui cobertura de testes unitários e de integração para validar as regras de negócio de agendamentos e segurança. Para executá-los, rode no terminal da pasta do back-end:

> **Para Windows:**
> ```cmd
> gradlew.bat test
> ```

> **Para Linux/Mac:**
> ```bash
> ./gradlew test
> ```

---

## 📚 Primeiros Passos e Referências

### Documentação de Referência
Para consultas futuras sobre a base deste projeto Spring Boot, por favor, considere as seguintes seções:

* [Documentação oficial do Gradle](https://docs.gradle.org)
* [Guia de Referência do Plugin Gradle do Spring Boot](https://docs.spring.io/spring-boot/4.1.0/gradle-plugin)
* [Criar uma imagem OCI](https://docs.spring.io/spring-boot/4.1.0/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.0/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validação (Validation)](https://docs.spring.io/spring-boot/4.1.0/reference/io/validation.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.1.0/reference/using/devtools.html)

### Guias
Os guias a seguir ilustram como utilizar alguns recursos de forma prática:

* [Construindo um Web Service RESTful](https://spring.io/guides/gs/rest-service/)
* [Servindo Conteúdo Web com Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Construindo serviços REST com Spring](https://spring.io/guides/tutorials/rest/)
* [Acessando Dados com JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validação de Entrada de Dados](https://spring.io/guides/gs/validating-form-input/)

### Links Adicionais
Estas referências adicionais também podem ajudar no desenvolvimento:

* [Gradle Build Scans – insights para a compilação do seu projeto](https://scans.gradle.com#gradle)