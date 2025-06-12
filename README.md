# Acervo Inclusivo 📚

Este projeto é uma API desenvolvida em Java cujo objetivo é prover funcionalidades para um sistema de uma biblioteca virtual de materiais acessíveis. Ele foi construído para ser escalável, fácil de utilizar e de manter.


## Requisitos ✅

Certifique-se de ter os seguintes requisitos instalados para executar o projeto:
- **Java SDK 24** ou superior para compilar e executar o código
- **Maven 3.6.3** ou superior para gerenciamento de dependências
- **Docker-compose** para executar o banco de dados MySQL

- #### IMPORTANTE
    Para configurar o banco é necessário criar primeiro:
1. Criar um banco de dados chamado `acervoinclusivo` no MySQL.
2. alterar o arquivo `src/db/ConnectionDB.java` alterando o valor da variável `user` e `password` para o usuário e senha do seu banco de dados MySQL.
3. Rodar o script `backup.sql` no seu banco de dados MySQL para criar as tabelas necessárias.

# Estrutura de Pastas 📦

Abaixo está a estrutura de organização do projeto:

```
api-bibliotec
├── src
│   ├── controller/     -> Controladores REST
│   ├── dao/        -> Acesso e  manipulação do banco de dados
│   ├── db/        -> Configuração do banco de dados
│   ├── dto/        -> Objetos de Transferência de Dados
│   ├── exception/      -> Exceções personalizadas
│   ├── entity/      -> Modelos de dados
│   ├── service/        -> Lógica de negócios
│   ├── type/       -> Tipos de dados
│   ├── util/       -> Utilitários
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
    Utilize aplicativos para testes como Postam ou Insomnia, ou até mesmo o navegador, para interagir com a API.

## Conectando com o front-end (opcional) 🌐
Siga as etapas abaixo para executar o projeto localmente:

Acesse o repositório do frontend: https://github.com/emerss001/bibliotech

1. Clone o repositório:
    ```bash
    git clone https://github.com/emerss001/bibliotech.git
    cd bibliotech
    ```
2. Instale as dependências do projeto:
   ```bash
   npm install
   ```
3. Inicie o servidor:
   ```bash
    npm run dev
    ```
4. Acesse o aplicativo no navegador:
   ```
   http://localhost:3000
   ```



## Documentação da API 📖
A seguir estão os principais endpoints da API:

#### Criar um novo usuário

```
  POST /cadastro
```
##### Dados esperados:
```json lines
{
  "nome": "Emerson Neves",
  "email": "fdfdf@gmail.com",
  "senha": "minhasenha",
  "vinculo": "PROFESSOR", // (ou "ALUNO" ou "BIBLIOTECARIO")
  "matricula": "202501GT078", // (Quando for aluno)
  "siap": "123456789", // (Quando for professor)
  "codigo": "852", // (Quando for bibliotecário)
  "idNecessidade": 1 // (quando for aluno)
}
```

#### Fazer login

```
  POST /login
```
##### Dados esperados:
```json lines
{
  "vinculo": "BIBLIOTECARIO", // (vinculo do usuário)
  "email": "fdfdf@gmail.com",
  "senha": "minhasenha",
}
```

#### Listar materiais 

```
  GET /protegida/materials
```
- Toda rota que possuir o prefixo `/protegida` requer autenticação. Nesse caso, token gerado no login deve ser passado no campo `Authorization` do cabeçalho da requisição.

## Autores 👨‍💻
- [@emerss001](https://github.com/emerss001)
- [@Teless0](https://github.com/Teless0)
- [@Denilson-S](https://github.com/Denilson-S)
- [@andr6z](https://github.com/andr6z)    

