# Acervo Inclusivo 📚

Este projeto é uma API desenvolvida em Java cujo objetivo é prover funcionalidades para um sistema de uma biblioteca virtual de materiais acessíveis. Ele foi construído para ser escalável, fácil de utilizar e de manter.

## Acesse o sistema web 🚀
Você pode acessar o sistema web desenvolvido através do seguinte link: [Acervo Inclusivo](https://bibliotech-indol.vercel.app/)

## Tecnologias Utilizadas 🛠️
- **Java 24**: Linguagem de programação utilizada para o desenvolvimento da API.
- **Maven 3.6.3**: Gerenciador de dependências utilizado para facilitar o gerenciamento do projeto.
- **MySQL**: Sistema de gerenciamento de banco de dados utilizado para armazenar os dados da aplicação.
- **Docker**: Utilizado para criar um ambiente isolado e consistente para o banco de dados MySQL.
- **Next.js**: Framework utilizado para o desenvolvimento do front-end, proporcionando uma experiência de usuário dinâmica e responsiva.

## Requisitos ✅

Certifique-se de ter os seguintes requisitos instalados para executar o projeto:
- **Java SDK 24** ou superior para compilar e executar o código
- **Maven 3.6.3** ou superior para gerenciamento de dependências


#### Configuração

Para executar o projeto corretamente, é necessário configurar as credenciais do Firebase e do banco de dados.

Crie um arquivo `.env` na raiz do projeto, utilizando o exemplo abaixo como referência:

```dotenv
# Firebase
FIREBASE_TYPE=service_account
FIREBASE_PROJECT_ID=
FIREBASE_PRIVATE_KEY_ID=
FIREBASE_PRIVATE_KEY=
FIREBASE_CLIENT_EMAIL=
FIREBASE_CLIENT_ID=
FIREBASE_AUTH_URI=https://accounts.google.com/o/oauth2/auth
FIREBASE_TOKEN_URI=https://oauth2.googleapis.com/token
FIREBASE_AUTH_PROVIDER_CERT_URL=https://www.googleapis.com/oauth2/v1/certs
FIREBASE_CLIENT_CERT_URL=
FIREBASE_UNIVERSE_DOMAIN=googleapis.com
FIREBASE_STORAGE_BUCKET=

# Database
DB_USER=
DB_PASSWORD=
```

Preencha as variáveis com as credenciais correspondentes ao seu projeto Firebase e ao banco de dados utilizado pela aplicação.

> ⚠️ **Importante:** nunca compartilhe ou versione o arquivo `.env`, pois ele contém informações sensíveis. O arquivo `.env` deve estar incluído no `.gitignore`.

O Firebase é utilizado pelo projeto para realizar o armazenamento de arquivos.

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

3. Execute o arquivo principal(Server.java):
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

