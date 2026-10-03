# 🩺 API de Médicos

> **Trabalho 1 — APIs e Web Services | UTFPR**

API REST desenvolvida em **Java + Spring Boot**, responsável pelo gerenciamento de informações de médicos, permitindo operações de cadastro, consulta, alteração, exclusão e pesquisa utilizando diferentes critérios.

A aplicação utiliza **MongoDB** como banco de dados para persistência dos registros.

---

## 📌 Sobre o projeto

O projeto consiste no desenvolvimento de uma API REST para disponibilização e gerenciamento de informações de médicos.

A API permite:

* 👨‍⚕️ Cadastrar médicos
* 🔎 Consultar médicos
* 📋 Listar médicos
* 🔍 Pesquisar por nome
* 🪪 Pesquisar por CRM
* 🩺 Pesquisar por especialidade
* ✏️ Alterar dados de médicos
* 🗑️ Excluir médicos
* ⚠️ Validar dados recebidos
* 🚨 Tratar erros e exceções
* 💾 Persistir os dados utilizando MongoDB

Os registros fornecidos para o trabalho podem ser importados diretamente para uma coleção MongoDB e posteriormente acessados pela API.

---

## 🚀 Tecnologias utilizadas

| Tecnologia             | Utilização                    |
| ---------------------- | ----------------------------- |
| ☕ Java                 | Linguagem de programação      |
| 🌱 Spring Boot         | Desenvolvimento da API        |
| 🌐 Spring Web          | Criação dos endpoints REST    |
| ✅ Bean Validation      | Validação dos dados           |
| 🍃 Spring Data MongoDB | Persistência dos dados        |
| 🍃 MongoDB             | Banco de dados NoSQL          |
| 📦 Maven               | Gerenciamento de dependências |
| 🧪 Postman             | Testes da API                 |
| 🐙 Git/GitHub          | Versionamento                 |

---

## 🏗️ Arquitetura

A aplicação foi organizada seguindo uma separação de responsabilidades:

```text
                    ┌─────────────────────┐
                    │      Cliente        │
                    │ Postman / Frontend  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controller      │
                    │    REST / HTTP      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       Service       │
                    │   Regras de negócio │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Repository      │
                    │ Spring Data MongoDB │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       MongoDB       │
                    │     Persistência    │
                    └─────────────────────┘
```

---

## 📂 Estrutura do projeto

```text
src/
└── main/
    ├── java/
    │   └── br/
    │       └── edu/
    │           └── utfpr/
    │               └── tsi/
    │                   └── medicos/
    │
    │                       ├── controller/
    │                       │   └── MedicoController.java
    │                       │
    │                       ├── dto/
    │                       │   ├── MedicoRequestDTO.java
    │                       │   └── MedicoResponseDTO.java
    │                       │
    │                       ├── exception/
    │                       │   ├── GlobalExceptionHandler.java
    │                       │   ├── MedicoNaoEncontradoException.java
    │                       │   └── RegraNegocioException.java
    │                       │
    │                       ├── model/
    │                       │   └── Medico.java
    │                       │
    │                       ├── repository/
    │                       │   └── MedicoRepository.java
    │                       │
    │                       └── service/
    │                           └── MedicoService.java
    │
    └── resources/
        └── application.properties
```

---

# 🍃 MongoDB

A persistência dos dados é realizada utilizando MongoDB.

Banco utilizado:

```text
medicos
```

Coleção:

```text
medicos
```

A aplicação utiliza o Spring Data MongoDB para realizar as operações de persistência.

---

## 🔌 Configuração do MongoDB

No arquivo:

```text
src/main/resources/application.properties
```

configure a conexão:

```properties
spring.application.name=api-medicos

server.port=8080

spring.data.mongodb.uri=mongodb://localhost:27017/medicos

spring.jackson.serialization.write-dates-as-timestamps=false
```

Caso esteja utilizando MongoDB Atlas, substitua a URI pela string de conexão fornecida pelo Atlas.

---

# 📥 Importação dos médicos

Os registros dos médicos podem ser importados diretamente para o MongoDB.

Exemplo utilizando o MongoDB Compass:

1. Abrir o MongoDB Compass
2. Conectar ao banco
3. Criar/selecionar o banco:

```text
medicos
```

4. Criar/selecionar a coleção:

```text
medicos
```

5. Selecionar:

```text
Add Data
```

6. Selecionar:

```text
Import JSON or CSV file
```

7. Selecionar o arquivo JSON
8. Realizar a importação

Após a importação, a API poderá consultar os registros diretamente da coleção.

---

# 🌐 Endpoints

A API utiliza o prefixo:

```text
/api/medicos
```

### Listar médicos

```http
GET /api/medicos
```

### Buscar médico por ID

```http
GET /api/medicos/{id}
```

### Cadastrar médico

```http
POST /api/medicos
```

### Alterar médico

```http
PUT /api/medicos/{id}
```

### Excluir médico

```http
DELETE /api/medicos/{id}
```

---

# 🔎 Filtros

A API permite realizar consultas utilizando parâmetros.

### Por nome

```http
GET /api/medicos?nome=João
```

### Por CRM

```http
GET /api/medicos?crm=12345
```

### Por especialidade

```http
GET /api/medicos?especialidade=Cardiologia
```

Também é possível combinar os filtros:

```http
GET /api/medicos?nome=João&crm=12345&especialidade=Cardiologia
```

---

# 📝 Exemplo de cadastro

### Requisição

```http
POST /api/medicos
Content-Type: application/json
```

```json
{
    "nome": "AARÃO SALOMÃO COHEN JÚNIOR",
    "crm": "27499",
    "uf": "MG",
    "especialidade": "MEDICINA DO TRABALHO",
    "areaAtuacao": null,
    "tipoInscricao": "Principal",
    "situacao": "Regular",
    "municipio": "Varginha",
    "dataInscricao": "1994-09-08",
    "primeiraInscricaoUf": "1994-09-08",
    "endereco": "BANCO DO BRASIL - CENTRO",
    "telefone": null,
    "instituicaoGraduacao": "ESCOLA DE MEDICINA SOUZA MARQUES",
    "anoFormatura": 1984
}
```

### Resposta

```json
{
    "id": "68f000000000000000000001",
    "nome": "AARÃO SALOMÃO COHEN JÚNIOR",
    "crm": "27499",
    "uf": "MG",
    "especialidade": "MEDICINA DO TRABALHO",
    "areaAtuacao": null,
    "tipoInscricao": "Principal",
    "situacao": "Regular",
    "municipio": "Varginha",
    "dataInscricao": "1994-09-08",
    "primeiraInscricaoUf": "1994-09-08",
    "endereco": "BANCO DO BRASIL - CENTRO",
    "telefone": null,
    "instituicaoGraduacao": "ESCOLA DE MEDICINA SOUZA MARQUES",
    "anoFormatura": 1984
}
```

---

# ⚠️ Tratamento de erros

A aplicação possui tratamento centralizado de exceções.

Exemplo de médico não encontrado:

```http
GET /api/medicos/999999
```

Resposta:

```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Médico não encontrado"
}
```

Para dados inválidos, a API retorna:

```http
400 Bad Request
```

acompanhado das informações referentes aos campos que não passaram pela validação.

---

# 🧪 Testando com Postman

Os endpoints podem ser testados utilizando o Postman.

Exemplo:

```text
GET
http://localhost:8080/api/medicos
```

Para cadastro:

```text
POST
http://localhost:8080/api/medicos
```

Body:

```text
raw → JSON
```

---

# ▶️ Como executar

## 1. Clonar o projeto

```bash
git clone <URL_DO_REPOSITORIO>
```

## 2. Entrar no diretório

```bash
cd api-medicos
```

## 3. Configurar o MongoDB

Certifique-se de que o MongoDB esteja em execução.

Por padrão:

```text
mongodb://localhost:27017
```

## 4. Executar a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou execute a classe:

```text
MedicosApplication.java
```

---

# 📌 URL da API

Após iniciar a aplicação:

```text
http://localhost:8080
```

Endpoint principal:

```text
http://localhost:8080/api/medicos
```

---

# 📚 Organização das responsabilidades

### Controller

Responsável por:

* receber requisições HTTP;
* definir endpoints;
* receber parâmetros;
* retornar respostas HTTP.

### Service

Responsável por:

* regras de negócio;
* validações;
* conversão entre DTO e Model;
* coordenação das operações.

### Repository

Responsável por:

* acesso ao MongoDB;
* consultas;
* inserções;
* alterações;
* exclusões.

### DTO

Responsável por controlar os dados enviados e retornados pela API.

### Model

Representa a entidade médico persistida no banco.

### Exception

Centraliza o tratamento dos erros da aplicação.

---

# 🎯 Objetivo

O projeto tem como objetivo aplicar conceitos de:

* APIs REST;
* Web Services;
* Spring Boot;
* arquitetura em camadas;
* DTOs;
* validação;
* tratamento de exceções;
* persistência de dados;
* MongoDB;
* comunicação HTTP;
* desenvolvimento de aplicações web.

---

## 👩‍💻 Autora

**Franciele Martins**

Tecnologia em Sistemas para Internet — UTFPR

---

## 📄 Licença

Projeto desenvolvido para fins acadêmicos no curso de Tecnologia em Sistemas para Internet da UTFPR.
