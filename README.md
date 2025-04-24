# api-bibliotec 📚

Este projeto é uma API desenvolvida em Java cujo objetivo é prover funcionalidades para um sistema de uma biblioteca virtual de materiais acessíveis. Ele foi construído para ser escalável, fácil de utilizar e de manter.


## Requisitos ✅

Certifique-se de ter os seguintes requisitos instalados para executar o projeto:
- **Java SDK 24** ou superior para compilar e executar o código
- **Maven 3.6.3** ou superior para gerenciamento de dependências
- **Docker-compose** para executar o banco de dados MySQL


# Estrutura de Pastas 📦

Abaixo está a estrutura de organização do projeto:

```
api-bibliotec
├── src
│   ├── controller/     -> Controladores REST
│   ├── dao/        -> Acesso e  manipulação do banco de dados
│   ├── dto/        -> Objetos de Transferência de Dados
│   ├── exception/      -> Exceções personalizadas
│   ├── model/      -> Modelos de dados
│   ├── service/        -> Lógica de negócios
│   ├── type/       -> Tipos de dados
│   └── Server.java     -> Classe principal do projeto
├── pom.xml     -> Arquivo de configuração do Maven
├── docker-compose.yml      -> Arquivo de configuração do Docker
└── README.md     -> Documentação do projeto
```


## Como Executar o Projeto 🚀
Siga as etapas abaixo para executar o projeto localmente:

1. Clone o repositório:
    ```bash
    git clone git@github.com:emerss001/api-bibliotech.git
    cd api-bibliotech
    ```

2. Configure as dependências do projeto:
   ```bash
   mvn clean install
   ```

3. Inicie o banco de dados MySQL usando Docker:
   ```bash
    docker-compose up -d
    ```
4. Execute o arquivo principal(Server.java):
- se estiver usando **IntelliJ IDEA**:  
  clique com o botão direito em ```Server.java``` e escolha Run ```Server.main()```.


- Se estiver usando **Eclipse**:  
  clique com o botão direito em ```Server.java``` e escolha ```Run As > Java Application```.


- Se estiver usando **Terminal**:
    ```bash
    mvn clean compile exec:java
    ```


4. O aplicativo estará disponível no endereço:
   ```
   http://localhost:8888
   ```


## Documentação da API 📖
A seguir estão os principais endpoints da API:

#### Criar um novo usuário

```
  POST /pessoas
```
##### Dados esperados:
```json
{
  "nome": "Emerson Neves",
  "email": "fdfdf@gmail.com",
  "vinculo": "PROFESSOR", // (ou "ALUNO")
  "matricula": "202501GT078", // (Quando for aluno)
  "siap": "123456789", // (Quando for professor)
  "senha": "minhasenha",
  "idNecessidade": 1 // (quando for aluno)
}
```

##### Possíveis respostas:
```json
// 201 - created
{
    "id": 3

}

// 400 - bad request
{
    "error": "Nome deve ter pelo menos 3 caracteres"
}

// 500 - internal server error
{
    "error": "Erro interno no servidor"
}
```