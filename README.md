# 📦 API de Pedidos - LuizaLabs
API RESTful desenvolvida em **Spring Boot 3.4.5** para processamento e gerenciamento de pedidos, com importação de arquivos, filtros dinâmicos e exportação de dados.

***

## 🔖 Descrição
Este projeto consiste em uma API para gerenciar pedidos de usuários, permitindo:
 - Upload de arquivos para **importação em lote** de pedidos.
 - Consulta com múltiplos filtros (por **ID**, **datas**, **usuário**, **valores**).
 - Exportação dos pedidos filtrados em arquivo **JSON**.
 - Persistência em **banco de dados** em memória H2.
 - Documentação automática via **OpenAPI (Swagger)**.
 - **Automatização de build e execução via script Docker**, facilitando a implantação em ambientes isolados e reprodutíveis.

***

## 💡 Tecnologias Utilizadas
 - Java 17
 - Spring Boot 3.4.5
 - Spring Data JPA
 - Spring Boot Validation
 - Springdoc OpenAPI (Swagger)
 - Banco de dados H2 (em memória)
 - Lombok
 - Maven

***
## 🧠 Funcionamento Detalhado
### Upload de Arquivo
- Recebe um arquivo de texto no formato fixo onde cada linha representa um item de pedido.
- Cada linha tem campos de tamanho fixo para userId, userName, orderId, productId, productValue e purchaseDate.
- Converte as linhas em objetos Order, salva no banco e retorna a lista agrupada por usuário e pedidos.

### Consulta com Filtros
- Permite filtrar pedidos por:
  - orderId (Long)
  - intervalo de datas (startDate e endDate no formato yyyy-MM-dd)
  - username (contém, case insensitive)
  - valores mínimos e máximos (minValue, maxValue como BigDecimal)
- Retorna uma estrutura JSON agrupada:
  - Usuário → Pedidos → Produtos

### Download JSON
- Gera um arquivo JSON temporário contendo todos os pedidos agrupados.
- Retorna arquivo para download com cabeçalhos adequados.

***
## 🛠️ Configuração do projeto
O projeto utiliza banco H2 em memória configurado para permanecer ativo durante a execução:

```
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
    username: sa
    password: password
    driverClassName: org.h2.Driver

  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true

  h2:
    console:
      enabled: true
      path: /h2-console
      settings:
        web-allow-others: true
```
***
## 🧩 Estrutura do Projeto
### Model
- **Order**: Entidade principal que representa um item de pedido individual, com atributos como ```userId```, ```userName```, ```orderId```, ```productId```, ```productValue``` e ```purchaseDate```.

### Repository
- **OrderRepository**: Interface que estende ```JpaRepository<Order, Long>``` para operações CRUD no banco H2.

### Service
- **OrderServiceImpl**: Implementação do serviço para processar upload de arquivos, filtrar pedidos e agrupar dados por usuário e pedido.
- Conversão do arquivo texto para entidade Order.
- Métodos para filtrar pedidos com base em parâmetros opcionais.
- Agrupamento dos pedidos para retorno de estrutura hierárquica por usuário e pedidos.

### Controller
- **OrderController**: Endpoints REST para upload de arquivo, consulta com filtros e download dos pedidos no formato JSON.

### Configuração Swagger
- Classe **SwaggerConfig** que configura a documentação OpenAPI para a API, acessível via UI.

***

## 💻 Endpoints da API

| Método | Endpoint               | Descrição                                | Parâmetros / Corpo                                                    | Resposta                            |
| ------ | ---------------------- | ---------------------------------------- | --------------------------------------------------------------------- | ----------------------------------- |
| POST   | `/api/orders/upload`   | Upload de arquivo de pedidos (form-data) | `file` (MultipartFile)                                                | Lista de usuários com pedidos       |
| GET    | `/api/orders`          | Consulta pedidos com filtros opcionais   | `orderId`, `startDate`, `endDate`, `username`, `minValue`, `maxValue` | Lista filtrada de pedidos agrupados |
| GET    | `/api/orders/download` | Download dos pedidos no formato JSON     | Nenhum                                                                | Arquivo JSON para download          |

***

## ⚙️ Como Executar
1. Clone o repositório:
   ```
   git clone git@github.com:gustavogalli/luizalabs.git
   cd luizalabs
   ```
2. Compile e rode a aplicação usando Maven:
   ```
   ./mvnw clean spring-boot:run
   ```
3. Acesse:
   - **H2 Console**: ```http://localhost:8080/h2-console``` (JDBC URL: ```jdbc:h2:mem:testdb```)
   - **Swagger UI**: ```http://localhost:8080/swagger-ui.html``` (documentação da API)

### 🐳 Build Automatizado com Docker
Você pode também utilizar o Docker para automatizar o build da aplicação.
### **📜 Executar o Script de Build**
1. Dê permissão de execução (apenas uma vez):
```chmod +x build.sh```
2. Execute o script:
```./build.sh```

**O que o script faz:**
- Remove imagens Docker antigas (se existirem).
- Constrói uma nova imagem Docker com a tag latest.
> 💡 Dica: personalize o nome da imagem e o container diretamente no build.sh.

***

## 📄 Swagger / Documentação
A documentação automática da API está disponível pelo springdoc-openapi, com interface UI em:

```
http://localhost:8080/swagger-ui.html
```

***

## 💻 Considerações Finais
- O projeto está configurado para banco H2 em memória, ideal para testes e desenvolvimento.
- Para produção, configure outro banco de dados (MySQL, Postgres, etc.).
- Utiliza validação, logging (via Lombok e SLF4J) e configuração limpa para fácil manutenção.
- O agrupamento e processamento de pedidos podem ser adaptados conforme regras de negócio futuras.
