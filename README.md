# Company (front)

## Descrição

Esse projeto repositório contém o projeto da interface e do servidor.
- Para acessar o código do servidor basta acessar [Server](https://github.com/ThatsJojo/MI-Redes-3/tree/main/company);
- Para acessar o código da interface, acesse [Front](https://github.com/ThatsJojo/MI-Redes-3/tree/main/front);

## Requisitos

- Node >=12.22.1
- NPM >=6.14.12 
- Apache Maven 3.8.2
- Apache Netbeans 12.4 
- Java version: 1.8.0_292

## 🖥️  Instalar dependências

```bash
cd front
npm install -g @quasar/cli
npm install
```

### Iniciando o app

```bash
quasar dev
```

### Definir Companhia

Para definir qual companhia a interface representará, basta acessar o arquivo [Config](https://github.com/ThatsJojo/MI-Redes-3/blob/main/front/quasar.conf.js).
Haverá os atributos "NAME, ID e BASE_URL". Alterando esses valores o front-end já consegue alterar o servidor.

Servidores disponíveis:

```bash
NAME: Azul, ID: 0, BASE_URL: https://azul-company.herokuapp.com/ 
NAME: Gol, ID: 1, BASE_URL: https://gol-company.herokuapp.com/ 
NAME: Tam, ID: 2, BASE_URL: https://tam-company.herokuapp.com/ 

```

## Funcionalidades

## Buscar por caminhos

Define a cidade de origem e a de destino. O servidor buscará os caminhos possíveis e apresentará as opções para o usuário.

![image](https://user-images.githubusercontent.com/32804625/146361080-7b16603a-b68c-4258-b264-7b1a22f52fab.png)


## Realizar compra

O usuário define o caminho que deseja e seleciona as passagens. Ao receber a requisição o servidor verificará se há passagens e se as outras companhias permitem a operação
![image](https://user-images.githubusercontent.com/32804625/146361454-91aaa8d3-f317-45b2-a0f0-1d0d463b294a.png)

## Criar novos voos
Cada companhia pode criar novos voos.
![image](https://user-images.githubusercontent.com/32804625/146361909-318a64b1-359f-43ce-ab6a-28279109ade9.png)

### Realizando build para produção

```bash
quasar build
```

### 🔗 Tecnologias utilizadas: ### 

- [Quasar](https://quasar.dev);
- JavaScript;

### Serviço de hospedagem ###

- [Heroku](https://www.heroku.com/)
