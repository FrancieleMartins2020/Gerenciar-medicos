# 🩺 API de Médicos - UTFPR

API REST desenvolvida para processamento, normalização, validação e consulta de dados de profissionais médicos.

O projeto utiliza uma **arquitetura híbrida envolvendo MongoDB e MySQL**: o MongoDB mantém os dados brutos provenientes do Conselho Federal de Medicina (CFM) e o MySQL armazena os dados normalizados, validados e estruturados utilizados de forma operacional pela aplicação.

---

## 📌 Visão Geral da Arquitetura

O fluxo da aplicação funciona da seguinte forma:

```text
                         DADOS BRUTOS (CFM)
                               │
                               ▼
                        ┌─────────────┐
                        │   MongoDB   │
                        │ JSON bruto  │
                        └──────┬──────┘
                               │
                               │ Sincronização /
                               │ Normalização Paginada
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

### Responsabilidade de cada Banco de Dados

* **MongoDB (medicos):** Armazena o JSON original coletado do CFM na raiz do documento. Mantém a fidelidade da fonte bruta e atua como provedor estável de dados para a etapa de limpeza e fragmentação relacional.
* **MySQL (medicos):** Armazena a estrutura dividida em tabelas relacionais (`medico`, `especialidade`, `graduacao`, `endereco`). Garante a aplicação de chaves únicas compostas (CRM + UF), índices de busca e integridade referencial executada localmente pelo XAMPP.

---

## 🏗️ Arquitetura do Domínio e Estrutura Relacional

O objeto `Medico` foi completamente normalizado seguindo os padrões do portal do CFM, mudando relacionamentos complexos para tabelas filhas. **A propriedade `telefone` foi movida da tabela principal de médicos para a tabela de endereços** para otimização cadastral.

```text
┌──────────────────────────┐
│          medico          │
├──────────────────────────┤
│ PK id                    │
│    nome                  │
│    crm                   │
│    uf                    │
│    area_atuacao          │
│    tipo_inscricao        │
│    situacao              │
│    data_inscricao        │
│    primeira_inscricao_uf │
│ FK graduacao_id          │
│ FK endereco_id           │
└───────────┬──────────────┘
            │
       ┌────┼──────────────────────┐
       │    │                      │
       ▼    ▼                      ▼
┌─────────────┐ ┌────────────────┐ ┌────────────────┐
│  graduacao  │ │    endereco    │ │ especialidade  │
├─────────────┤ ├────────────────┤ ├────────────────┤
│ PK id       │ │ PK id          │ │ PK id          │
│ instituicao │ │ logradouro     │ │ nome           │
│ ano_formatura││ numero         │ │ rqe            │
└─────────────┘ │ complemento    │ │ FK medico_id   │
                │ bairro         │ └────────────────┘
                │ municipio      │
                │ uf             │
                │ cep            │
                │ telefone       │
                └────────────────┘
```

---

## ⚡ Otimização e Paginação de Alta Performance

Devido ao grande volume de dados brutos (mais de 153 mil registros), o endpoint de listagem implementa paginação nativa de alta performance através do ecossistema do Spring Data JPA.

* **Subqueries com JOIN FETCH:** A consulta realiza filtros preliminares em sub-seleções indexadas para obter apenas os IDs necessários da página corrente, aplicando a agregação de dados carregados de forma ávida (`FETCH`) exclusivamente nos registros do lote de resposta. Isso elimina problemas de lentidão de disco e resolve o erro clássico de `LazyInitializationException`.
* **Interface Swagger Limpa:** Utiliza a anotação `@ParameterObject` e configurações de exclusão para quebrar o objeto de paginação complexo em campos numéricos amigáveis de controle de fluxo de dados (`page` e `size`), ocultando parâmetros de ordenação dinâmicos em favor de uma ordenação nativa fixa por ordem alfabética no banco de dados.

### 🗂️ Scripts de Índices para Banco de Dados (MySQL)

Para viabilizar que as buscas textuais por nome do profissional e especialidades ocorram em milissegundos, **é obrigatório rodar o script SQL abaixo** no console do seu gerenciador (ex: phpMyAdmin ou MySQL Workbench). Eles criam estruturas de indexação rápida nas colunas mais requisitadas, evitando leituras sequenciais pesadas em disco:

```sql
-- Cria índice para busca rápida pelo nome do médico na tabela relacional
CREATE INDEX idx_medico_nome ON medico (nome);

-- Cria índice para busca rápida pelo nome da especialidade médica
CREATE INDEX idx_especialidade_nome ON especialidade (nome);
```

---

## 🛡️ Regras de Negócio e Validações Implementadas

O `MedicoService` realiza validações cadastrais estritas em conformidade com as diretrizes do CFM antes de persistir dados no MySQL:
1. **Unicidade de CRM por Estado:** Impede a gravação de um mesmo número de CRM dentro da mesma Unidade Federativa (`uk_medico_crm_uf`).
2. **Consistência Temporal de Graduação:** Barra registros cujo ano de formatura informado seja superior ao ano corrente.
3. **Consistência Temporal de Inscrição:** Impede que a data de inscrição no conselho ou a data da primeira inscrição na UF estejam localizadas no futuro.
4. **Campos Obrigatórios:** Validação de preenchimento imposta para `nome`, `crm` e `uf`.
5. **Persistência em Cascata:** Propaga alterações estruturais e inclusões em lote de especialidades automaticamente através da ativação do comportamento `cascade = CascadeType.ALL`.

---

## ⚙️ Configurações de Execução (`application.properties`)

```properties
spring.application.name=api-medicos
server.port=8090

# Swagger OpenAPI Path
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.model-and-parameter-object-properties-to-ignore=sort

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/medicos
spring.data.mongodb.database=medicos

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/medicos?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

---

## 📊 Relatório de Carga e Diagnóstico de Sincronização

O processo de carga utiliza processamento paginado de **1000 em 1000** registros em nível de cursor BSON nativo, otimizando o consumo de memória RAM do servidor.

### ⏱️ Tempo de Execução da Carga Inicial
* **Endpoint:** `POST /api/importacao/sincronizar`
* **Tempo Total:** [INSIRA O TEMPO EX: 169 segundos]
* **Veredito:** Commits em tempo real diretamente no MySQL do XAMPP com persistência relacional automatizada.

> 📷 **Print do Console / Swagger do Tempo da Carga Inicial:**
> <!-- COLE O SEU PRINT DA CARGA DO SWAGGER/CONSOLE AQUI -->
> ![Tempo da Carga Inicial](CADASTRAR_LINK_DO_SEU_PRINT_AQUI)

---

### 🔎 Relatório de Auditoria e Prova Real
* **Endpoint:** `GET /api/importacao/relatorio`

O arquivo bruto original continha **153.974 registros**. O relatório demonstra o porquê dos dados refinados resultarem em um número menor no MySQL, mapeando inconsistências e duplicidades originais da fonte de dados:

> 📷 **Print do JSON de Resposta do Relatório de Auditoria:**
> <!-- COLE O SEU PRINT DO JSON COMPLETO DO RELATORIO AQUI -->
> ![JSON de Auditoria](CADASTRAR_LINK_DO_SEU_PRINT_AQUI)

---

## 🧪 Massa de Dados para Testes da API (JSONs)

Utilize os payloads estruturados abaixo para testar as rotas no Swagger UI (`http://localhost:8090/swagger-ui.html`) ou Postman:

### 1. Cadastrar Médico (`POST /api/medicos`)
```json
{
  "nome": "Aarão Salomão Cohen",
  "crm": "27499",
  "uf": "MG",
  "areaAtuacao": "Medicina Intensiva Pediátrica",
  "tipoInscricao": "Principal",
  "situacao": "Regular",
  "dataInscricao": "1994-09-08",
  "primeiraInscricaoUf": "1994-09-08",
  "especialidades": [
    {
      "nome": "MEDICINA DO TRABALHO",
      "rqe": "6845"
    },
    {
      "nome": "PEDIATRIA",
      "rqe": "1022"
    }
  ],
  "graduacao": {
    "instituicao": "ESCOLA DE MEDICINA SOUZA MARQUES",
    "anoFormatura": 1984
  },
  "endereco": {
    "logradouro": "Av. BANCO DO BRASIL",
    "numero": "100",
    "complemento": "Centro",
    "bairro": "Centro",
    "municipio": "Varginha",
    "uf": "MG",
    "cep": "37026000",
    "telefone": "35988887777"
  }
}
```

### 2. Alterar Médico (`PUT /api/medicos/{id}`)
```json
{
  "nome": "Aarão Salomão Cohen JÚNIOR",
  "crm": "27499",
  "uf": "MG",
  "areaAtuacao": "Medicina Intensiva Pediátrica ALTERADA",
  "tipoInscricao": "Principal",
  "situacao": "Regular",
  "dataInscricao": "1994-09-08",
  "primeiraInscricaoUf": "1994-09-08",
  "especialidades": [
  {
    "nome": "MEDICINA DO TRABALHO",
    "rqe": "6845"
  }
  ],
  "graduacao": {
    "instituicao": "ESCOLA DE MEDICINA SOUZA MARQUES",
    "anoFormatura": 1984
  },
  "endereco": {
    "logradouro": "Av. BANCO DO BRASIL",
    "numero": "200",
    "complemento": "Sala 10",
    "bairro": "Centro",
    "municipio": "Varginha",
    "uf": "MG",
    "cep": "37026000",
    "telefone": "35988887777"
  }
}
```
3. Teste de Validação - Disparar Erro de CRM Duplicado (POST)
Envie o payload do exemplo 1 duas vezes consecutivas para validar o disparo controlado da exceção de regra de negócio (RegraNegocioException), que impede a duplicação do CRM no mesmo estado com resposta personalizada.

## 🚀 Como Executar o Projeto

1. Certifique-se de ter o **Java 21** e o **Maven** instalados em sua máquina.
2. Inicie os servidores do **MongoDB** e do **MySQL** (utilizando o XAMPP ou instâncias nativas).
3. Clone este repositório em sua máquina local.
4. Abra o console do banco MySQL (phpMyAdmin) e execute as instruções contidas na seção **Scripts de Índices** para habilitar o alto desempenho das buscas.
5. Execute a classe principal `MedicosApplication` através da sua IDE (IntelliJ IDEA) ou utilize o terminal na raiz do projeto:
   ```bash
   mvn spring-boot:run
   ```
6. Acesse a documentação interativa e realize os testes das rotas através do link:
   👉 `http://localhost:8090/swagger-ui.html`
