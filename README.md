# Projet Chatop - Angular | Spring Boot & MySQL

## Description
Ce projet consiste en une application web de gestion d'annonces de location (Chatop), développée en utilisant Java et Spring Boot pour le backend, ainsi qu'Angular pour le frontend. Ce guide vous accompagnera étape par étape pour configurer et exécuter l'application.

## Prérequis
- **Java 17+** 
- **Node.js** 
- **Npm** 
- **MySQL** 
- **Git** 

## Installation du Frontend

**1. Cloner le dépôt du frontend :**

 ```bash
   git clone https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring.git
```

**2. Accéder au répertoire du frontend :**

 ```bash
  cd Developpez-le-back-end-en-utilisant-Java-et-Spring
   ```
**3. Developpez-le-back-end-en-utilisant-Java-et-Spring**

```bash
  npm install
   ```
**4. Lancer l'application Angular :**

```bash
  ng serve
   ```

**5. L'application sera accessible à l'adresse suivante :**

```bash
  http://localhost:4200
   ```

## Installation du Backend

**1. Créer une nouvelle base de données dans MySQL :**

```bash
  CREATE DATABASE chatopdata;
  ```
**2. Télécharger le script SQL [ici](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring/blob/main/ressources/sql/script.sql) et exécuter le :** 

```bash
  USE chatopdata;
   ```
```bash
  SOURCE C:/your/path/to/script.sql;
   ```
   
**3. Cloner le dépôt du back-end :**

 ```bash
   git clone https://github.com/mohaali77/OC-DevFullStack-P3-Developpez-le-back-end-en-utilisant-Java-et-Spring.git
```

**4. Accéder au répertoire du back-end :**

 ```bash
  cd OC-DevFullStack-P3-Developpez-le-back-end-en-utilisant-Java-et-Spring
   ```
   
**5. Configurer le fichier application.properties :**

```bash
  spring.datasource.username=VOTRE_USERNAME_MYSQL
  spring.datasource.password=VOTRE_PASSWORD_MYSQL

  # Clé secrète JWT (doit être au format 256 bits)
  jwt.secret=VOTRE_CLE_JWT
   ```
   
**6. Lancer l'application Spring Boot :**

```bash
  ./mvnw spring-boot:run
```

## Documentation API

Vous pouvez accéder à la documentation de l'API générée automatiquement par Swagger à l'adresse suivante une fois le backend lancé :

```bash
http://localhost:3001/swagger-ui.html
```


   