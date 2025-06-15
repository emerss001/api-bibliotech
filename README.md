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

#### IMPORTANTE
Para o funcionamento correto do projeto, é necessário adicionar as credenciais do Firebase. Para isso, crie na raiz projeto um arquivo `.env` com as seguintes variáveis de ambiente:

```dotenv
   FIREBASE_TYPE=service_account
   FIREBASE_PROJECT_ID=api-bibliotech
   FIREBASE_PRIVATE_KEY_ID=c7947f8ec182fcf8c7aea7381b7e2f10ea50ff4e
   FIREBASE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCdLqaZN7k78FsX\nkYwfCQRRs3rBMzBqMXMCSgfiR6UilTcQbNqkhWfPIf9Kcvrkkuy78zZDBYy0ASqf\nPgDd0fMdpztVuOAgfdMnz+MCRjpavVwe4lZZQIbTDl911KzXT40Yvu91Y+GGX+1l\n0eLtfRpgktyqAGcbK+KMrCjzeqmCN1Z54pw8djptCK1/q3zCvoaabh7ry76xegUH\nFdwdrxJMvJHiqdCrM20jakzXUYlqt/rZEMkjk4LtbszAN5pJA3zIGCAxM0b6UHoU\nfsQ+3Vj+j1Bfxuc4LP15sX3ek+OD3h84St3wmnc836p2WbpczDut225HWQmq1Tkm\nAVfMqNbtAgMBAAECggEAA0UDZ6gCVSYaivq21rVuDtIEMW0iam8VZJyMHe+eFoJS\nlyegg7AXuPYM1KxgHi2VOZKlVA8TPnJQw+e7BmI1463FHPMfzGU5pXgcUYGK+LS8\nLBNKtwgR1eWUZEPUhZUMyxE7s24stIodytKrHQQPbqdXajzgaC8JQyJYprnsOT5r\nlqr4l2Spi4Enp0FVslUhjphPTgsYdDo+H6Q1wCncnNv6QCRxJXi0bAIrWvzv4aFo\n1WnEasmJ7/0xnitgzIj8aT6Ol1+ad5Z/B4YvJo8PE9iSG1hMqxB5pdXvsBfrBFi+\nYZWRPNG+86ZAG++I069/S/P42v4YMQYtPmz9qcSZeQKBgQDQ2/4VOvjnP24h+KC+\naA5CCCFgOEPimeDZhWHrqnZM/eb9DVrAbgVsFZM17rgwA1FsQPUJqZ8pfumD7TPp\nuhJB2e45DcH8vScxr0FqH++8Ms2srKf0neK6d9ckHuTxwABlMpI3u20hY/G0VNiN\n6/brGSscMGZLtUIVAryFuX61eQKBgQDAqLnl6ROc2mBxQRf913VG1FTutVVYfK83\nQ9vFMGVFbkWHSkOB3bfcBSNPNwnzqGn9qAEdN/SuxoVbuPW0nLDsZG2he5Je0ARo\nXL/IrnOemQa08ef4fyDEnY8jPPbMiJ4SOG7t9qWz2qzwUk1XlyC24woGUwyfO+Dt\nqRHR4qqUFQKBgBPWoZbAqD3G354ocJRFa/1HqmvqTEBs31ep/LgW0+/SOiuVJiab\nDLbMgdQgnawp1IUU0nGdg/m8DVAzqrerrepFWgRfUyq/iMaGYA9Fg078AF9Dcxyg\nFpYhpfTPXm28EA2MtSjIC8CdLqszV/J7FFQWaurdigns6J74SJHnIar5AoGACpd8\nGOK2fFIZKUDN49u7i9hSjwkTFxlLvLsTUwT1HFsSoXx4t6QL3qG9rjY1atrgcNyS\nqxuPbShm3oMNpw9SPrzKti0IAARpqZ8nwA2vN5HsJ3iBK0057PVIkERiwR3lqtTF\nbHm55GhqR5AOxnj9iHB09aINOJffJtG7tBFFFX0CgYB2cKIPJ/6L9tH/fYiDEuQg\nh6pRjLccBXstJXxIjDVB317pbfg/RvVgUku522tqItU+CbwIgz7PNUnNhzyU/zKk\ny3KF+4yF0BoNyHCrFn2EQcPbAaxLDsNOidLbyLWQyEuw1FtVNqPmp/M3nPNVyW1A\npY/SXdOHJOWBxu1Y36bE0w==\n-----END PRIVATE KEY-----"
   FIREBASE_CLIENT_EMAIL=firebase-adminsdk-fbsvc@api-bibliotech.iam.gserviceaccount.com
   FIREBASE_CLIENT_ID=102964200623220324609
   FIREBASE_AUTH_URI=https://accounts.google.com/o/oauth2/auth
   FIREBASE_TOKEN_URI=https://oauth2.googleapis.com/token
   FIREBASE_AUTH_PROVIDER_CERT_URL=https://www.googleapis.com/oauth2/v1/certs
   FIREBASE_CLIENT_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40api-bibliotech.iam.gserviceaccount.com
   FIREBASE_UNIVERSE_DOMAIN=googleapis.com
   FIREBASE_STORAGE_BUCKET=api-bibliotech.firebasestorage.app
```

Copie e cole as credenciais no arquivo `.env` criado na raiz do projeto. Essas credenciais são necessárias para a integração com o Firebase, utilizado para o armazenamento de arquivos.
#### IMPORTANTE
É necessário também adicionar ao `.env` as variáveis do banco de dados, aidione também este código ao arquivo `.env`:

```dotenv
   DB_USER=avnadmin
   DB_PASSWORD=AVNS_IE6kQ6tSruZplEe08q5
```

## Usuários de Teste 👤
Para facilitar os testes, foram criados alguns usuários de exemplo. Você pode utilizar as seguintes credenciais:

- Vinculo: Aluno
- matricula: 2024005
- senha: minhasenha


- Vinculo: Bibliotecário
- SIAP: 852
- senha: minhasenha

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

