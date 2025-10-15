# App To Do List

## Laboratório nº2 Engenharia de Software

Trabalho realizado por:

Carolina Torres - LEI-122718

Matilde Marcelino - LEI-122695

Ruama Felix - LEI-122662

## Pipeline (D6)

Este projeto inclui uma pipeline configurada com GitHub Actions, que automatiza o processo de build da aplicação Java.

A pipeline realiza as seguintes ações:

- Executa automaticamente sempre que é feito um push para a branch principal (main);

- Configura o ambiente Java (versão 21);

- Compila o projeto utilizando o Maven com o comando mvn clean package;

- Publica o ficheiro .jar gerado como artefacto do workflow, disponível para download na secção Actions do GitHub.

## Link para o video de apresentação das funcionalidades:
https://youtu.be/h9_fxXl3wnQ

Excerto do ficheiro build.yml :

```
name: Build Java Project

on:
  push:
    branches: [ "main" ]  # Executa em push na branch principal

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout do código
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '21'

      - name: Compilar projeto com Maven
        run: mvn -B clean package

      - name: Publicar artefacto .jar
        uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: target/*.jar
```
## Project Structure

The sources of your App have the following structure:

```
src
├── main/frontend
│   └── themes
│       └── default
│           ├── styles.css
│           └── theme.json
├── main/java
│   └── [application package]
│       ├── base
│       │   └── ui
│       │       ├── component
│       │       │   └── ViewToolbar.java
│       │       ├── MainErrorHandler.java
│       │       └── MainLayout.java
│       ├── examplefeature
│       │   ├── ui
│       │   │   └── TaskListView.java
│       │   ├── Task.java
│       │   ├── TaskRepository.java
│       │   └── TaskService.java                
│       └── Application.java       
└── test/java
    └── [application package]
        └── examplefeature
           └── TaskServiceTest.java                 
```

The main entry point into the application is `Application.java`. This class contains the `main()` method that start up 
the Spring Boot application.

The skeleton follows a *feature-based package structure*, organizing code by *functional units* rather than traditional 
architectural layers. It includes two feature packages: `base` and `examplefeature`.

* The `base` package contains classes meant for reuse across different features, either through composition or 
  inheritance. You can use them as-is, tweak them to your needs, or remove them.
* The `examplefeature` package is an example feature package that demonstrates the structure. It represents a 
  *self-contained unit of functionality*, including UI components, business logic, data access, and an integration test.
  Once you create your own features, *you'll remove this package*.

The `src/main/frontend` directory contains an empty theme called `default`, based on the Lumo theme. It is activated in
the `Application` class, using the `@Theme` annotation.

## Starting in Development Mode

To start the application in development mode, import it into your IDE and run the `Application` class. 
You can also start the application from the command line by running: 

```bash
./mvnw
```

## Building for Production

To build the application in production mode, run:

```bash
./mvnw -Pproduction package
```

To build a Docker image, run:

```bash
docker build -t my-application:latest .
```

If you use commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey .
```

## Getting Started

The [Getting Started](https://vaadin.com/docs/latest/getting-started) guide will quickly familiarize you with your new
App implementation. You'll learn how to set up your development environment, understand the project 
structure, and find resources to help you add muscles to your skeleton — transforming it into a fully-featured 
application.
