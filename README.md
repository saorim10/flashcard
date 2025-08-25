# 📚 Flash Cards App

Uma aplicação web para criação, edição e gerenciamento de flashcards de estudo. Ideal para estudantes que desejam revisar conteúdos de forma prática e interativa.

---

## 🚀 Funcionalidades

- Criar novos flashcards com pergunta, resposta e categoria
- Editar e excluir flashcards existentes
- Visualizar flashcards com efeito de "flip" para mostrar a resposta
- Interface responsiva e intuitiva
- Integração completa com API REST

## Principais recursos implementados:
### 1. Entidade Usuario

- Email único como login
- Senha criptografada com BCrypt
- Roles (USER, ADMIN)
- Implementa UserDetails do Spring Security

### 2. Sistema JWT

- Geração e validação de tokens JWT
- Tempo de expiração configurável (24h)
- Filtro de autenticação automática

### 3. Endpoints de autenticação

- POST /api/auth/login - Login do usuário
- POST /api/auth/registro - Cadastro de novo usuário

### 4. Segurança dos Flashcards

- Cada flashcard agora pertence a um usuário
- Usuários só podem ver/editar seus próprios flashcards
- Validação automática de propriedade

---

## 🛠️ Tecnologias Utilizadas

### Backend (Java + Spring Boot)
- Spring Boot
- Spring Data JPA
- MySQL
- Lombok

### Frontend (Angular)
- Angular 17
- Bootstrap 5
- Font Awesome
- RxJS

---

## 📦 Instalação

### 🔧 Backend

1. Clone o repositório:
   ```bash
   git clone https://github.com/saorim10/Flashcard-v1.git
   cd Flashcard-v1
- Configure o banco de dados MySQL:
- Crie um banco chamado flash-cards
- Usuário: root
- Senha: root
- Execute a aplicação Spring Boot:
- Porta padrão: 8080

## 💻 Frontend
Crie o projeto Angular:

```bash
ng new flash-cards-app
cd flash-cards-app
```
Instale as dependências:

```bash
npm install
Substitua os arquivos conforme a estrutura abaixo:
````

```Código
src/app/
├── models/
│   └── flashcard.model.ts
├── services/
│   └── flashcard.service.ts
├── components/
│   └── flashcard-list/
│       └── flashcard-list.component.ts
├── app.component.ts
└── app.module.ts
```
Execute o projeto Angular:

```bash
ng serve
```
Acesse no navegador:

- Frontend: http://localhost:4200
- Backend: http://localhost:8080

## 📁 Estrutura do Projeto
```text
flash-cards-app/
├── src/
│   ├── app/
│   │   ├── models/
│   │   ├── services/
│   │   ├── components/
│   │   ├── app.component.ts
│   │   └── app.module.ts
│   └── index.html
├── package.json
└── angular.json
```
## Diagramas do projeto completo

### Diagrama de Casos de Uso

![Diagrama de Casos de Uso](diagrama%20de%20casos%20de%20uso.png)

### Diagrama de Classes

![Diagrama de classe](diagrama%20de%20classe.png)

### Diagrama de Sequência - Login

![Diagrama de Sequência](diagrama%20de%20sequência%20-%20login.png)

### Diagrama de Sequência - Registro

![Diagrama de Sequência - Registro](diagrama%20de%20sequência%20-%20registro.png)

### Diagrama de Sequência - CRUD

![Diagrama de Sequência - CRUD](diagrama%20de%20sequência%20-%20crud.png)

### Diagrama de Arquitetura

![Diagrama de Arquitetura](diagrama%20de%20arquitetura.png)

## 📌 Observações
Certifique-se de que o backend esteja rodando antes de iniciar o frontend.

A aplicação utiliza CORS liberado para facilitar o desenvolvimento local.

## 🧠 Autor
Desenvolvido por Marcelo (@saorim10) com 💙 e foco em produtividade nos estudos.

## 📄 Licença
Este é um projeto acadêmico e está sob a licença MIT.
