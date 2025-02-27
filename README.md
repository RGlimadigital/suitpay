# 📌 API RESTful com Spring Boot, Hibernate e SQL Server

## 🎯 Objetivo

Implementação de uma API RESTful utilizando **Spring Boot**, **Hibernate** e **SQL Server**.

O projeto consiste na criação de uma aplicação Java que se conecta a um banco de dados **SQL Server** através do **Hibernate**, permitindo a manipulação de dados via **API RESTful**.

O foco é avaliar boas práticas de desenvolvimento, uso de **JPA**, e implementação de endpoints REST para gerenciar **produtos e categorias**.

---

## 🔧 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3+**
- **Spring Data JPA (Hibernate)**
- **Spring HATEOAS**
- **SQL Server**
- **Spring Web**
- **JUnit 5 e Mockito**
- **Maven**

---

## ⚙️ Configuração do Ambiente

### 1️⃣ Pré-requisitos
Antes de rodar o projeto, verifique se possui os seguintes requisitos instalados:
- **Java 17+** ([Download](https://adoptium.net/))
- **Maven 3+** ([Download](https://maven.apache.org/download.cgi))
- **SQL Server** ([Download](https://www.microsoft.com/pt-br/sql-server/sql-server-downloads))
- **Postman ou cURL** (para testar os endpoints)

---

## 📁 Configuração do Banco de Dados

### 1️⃣ Criar o Banco de Dados no SQL Server
Execute o seguinte comando no seu banco de dados SQL Server:
```sql
CREATE DATABASE testsuitpay;
