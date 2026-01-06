# 📅 API de Agendamentos Online

API RESTful para gerenciamento de agendamentos online, desenvolvida em **Java com Spring Boot**, seguindo princípios de **Arquitetura Limpa**, com foco em **segurança**, **manutenibilidade** e **prontidão para produção**.

O projeto é **monolítico**, porém bem estruturado em camadas, utilizando **JWT para autenticação**, **RBAC (Role-Based Access Control)** para autorização, **PostgreSQL** como banco de dados e **Swagger/OpenAPI** para documentação interativa.

---

## 🚀 Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot**
* Spring Web
* Spring Security
* Spring Data JPA
* **JWT (JSON Web Token)**
* **RBAC (Controle de Acesso por Papéis)**
* **PostgreSQL**
* **Swagger / OpenAPI**
* Maven

---

## 🏗️ Arquitetura

Embora seja um projeto **monolítico**, a aplicação segue conceitos de **Arquitetura Limpa**, separando responsabilidades e facilitando manutenção e evolução.

### Camadas principais:

* **Controller (API Layer)**
  Responsável por receber as requisições HTTP e retornar as respostas.

* **Service (Use Cases / Business Rules)**
  Contém as regras de negócio e orquestra os fluxos da aplicação.

* **Domain / Model**
  Entidades e objetos de domínio.

* **Repository (Persistence Layer)**
  Comunicação com o banco de dados utilizando Spring Data JPA.

* **Security**
  Implementação de autenticação JWT e autorização baseada em roles (RBAC).

---

## 🔐 Segurança

### Autenticação

* Autenticação baseada em **JWT**
* Token gerado após login válido
* Token deve ser enviado no header:

```http
Authorization: Bearer <token>
```

### Autorização (RBAC)

O acesso aos endpoints é controlado por **roles**, garantindo que cada usuário execute apenas ações permitidas.

Exemplo de roles:

* `ROLE_ADMIN`
* `ROLE_USER`

As permissões são aplicadas via **Spring Security** e anotações como:

```java
@PreAuthorize("hasRole('ADMIN')")
```

---

## 📚 Documentação da API

A API é totalmente documentada utilizando **Swagger/OpenAPI**, permitindo testar os endpoints diretamente pelo navegador.

📌 **Acesse:**

```
http://localhost:8080/swagger-ui.html
```

ou

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Configuração do Projeto

### Pré-requisitos

* Java 17+
* Maven
* PostgreSQL

---

### Variáveis de Ambiente

Configure o `application.yml` ou `application.properties`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agendamentos
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: sua_chave_secreta
  expiration: 86400000
```

---

## ▶️ Executando o Projeto

```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

## 📌 Funcionalidades Principais

* Cadastro e autenticação de usuários
* Geração e validação de JWT
* Controle de acesso por roles (RBAC)
* Gerenciamento de agendamentos
* Integração com banco de dados PostgreSQL
* Documentação automática com Swagger

---

## 🧪 Testes

Os endpoints podem ser testados via:

* Swagger UI
* Postman / Insomnia

Lembre-se de incluir o **JWT no header** para endpoints protegidos.

---

## 📈 Possíveis Evoluções

* Cache com Redis
* Refresh Token
* Dockerização
* Monitoramento com Actuator + Prometheus
* Migração para arquitetura de microsserviços

---

## 👨‍💻 Autor

Desenvolvido por **Pedro De Almeida Peixoto** 🚀

Se este projeto te ajudou, deixe uma ⭐ no repositório!
