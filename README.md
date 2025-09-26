# Gestionnaire d'Abonnements et Paiements

## Description

Application console Java pour gérer des abonnements et leurs paiements.  
Elle permet de créer, modifier, supprimer et lister des abonnements, enregistrer des paiements, générer des rapports mensuels et annuels, et analyser les paiements impayés.

L'architecture suit le modèle **DAO / Service**, avec des entités pour les abonnements (`Abonnement`, `AbonnementAvecEngagement`, `AbonnementSansEngagement`) et les paiements (`Paiement`). Des utilitaires sont inclus pour la validation des données et la gestion des dates.

---

## Fonctionnalités

### Gestion des abonnements
- Créer un abonnement (avec ou sans engagement)
- Lister tous les abonnements
- Rechercher un abonnement par ID
- Modifier un abonnement
- Supprimer ou résilier un abonnement
- Lister uniquement les abonnements actifs
- Générer automatiquement les échéances

### Gestion des paiements
- Enregistrer un paiement (date et type)
- Afficher les paiements d’un abonnement
- Modifier un paiement (statut, date, type)
- Supprimer un paiement
- Consulter les paiements impayés
- Afficher la somme totale payée pour un abonnement
- Afficher les 5 derniers paiements
- Lister tous les paiements

### Rapports et analyses
- Rapport mensuel et annuel des paiements
- Analyse des paiements impayés
- Synthèse globale :
  - Nombre d’abonnements actifs/inactifs
  - Montant total des abonnements actifs
  - Nombre et montant des paiements effectués et impayés
  - Répartition des abonnements par type

### Vérification automatique
- Marquer automatiquement les paiements en retard

---

## Technologies utilisées
- Java 8
- DAO / Service
- Streams Java
- `java.time.LocalDate`
- Interface console (`Scanner`)

---

## Structure du projet
