# 🩺 API de Médicos

API REST desenvolvida para processamento, normalização e consulta de dados de médicos.

O projeto utiliza uma arquitetura híbrida envolvendo **MongoDB** e **MySQL**, em que o MongoDB mantém os dados brutos provenientes da coleta e o MySQL armazena os dados normalizados utilizados pela aplicação.

---

## 📌 Visão geral

O fluxo da aplicação é:

```text
                         DADOS BRUTOS
                              │
                              ▼
                       ┌─────────────┐
                       │   MongoDB   │
                       │ JSON bruto  │
                       └──────┬──────┘
                              │
                              │ Importação /
                              │ Normalização
                              ▼
                    ┌────────────────────┐
                    │ ImportacaoService  │
                    └─────────┬──────────┘
                              │
                              ▼
                       ┌─────────────┐
                       │   MySQL     │
                       │ Normalizado │
                       └──────┬──────┘
                              │
                              ▼
                       ┌─────────────┐
                       │ Repository  │
                       └──────┬──────┘
                              │
                              ▼
                       ┌─────────────┐
                       │   Service   │
                       └──────┬──────┘
                              │
                              ▼
                       ┌─────────────┐
                       │ Controller  │
                       └──────┬──────┘
                              │
                              ▼
                         API REST
```

### Responsabilidade de cada banco

**MongoDB**

* Armazena o JSON original.
* Mantém os dados exatamente como foram coletados.
* Funciona como fonte dos dados para o processo de normalização.

**MySQL**

* Armazena os dados estruturados.
* Organiza os dados em entidades relacionais.
* É utilizado para consultas através da API.
* Permite utilização de chaves primárias, estrangeiras e índices.

---

# 🏗️ Arquitetura do domínio

O objeto `Medico` foi estruturado para evitar que todas as informações fiquem concentradas em uma única classe.

## Médico

```java
public class Medico {

    private Long id;
    private String nome;
    private String crm;
    private String uf;
    private String especialidade;
    private String areaAtuacao;
    private String tipoInscricao;
    private String situacao;
    private LocalDate dataInscricao;
    private LocalDate primeiraInscricaoUf;
    private String telefone;

    private Graduacao graduacao;
    private Endereco endereco;
}
```

O campo `uf` do `Medico` representa a **UF relacionada à inscrição/CRM**.

A UF pertencente ao endereço fica dentro do objeto `Endereco`.

---

# 🎓 Objeto Graduação

As informações referentes à formação acadêmica foram agrupadas em um objeto próprio.

```java
public class Graduacao {

    private Long id;
    private String instituicao;
    private Integer anoFormatura;
}
```

Isso evita manter no `Medico` campos como:

```java
private String instituicaoGraduacao;
private Integer anoFormatura;
```

passando a utilizar:

```java
private Graduacao graduacao;
```

### Exemplo

```json
{
    "graduacao": {
        "instituicao": "UNIVERSIDADE DE CUIABA",
        "anoFormatura": 2020
    }
}
```

---

# 📍 Objeto Endereço

O endereço também foi separado do objeto principal.

```java
public class Endereco {

    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String municipio;
    private String uf;
    private String cep;
}
```

Exemplo:

```json
{
    "endereco": {
        "logradouro": "Rua Exemplo",
        "numero": "100",
        "complemento": "Sala 5",
        "bairro": "Centro",
        "municipio": "Cuiabá",
        "uf": "MT",
        "cep": "78000-000"
    }
}
```

Os campos de endereço foram preparados para receber dados de endereço caso estejam disponíveis na fonte de dados.

---

# 🗄️ Estrutura relacional

A estrutura do MySQL será composta por três tabelas principais:

```text
┌──────────────────────────┐
│          medico          │
├──────────────────────────┤
│ PK id                    │
│    nome                  │
│    crm                   │
│    uf                    │
│    especialidade         │
│    area_atuacao          │
│    tipo_inscricao        │
│    situacao              │
│    data_inscricao        │
│    primeira_inscricao_uf │
│    telefone              │
│ FK graduacao_id          │
│ FK endereco_id           │
└───────────┬──────────────┘
            │
       ┌────┴─────┐
       │          │
       ▼          ▼
┌─────────────┐ ┌────────────────┐
│  graduacao  │ │    endereco    │
├─────────────┤ ├────────────────┤
│ PK id       │ │ PK id          │
│ instituicao │ │ logradouro     │
│ ano_formatura│ │ numero         │
└─────────────┘ │ complemento    │
                │ bairro         │
                │ municipio      │
                │ uf             │
                │ cep            │
                └────────────────┘
```

## Relacionamentos

```text
MEDICO
  │
  │ 1
  │
  │ 1
  ▼
GRADUACAO


MEDICO
  │
  │ 1
  │
  │ 1
  ▼
ENDERECO
```

A informação de graduação pertence ao médico, assim como seu endereço.

---

# 🛢️ Banco de dados

O banco utilizado pela aplicação é o **MySQL**, executado através do **XAMPP**.

## Criação do banco

```sql
CREATE DATABASE IF NOT EXISTS medicos
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE medicos;
```

---

# 📋 Tabela `graduacao`

```sql
CREATE TABLE graduacao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    instituicao VARCHAR(255),
    ano_formatura SMALLINT,

    PRIMARY KEY (id)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
```

### Campos

| Campo           | Tipo         | Descrição                            |
| --------------- | ------------ | ------------------------------------ |
| `id`            | BIGINT       | Identificador da graduação           |
| `instituicao`   | VARCHAR(255) | Instituição onde o médico se graduou |
| `ano_formatura` | SMALLINT     | Ano de conclusão da graduação        |

---

# 📍 Tabela `endereco`

```sql
CREATE TABLE endereco (
    id BIGINT NOT NULL AUTO_INCREMENT,
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(150),
    municipio VARCHAR(150),
    uf CHAR(2),
    cep VARCHAR(10),

    PRIMARY KEY (id),

    INDEX idx_endereco_municipio (municipio),
    INDEX idx_endereco_uf (uf)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
```

### Campos

| Campo         | Tipo         | Descrição                 |
| ------------- | ------------ | ------------------------- |
| `id`          | BIGINT       | Identificador do endereço |
| `logradouro`  | VARCHAR(255) | Rua, avenida etc.         |
| `numero`      | VARCHAR(20)  | Número do imóvel          |
| `complemento` | VARCHAR(100) | Complemento               |
| `bairro`      | VARCHAR(150) | Bairro                    |
| `municipio`   | VARCHAR(150) | Município                 |
| `uf`          | CHAR(2)      | Estado                    |
| `cep`         | VARCHAR(10)  | CEP                       |

O CEP é armazenado como `VARCHAR`, e não como número, porque é um identificador e pode possuir zeros à esquerda.

---

# 👨‍⚕️ Tabela `medico`

```sql
CREATE TABLE medico (
    id BIGINT NOT NULL AUTO_INCREMENT,

    nome VARCHAR(200) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    uf CHAR(2) NOT NULL,

    especialidade VARCHAR(255),
    area_atuacao VARCHAR(255),
    tipo_inscricao VARCHAR(30),
    situacao VARCHAR(50),

    data_inscricao DATE,
    primeira_inscricao_uf DATE,

    telefone VARCHAR(30),

    graduacao_id BIGINT,
    endereco_id BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT uk_medico_crm_uf
        UNIQUE (crm, uf),

    CONSTRAINT fk_medico_graduacao
        FOREIGN KEY (graduacao_id)
        REFERENCES graduacao(id),

    CONSTRAINT fk_medico_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES endereco(id),

    INDEX idx_medico_nome (nome),
    INDEX idx_medico_crm (crm),
    INDEX idx_medico_uf (uf),
    INDEX idx_medico_especialidade (especialidade),
    INDEX idx_medico_situacao (situacao)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
```

---

# 🔗 Modelo relacional completo

A estrutura final pode ser representada assim:

```text
                         ┌──────────────────────┐
                         │      GRADUACAO       │
                         ├──────────────────────┤
                         │ PK id                │
                         │    instituicao       │
                         │    ano_formatura     │
                         └──────────▲───────────┘
                                    │
                                    │ 1
                                    │
                                    │
                                    │ N
┌───────────────────────────────┐   │
│            MEDICO             │   │
├───────────────────────────────┤   │
│ PK id                         │───┘
│    nome                       │
│    crm                        │
│    uf                         │
│    especialidade              │
│    area_atuacao               │
│    tipo_inscricao             │
│    situacao                   │
│    data_inscricao             │
│    primeira_inscricao_uf      │
│    telefone                   │
│ FK graduacao_id               │
│ FK endereco_id                │
└───────────────┬───────────────┘
                │
                │ N
                │
                │ 1
                ▼
       ┌──────────────────────┐
       │       ENDERECO       │
       ├──────────────────────┤
       │ PK id                │
       │    logradouro        │
       │    numero            │
       │    complemento       │
       │    bairro            │
       │    municipio         │
       │    uf                │
       │    cep               │
       └──────────────────────┘
```

> Na implementação atual, cada médico possui uma graduação e um endereço. As tabelas foram separadas para representar os conceitos como objetos independentes e evitar manter estruturas compostas diretamente na tabela `medico`.

---

# 🔄 Mapeamento MongoDB → Modelo de domínio

O MongoDB continua armazenando o documento original.

Exemplo de documento:

```json
{
    "SG_UF": "MT",
    "NU_CRM": "11520",
    "NM_MEDICO": "AARÃO MOSKOWISKI PINTO DE ANDRADE",
    "COD_SITUACAO": "A",
    "DT_INSCRICAO": "27/07/2020",
    "IN_TIPO_INSCRICAO": "P",
    "TIPO_INSCRICAO": "Principal",
    "SITUACAO": "Regular",
    "ESPECIALIDADE": null,
    "PRIM_INSCRICAO_UF": "27/07/2020",
    "NM_INSTITUICAO_GRADUACAO": "UNIVERSIDADE DE CUIABA",
    "DT_GRADUACAO": "2020"
}
```

O processo de normalização transforma esses dados em objetos do domínio.

### Médico

```text
NM_MEDICO
    ↓
Medico.nome

NU_CRM
    ↓
Medico.crm

SG_UF
    ↓
Medico.uf

TIPO_INSCRICAO
    ↓
Medico.tipoInscricao

SITUACAO
    ↓
Medico.situacao

ESPECIALIDADE
    ↓
Medico.especialidade

DT_INSCRICAO
    ↓
Medico.dataInscricao

PRIM_INSCRICAO_UF
    ↓
Medico.primeiraInscricaoUf
```

### Graduação

```text
NM_INSTITUICAO_GRADUACAO
    ↓
Graduacao.instituicao

DT_GRADUACAO
    ↓
Graduacao.anoFormatura
```

### Endereço

Os campos de endereço serão preenchidos somente quando existirem na fonte:

```text
municipio
    ↓
Endereco.municipio

UF
    ↓
Endereco.uf

logradouro
    ↓
Endereco.logradouro

numero
    ↓
Endereco.numero

bairro
    ↓
Endereco.bairro

CEP
    ↓
Endereco.cep
```

Como os documentos MongoDB apresentados não possuem esses campos, eles não serão preenchidos artificialmente durante a importação.

---

# 🔄 Processo de normalização

A importação seguirá o seguinte fluxo:

```text
┌────────────────────────────┐
│ JSON original no MongoDB   │
└──────────────┬─────────────┘
               │
               ▼
      MedicoMongoDocument
               │
               ▼
       ImportacaoService
               │
       ┌───────┴────────┐
       │                │
       ▼                ▼
   Graduação         Endereço
       │                │
       └───────┬────────┘
               │
               ▼
             Medico
               │
               ▼
        ┌──────────────┐
        │    MySQL     │
        └──────────────┘
```

O serviço de importação será responsável por:

1. Ler os documentos do MongoDB.
2. Criar o objeto `Graduacao`.
3. Criar o objeto `Endereco`.
4. Criar o objeto `Medico`.
5. Associar a graduação ao médico.
6. Associar o endereço ao médico.
7. Persistir as entidades no MySQL.
8. Evitar duplicação de médicos através de `CRM + UF`.

---

# 📦 Estrutura do projeto

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
    │                       ├── MedicosApplication.java
    │                       │
    │                       ├── controller/
    │                       │   ├── MedicoController.java
    │                       │   └── ImportacaoController.java
    │                       │
    │                       ├── dto/
    │                       │   ├── MedicoRequestDTO.java
    │                       │   └── MedicoResponseDTO.java
    │                       │
    │                       ├── model/
    │                       │   ├── Medico.java
    │                       │   ├── Graduacao.java
    │                       │   └── Endereco.java
    │                       │
    │                       ├── mongo/
    │                       │   ├── MedicoMongoDocument.java
    │                       │   └── MedicoMongoRepository.java
    │                       │
    │                       ├── repository/
    │                       │   ├── MedicoRepository.java
    │                       │   ├── GraduacaoRepository.java
    │                       │   └── EnderecoRepository.java
    │                       │
    │                       ├── service/
    │                       │   ├── MedicoService.java
    │                       │   └── ImportacaoService.java
    │                       │
    │                       └── exception/
    │                           ├── GlobalExceptionHandler.java
    │                           ├── MedicoNaoEncontradoException.java
    │                           └── RegraNegocioException.java
    │
    └── resources/
        └── application.properties
```

---

# 🧩 Tecnologias

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Data MongoDB
* MySQL
* MongoDB
* XAMPP
* Maven
* REST API

---

# 🗃️ Bancos utilizados

## MongoDB

Banco destinado aos dados brutos:

```text
medicos_raw
└── medicos
    ├── documento 1
    ├── documento 2
    ├── documento 3
    └── ...
```

O MongoDB preserva o formato original da coleta.

---

## MySQL

Banco destinado aos dados normalizados:

```text
medicos
│
├── medico
│
├── graduacao
│
└── endereco
```

A API realiza suas consultas nesse banco.

---

# 🚀 Configuração

## MongoDB

Executar o MongoDB localmente e criar/utilizar o banco:

```text
medicos_raw
```

Coleção:

```text
medicos
```

Os documentos JSON brutos devem ser inseridos nessa coleção.

---

# 🛠️ XAMPP / MySQL

Iniciar o MySQL pelo XAMPP.

Depois executar:

```sql
CREATE DATABASE medicos
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Executar as tabelas na seguinte ordem:

```text
1. graduacao
2. endereco
3. medico
```

A ordem é importante porque `medico` possui chaves estrangeiras para as outras tabelas.

---

# ⚙️ Configuração da aplicação

Exemplo de `application.properties`:

```properties
spring.application.name=api-medicos

server.port=8080

spring.data.mongodb.uri=mongodb://localhost:27017/medicos_raw
spring.data.mongodb.database=medicos_raw

spring.datasource.url=jdbc:mysql://localhost:3306/medicos?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

spring.jackson.serialization.write-dates-as-timestamps=false
```

---

# 🔄 Importação MongoDB → MySQL

Após inserir os documentos no MongoDB, executar:

```http
POST /api/importacao/mongo-para-mysql
```

Exemplo de resposta:

```json
{
    "mensagem": "Importação concluída",
    "registrosProcessados": 17659
}
```

O processo:

```text
MongoDB
   │
   │ documentos brutos
   ▼
ImportacaoService
   │
   ├── Medico
   ├── Graduacao
   └── Endereco
   │
   ▼
MySQL
```

---

# 🌐 Endpoints

## Listar médicos

```http
GET /api/medicos
```

---

## Buscar médico por ID

```http
GET /api/medicos/1
```

---

## Buscar por nome

```http
GET /api/medicos?nome=AARÃO
```

---

## Buscar por CRM

```http
GET /api/medicos?crm=11520
```

---

## Buscar por UF

```http
GET /api/medicos?uf=MT
```

---

## Buscar por especialidade

```http
GET /api/medicos?especialidade=ANESTESIOLOGIA
```

---

# ➕ Cadastrar médico

```http
POST /api/medicos
Content-Type: application/json
```

Exemplo:

```json
{
    "nome": "AARÃO MOSKOWISKI PINTO DE ANDRADE",
    "crm": "11520",
    "uf": "MT",
    "especialidade": "ANESTESIOLOGIA",
    "areaAtuacao": null,
    "tipoInscricao": "Principal",
    "situacao": "Regular",
    "dataInscricao": "2020-07-27",
    "primeiraInscricaoUf": "2020-07-27",
    "telefone": null,

    "graduacao": {
        "instituicao": "UNIVERSIDADE DE CUIABA",
        "anoFormatura": 2020
    },

    "endereco": {
        "logradouro": null,
        "numero": null,
        "complemento": null,
        "bairro": null,
        "municipio": null,
        "uf": null,
        "cep": null
    }
}
```

---

# ✏️ Atualizar médico

```http
PUT /api/medicos/1
```

O corpo utiliza a mesma estrutura do cadastro.

---

# 🗑️ Excluir médico

```http
DELETE /api/medicos/1
```

---

# 🧪 Exemplos de consultas SQL

## Quantidade de médicos

```sql
SELECT COUNT(*)
FROM medico;
```

---

## Listar médicos

```sql
SELECT *
FROM medico;
```

---

## Médicos de MT

```sql
SELECT *
FROM medico
WHERE uf = 'MT';
```

---

## Médico com graduação

```sql
SELECT
    m.nome,
    m.crm,
    m.uf,
    g.instituicao,
    g.ano_formatura
FROM medico m
LEFT JOIN graduacao g
    ON g.id = m.graduacao_id;
```

---

## Médico com endereço

```sql
SELECT
    m.nome,
    m.crm,
    e.logradouro,
    e.numero,
    e.bairro,
    e.municipio,
    e.uf,
    e.cep
FROM medico m
LEFT JOIN endereco e
    ON e.id = m.endereco_id;
```

---

## Consulta completa

```sql
SELECT
    m.id,
    m.nome,
    m.crm,
    m.uf AS uf_inscricao,
    m.especialidade,
    m.tipo_inscricao,
    m.situacao,
    m.data_inscricao,

    g.instituicao AS instituicao_graduacao,
    g.ano_formatura,

    e.logradouro,
    e.numero,
    e.complemento,
    e.bairro,
    e.municipio,
    e.uf AS uf_endereco,
    e.cep

FROM medico m

LEFT JOIN graduacao g
    ON g.id = m.graduacao_id

LEFT JOIN endereco e
    ON e.id = m.endereco_id;
```

---

# 🔐 Regra de unicidade

O CRM é considerado único dentro da UF de inscrição.

Por isso:

```sql
UNIQUE (crm, uf)
```

foi definido na tabela `medico`.

Isso permite, por exemplo:

```text
CRM 12345 - MT
CRM 12345 - SP
```

como registros distintos.

---

# 📐 Critérios de modelagem

## Identificadores

Os identificadores utilizam:

```sql
BIGINT
```

correspondendo a:

```java
Long
```

---

## CRM

O CRM utiliza:

```sql
VARCHAR(20)
```

e não um tipo numérico.

Isso ocorre porque o CRM é um identificador e não um valor utilizado para cálculos.

---

## UF

A UF utiliza:

```sql
CHAR(2)
```

Exemplos:

```text
MT
PR
SP
RJ
```

---

## Datas

As datas de inscrição utilizam:

```sql
DATE
```

e no Java:

```java
LocalDate
```

---

## Ano de formação

O ano de formação utiliza:

```sql
SMALLINT
```

e no Java:

```java
Integer
```

---

## Telefone

O telefone utiliza:

```sql
VARCHAR(30)
```

pois não deve ser tratado como número para cálculo.

---

# 🧠 Normalização aplicada

A estrutura evita concentrar todas as informações em uma única tabela.

Antes:

```text
MEDICO
├── nome
├── crm
├── uf
├── instituicao_graduacao
├── ano_formatura
├── logradouro
├── numero
├── bairro
├── municipio
├── uf_endereco
└── cep
```

Depois:

```text
MEDICO
├── nome
├── crm
├── uf
├── especialidade
├── situacao
├── telefone
├── graduacao_id
└── endereco_id

GRADUACAO
├── id
├── instituicao
└── ano_formatura

ENDERECO
├── id
├── logradouro
├── numero
├── complemento
├── bairro
├── municipio
├── uf
└── cep
```

Essa organização permite que os conceitos de **médico, graduação e endereço** sejam representados separadamente, mantendo o relacionamento entre eles através das chaves estrangeiras.

---

# 📊 Estrutura final

```text
                         MONGODB
                    ┌─────────────────┐
                    │   JSON BRUTO    │
                    │                 │
                    │ NM_MEDICO       │
                    │ NU_CRM          │
                    │ SG_UF           │
                    │ DT_GRADUACAO    │
                    │ ...             │
                    └────────┬────────┘
                             │
                             │ NORMALIZAÇÃO
                             ▼
                       ┌─────────────┐
                       │   MySQL     │
                       └──────┬──────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
        ┌──────────┐   ┌────────────┐   ┌──────────┐
        │  MEDICO  │   │ GRADUACAO  │   │ ENDERECO │
        ├──────────┤   ├────────────┤   ├──────────┤
        │ id       │   │ id         │   │ id       │
        │ nome     │   │ instituicao│   │ endereço │
        │ crm      │   │ ano        │   │ município│
        │ uf       │   └────────────┘   │ uf       │
        │ ...      │                    │ cep      │
        │ grad_id  │                    └──────────┘
        │ end_id   │
        └──────────┘
              │
              ▼
        ┌──────────────┐
        │  REST API    │
        ├──────────────┤
        │ GET          │
        │ POST         │
        │ PUT          │
        │ DELETE       │
        └──────────────┘
```

---

# 🎯 Objetivo da arquitetura

A solução separa claramente as responsabilidades:

**MongoDB**

> Preservar o dado original coletado.

**Processo de normalização**

> Transformar o documento bruto em objetos estruturados.

**MySQL**

> Armazenar os dados normalizados e relacionais.

**API REST**

> Disponibilizar consultas e operações sobre os dados normalizados.

Dessa forma, o dado original não é perdido e a aplicação possui uma estrutura relacional adequada para consultas, filtros e futuras expansões.
