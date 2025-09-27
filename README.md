# 📦 Gestion des Abonnements et Paiements - Java Console

## 📌 Description du projet
Ce projet est une application console en Java qui permet de gérer des **abonnements** et leurs **paiements**.  
Elle offre la possibilité de créer différents types d’abonnements (avec ou sans engagement), d’enregistrer et consulter les paiements, de générer des rapports financiers (mensuels et annuels), ainsi que de détecter automatiquement les impayés et retards.

L’objectif est de pratiquer la **programmation orientée objet (POO)**, la gestion des exceptions, l’utilisation de **DAO / Services**, et l’exploitation des **Streams Java** pour traiter les données.

---

## 🏗 Structure du projet
```bash


Gestion des abonnements et paiements/
├── src/
│ ├── ui/
│ │ └── ConsoleUI.java
│ ├── dao/
│ │ ├── AbonnementDAO.java
│ │ └── PaiementDAO.java
│ ├── dao/impl/
│ │ ├── AbonnementDAOImpl.java
│ │ └── PaiementDAOImpl.java
│ ├── entity/
│ │ ├── abonnement/
│ │ │ ├── Abonnement.java
│ │ │ ├── AbonnementAvecEngagement.java
│ │ │ └── AbonnementSansEngagement.java
│ │ └── paiement/
│ │ └── Paiement.java
│ ├── enums/
│ │ ├── StatutAbonnement.java
│ │ ├── StatutPaiement.java
│ │ └── TypePaiement.java
│ ├── service/
│ │ ├── AbonnementService.java
│ │ └── PaiementService.java
│ ├── service/impl/
│ │ ├── AbonnementServiceImpl.java
│ │ └── PaiementServiceImpl.java
│ └── util/
│ ├── DateUtil.java
│ └── ValidationUtil.java
└── Main.java
```

---

## ⚙️ Fonctionnalités

### 1. Gestion des abonnements
- Créer un abonnement avec ou sans engagement.
- Rechercher un abonnement par son ID.
- Modifier et résilier un abonnement.
- Lister tous les abonnements (actifs ou inactifs).
- Générer automatiquement les **échéances** d’un abonnement.

### 2. Gestion des paiements
- Enregistrer un paiement (date, montant, type).
- Modifier ou supprimer un paiement.
- Consulter les paiements liés à un abonnement.
- Lister les **5 derniers paiements**.
- Calculer la **somme totale payée** pour un abonnement.
- Détecter et afficher les **paiements impayés**.

### 3. Rapports et analyses
- Générer un **rapport mensuel** des paiements.
- Générer un **rapport annuel** des paiements.
- Analyser les **impayés** et marquer automatiquement les retards.
- Obtenir une **synthèse globale** :
  - Nombre d’abonnements actifs et résiliés.
  - Montant total des abonnements actifs.
  - Montant total payé et impayé.
  - Répartition des abonnements par type.

---

## 🛠 Technologies utilisées
- **Java 8**
- **DAO / Service**
- **Collections & Streams API**
- **LocalDate** pour la gestion des dates
- **Scanner (CLI)** pour l’interaction utilisateur

---

## 🚀 Instructions pour exécuter le projet

1. **Cloner le dépôt** ou copier les fichiers dans un projet IntelliJ IDEA, Eclipse ou VS Code.
2. **Compiler le projet** :
```bash
javac -d bin src/ui/ConsoleUI.java
