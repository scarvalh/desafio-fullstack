
# Fullstack Challenge

Este projeto foi desenvolvido utilizando **Angular** no frontend e **Java Spring Boot** no backend, com um banco de dados **MySQL**.

## Tecnologias Utilizadas

- **Frontend:** Angular
- **Backend:** Java Spring Boot
- **Banco de Dados:** MySQL
- **API de CEP:** [ViaCEP - IBGE](https://viacep.com.br/ws/)
- **Docker** para a conteinerização

## Estrutura dos Dados

### Tabelas do Banco de Dados

#### **Empresa**

| Campo | Tipo              |
| ----- | ----------------- |
| id    | INT (Primary Key) |
| nome  | VARCHAR           |
| CNPJ  | VARCHAR           |
| cep   | VARCHAR           |

#### **Pessoa Física**

| Campo              | Tipo              |
| ------------------ | ----------------- |
| id                 | INT (Primary Key) |
| nome               | VARCHAR           |
| cpf                | VARCHAR           |
| data de nascimento | DATE              |
| rg                 | VARCHAR           |

#### **Pessoa Jurídica**

| Campo | Tipo              |
| ----- | ----------------- |
| id    | INT (Primary Key) |
| nome  | VARCHAR           |
| CNPJ  | VARCHAR           |

#### **Fornecedor**

| Campo                | Tipo                                |
| -------------------- | ----------------------------------- |
| id                   | INT (Primary Key)                   |
| email                | VARCHAR                             |
| cep                  | VARCHAR                             |
| tipo                 | ENUM ('Física', 'Jurídica')         |
| id_pessoa_física   | INT (Foreign Key - Pessoa Física)   |
| id_pessoa_jurídica | INT (Foreign Key - Pessoa Jurídica) |

#### **Empresa Fornecedor** (Relacionamento N:N)

| Campo          | Tipo                           |
| -------------- | ------------------------------ |
| id             | INT (Primary Key)              |
| id_empresa    | INT (Foreign Key - Empresa)    |
| id_fornecedor | INT (Foreign Key - Fornecedor) |

## Regras de Negócio

- Um **fornecedor** pode ser **pessoa física** ou **pessoa jurídica**.
- A identificação do tipo de fornecedor ocorre pelo campo **tipo** e pelas chaves estrangeiras associadas.
- Um **fornecedor** possui **apenas um dos campos preenchidos** (id_pessoa_física ou id_pessoa_jurídica).
- Uma **empresa** pode ter **vários fornecedores**.
- Um **fornecedor** pode estar ligado a **várias empresas**.
- O relacionamento entre **empresas e fornecedores** é gerenciado pela **tabela empresa_fornecedor**.

### Operações Disponíveis

#### **Empresa**

- Criar uma empresa exibindo uma lista de fornecedores disponíveis.
- Editar uma empresa em qualquer campo.
- Deletar uma empresa. Os fornecedores ligados a ela perdem a relação, pois a entrada correspondente na **empresa_fornecedor** é removida.

#### **Fornecedor**

- Criar um fornecedor escolhendo o tipo e preenchendo os dados correspondentes.
- Ao criar um fornecedor, a **Pessoa Física** ou **Pessoa Jurídica** correspondente também é criada, se ainda não existir (validado pelo CPF/CNPJ).
- Editar um fornecedor a qualquer momento.
- Um fornecedor **somente pode ser deletado se não estiver relacionado a nenhuma empresa**.

## Comandos Docker

### Construir e Executar o Frontend (Angular)

```sh
# Construir a imagem
docker build -t fullstack-challenge-front .

# Executar o container
docker run -p 80:80 fullstack-challenge-front
```
### Construir e Executar o Backend (Spring Boot)
```sh
# Construir a imagem
docker build -t fullstack-challenge .

# Executar o container
docker run -p 8080:8080 fullstack-challenge
```


