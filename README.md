# Citronix 🍋

Citronix est RESTAPI une application de gestion développée en Java Spring Boot, conçue pour la gestion complète de fermes de citrons. Elle permet aux agriculteurs de suivre efficacement leur production, de la plantation à la vente.

## Fonctionnalités Principales

### Gestion des Fermes

- Création de nouvelles fermes avec informations détaillées
- Consultation des fermes existantes (individuelle ou liste paginée)
- Mise à jour des informations des fermes
- Suppression de fermes
- Recherche avancée de fermes selon plusieurs critères :
  - Nom
  - Adresse
  - Superficie (min/max)
  - Période (date début/fin)

### Gestion des Champs

- Création de nouveaux champs agricoles
- Consultation des champs (individuelle ou liste paginée)
- Mise à jour des informations des champs
- Suppression de champs
- Association des champs aux fermes

### Gestion des Arbres

- Ajout de nouveaux arbres avec leurs caractéristiques
- Consultation des arbres (individuelle ou liste paginée)
- Suppression d'arbres
- Suivi de l'état des arbres
- Association des arbres aux champs

### Gestion des Récoltes

- Enregistrement des récoltes
- Consultation des récoltes (individuelle ou liste paginée)
- Gestion détaillée des récoltes :
  - Création de détails de récolte
  - Consultation des détails
  - Suppression des détails
- Suivi des quantités récoltées

### Gestion des Ventes

- Enregistrement des nouvelles ventes
- Consultation des ventes (individuelle ou liste paginée)
- Mise à jour des informations de vente
- Suppression des ventes
- Suivi des transactions commerciales

## Technologies Utilisées

- Java 1.8
- Spring Boot 2.7.18
- Maven
- Spring Data JPA
- MapStruct 1.5.5
- Lombok
- JUnit 4.13.2 & Mockito 4.5.1
- OpenAPI/Swagger UI 1.6.15 pour la documentation API
- Base de données PostgreSQL et H2

## Prérequis

- JDK 1.8 ou supérieur
- Maven 2.7.18 ou supérieur
- Postgresql/H2

## Installation et démarrage

1. Cloner le projet depuis le repository GitHub

url : https://github.com/JavaAura/BELHAJ_MOKHLIS_S3_B4_Citronix

2. acceder au dossier citronix
```bash
cd Citronix/Citronix
```

3. Exécuter la commande `mvn clean install` pour installer les dépendances
4. Exécuter la commande `mvn spring-boot:run` pour démarrer l'application

pour acceder a l'api documenter :


[swagger-ui](http://localhost:8080/swagger-ui/index.html)


## CONCEPTION UML

![diagramme class](https://raw.githubusercontent.com/JavaAura/BELHAJ_MOKHLIS_S3_B4_Citronix/main/Documment/diagramme-class.png)







