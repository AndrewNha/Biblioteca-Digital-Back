# 1 . Introdução
<h3>Projeto Sistema de Biblioteca Digital</h3>

<p>O presente projeto consiste no desenvolvimento de um sistema de biblioteca digital denominado “ReadMaxxing”, cujo objetivo é gerenciar o acervo de livros, autores, usuários, empréstimos e reservas de uma biblioteca.</p>
<p>O problema central abordado é a necessidade de controlar de forma organizada e segura o fluxo de empréstimos e reservas de livros, garantindo que regras de negócio, como o limite de três empréstimos ativos por usuário e a impossibilidade de reservar um livro com cópias disponíveis, sejam respeitadas. Ademais, o objetivo central do projeto é o controle de integridade entre as entidades do sistema, isto é, garantir que os relacionamentos estejam válidos e sem contradições.</p>
<p>O sistema foi desenvolvido com uma arquitetura cliente-servidor, onde o back-end é responsável por toda a lógica de negócio e persistência de dados, enquanto o front-end oferece uma interface visual para interação com o sistema. O sistema implementa as operações de CRUD (criação, leitura, atualização e exclusão de dados), além disso, a comunicação entre as duas camadas ocorre através de requisições HTTP (<i>GET</i>, <i>POST</i>, <i>PUT</i>, <i>DELETE</i>), seguindo os princípios REST (<i>Representational State Transfer</i>), estes muito utilizados para o desenvolvimento de APIs web, pois organiza a comunicação entre cliente e servidor através de <i>endpoints</i> .</p>

# 2 . Modelagem do Problem

<h3>Diagrama UML</h3>

<img width="901" height="910" alt="NovidadeEmBreve drawio(1)" src="https://github.com/user-attachments/assets/6e5849ba-df4f-4a2f-a2ad-e8750f0a143b" />

<h3>Descrição do modelo</h3>

<p>O sistema foi modelado utilizando os princípios da Orientação a Objetos, com as seguintes classes principais:</p>
<p>`Person` — classe abstrata que serve como base para Author e User, aplicando o conceito de herança. Ambas as subclasses herdam os atributos id e name, e implementam o método abstrato `getInfo()`, que, supreendemente, pode ser visualizado no momento de uma requisição `POST`, aplicando o conceito de polimorfismo. </p>
<p>`Author` — representa um autor do acervo, com atributo `nationality` e uma lista de livros escritos. Possui uma associação ManyToMany bidirecional com Book. Entretanto, no sistema, ele deve ser criado antes de Book, pois um Book precisa ter um autor que o escreveu.</p> 
<p>`User` — representa um usuário da biblioteca, com atributos `email` e `telephoneNumber`. Pode ter múltiplos empréstimos e reservas associados. </p>
<p>`Book` — representa um livro do acervo, com atributos de `name`, `gender`, `publisher`, `releaseDate`, `quantity` e `quantityAvailable`, além de possuir uma lista de autores. Todo livro pode ter sido escrito por mais de um autor. </p>
<p>`Loan` — representa um empréstimo, associando um User a um Book com um status (`ACTIVE`, `RETURNED`, `LATE`) e datas de empréstimo e devolução. </p>
<p>`Reservation` — representa uma reserva, associando um User a um Book com um status (`PENDING`, `COMPLETED`, `CANCELLED`) e data de reserva.</p>



<h3>Estrutura atual do projeto</h3>

```
.
├── src/
│   ├── main/
│   │   ├── java/turminha/BibliotecaDigital/
│   │   │   ├── controller/
│   │   │   │   ├── AuthorController.java
│   │   │   │   ├── BookController.java
│   │   │   │   ├── LoanController.java
│   │   │   │   ├── ReservationController.java
│   │   │   │   └── UserController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthorService.java
│   │   │   │   ├── BookService.java
│   │   │   │   ├── LoanService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   └── UserService.java
│   │   │   ├── repository/
│   │   │   │   ├── AuthorRepository.java
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── LoanRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── model/
│   │   │   │   ├── Author.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── Loan.java
│   │   │   │   ├── Person.java
│   │   │   │   ├── Reservation.java
│   │   │   │   └── User.java
│   │   │   ├── enums/
│   │   │   │   ├── LoanStatus.java
│   │   │   │   └── ReservationStatus.java
│   │   │   └── BibliotecaDigitalApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/turminha/BibliotecaDigital/
│           └── BibliotecaDigitalApplicationTests.java
└── pom.xml
```
# Diagramação UML
<img width="901" height="910" alt="NovidadeEmBreve drawio(1)" src="https://github.com/user-attachments/assets/6e5849ba-df4f-4a2f-a2ad-e8750f0a143b" />

# TODO

- Front-end maroto
- Relatório
