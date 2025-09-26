package ui;

import dao.impl.AbonnementDAOImpl;
import dao.impl.PaiementDAOImpl;
import entity.abonnement.AbonnementSansEngagement;
import entity.abonnement.AbonnementAvecEngagement;
import entity.abonnement.Abonnement;
import entity.paiement.Paiement;
import enums.StatutAbonnement;
import enums.StatutPaiement;
import enums.TypePaiement;
import service.AbonnementService;
import service.PaiementService;
import service.impl.AbonnementServiceImpl;
import service.impl.PaiementServiceImpl;
import util.DateUtil;
import util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final AbonnementService abonnementService;
    private final PaiementService paiementService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        AbonnementDAOImpl abonnementDAO = new AbonnementDAOImpl();
        PaiementDAOImpl paiementDAO = new PaiementDAOImpl();
        this.abonnementService = new AbonnementServiceImpl(abonnementDAO, paiementDAO);
        this.paiementService = new PaiementServiceImpl(paiementDAO);
    }

    public void demarrer() {
        System.out.println("=== GESTIONNAIRE D'ABONNEMENTS ===");
        System.out.println("Bienvenue dans votre gestionnaire d'abonnements !");

        while (true) {
            afficherMenuPrincipal();
            String choix = scanner.nextLine().trim();

            try {
                switch (choix) {
                    case "1":
                        menuAbonnements();
                        break;
                    case "2":
                        menuPaiements();
                        break;
                    case "3":
                        menuRapports();
                        break;
                    case "4":
                        System.out.println("Vérification des paiements en retard...");
                        paiementService.marquerPaiementCommeRetard();
                        break;
                    case "0":
                        System.out.println("Au revoir !");
                        return;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.err.println("Une erreur s'est produite: " + e.getMessage());
                System.out.println("Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n" + "============================================================");
        System.out.println("MENU PRINCIPAL");
        System.out.println("===================================================");
        System.out.println("1. Gestion des Abonnements");
        System.out.println("2. Gestion des Paiements");
        System.out.println("3. Rapports et Analyses");
        System.out.println("4. Vérifier les retards");
        System.out.println("0. Quitter");
        System.out.print("Votre choix: ");
    }

    private void menuAbonnements() {
        while (true) {
            System.out.println("\n" + "===========================================");
            System.out.println("GESTION DES ABONNEMENTS");
            System.out.println("=====================================================");
            System.out.println("1. Créer un abonnement");
            System.out.println("2. Lister tous les abonnements");
            System.out.println("3. Rechercher un abonnement");
            System.out.println("4. Modifier un abonnement");
            System.out.println("5. Supprimer un abonnement");
            System.out.println("6. Résilier un abonnement");
            System.out.println("7. Abonnements actifs seulement");
            System.out.println("8. Générer les échéances");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    creerAbonnement();
                    break;
                case "2":
                    listerAbonnements();
                    break;
                case "3":
                    rechercherAbonnement();
                    break;
                case "4":
                    modifierAbonnement();
                    break;
                case "5":
                    supprimerAbonnement();
                    break;
                case "6":
                    resilierAbonnement();
                    break;
                case "7":
                    listerAbonnementsActifs();
                    break;
                case "8":
                    genererEcheances();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void creerAbonnement() {
        System.out.println("\n--- CRÉATION D'UN ABONNEMENT ---");

        System.out.print("Nom du service: ");
        String nomService = scanner.nextLine().trim();
        if (!ValidationUtil.isValidServiceName(nomService)) {
            System.out.println("Nom de service invalide (minimum 2 caractères)");
            return;
        }

        System.out.print("Montant mensuel (€): ");
        String montantStr = scanner.nextLine().trim();
        if (!ValidationUtil.isValidAmount(montantStr)) {
            System.out.println("Montant invalide");
            return;
        }
        double montant = Double.parseDouble(montantStr);

        System.out.print("Date de début (dd/MM/yyyy): ");
        String dateDebutStr = scanner.nextLine().trim();
        Optional<LocalDate> dateDebut = DateUtil.parseDate(dateDebutStr);
        if (!dateDebut.isPresent()) {
            System.out.println("Date de début invalide");
            return;
        }

        System.out.print("Date de fin (dd/MM/yyyy) [Optionnel]: ");
        String dateFinStr = scanner.nextLine().trim();
        LocalDate dateFin = null;
        if (!dateFinStr.isEmpty()) {
            Optional<LocalDate> dateFinOpt = DateUtil.parseDate(dateFinStr);
            if (!dateFinOpt.isPresent()) {
                System.out.println("Date de fin invalide");
                return;
            }
            dateFin = dateFinOpt.get();
        }

        System.out.println("Type d'abonnement:");
        System.out.println("1. Avec engagement");
        System.out.println("2. Sans engagement");
        System.out.print("Votre choix: ");
        String typeChoix = scanner.nextLine().trim();

        Abonnement abonnement;
        if ("1".equals(typeChoix)) {
            System.out.print("Durée d'engagement (en mois): ");
            String dureeStr = scanner.nextLine().trim();
            if (!ValidationUtil.isValidInteger(dureeStr)) {
                System.out.println("Durée d'engagement invalide");
                return;
            }
            int duree = Integer.parseInt(dureeStr);
            abonnement = new AbonnementAvecEngagement(nomService, montant, dateDebut.get(), dateFin, duree);
        } else if ("2".equals(typeChoix)) {
            abonnement = new AbonnementSansEngagement(nomService, montant, dateDebut.get(), dateFin);
        } else {
            System.out.println("Type d'abonnement invalide");
            return;
        }

        abonnementService.creerAbonnement(abonnement);
    }

    private void listerAbonnements() {
        System.out.println("\n--- LISTE DES ABONNEMENTS ---");
        List<Abonnement> abonnements = abonnementService.obtenirTousLesAbonnements();

        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement trouvé");
            return;
        }

        abonnements.forEach(System.out::println);
        System.out.println("\nTotal: " + abonnements.size() + " abonnement(s)");
    }

    private void rechercherAbonnement() {
        System.out.print("ID de l'abonnement: ");
        String id = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(id);
        if (abonnement.isPresent()) {
            System.out.println("\nAbonnement trouvé:");
            System.out.println(abonnement.get());
        } else {
            System.out.println("Abonnement non trouvé");
        }
    }

    private void modifierAbonnement() {
        System.out.print("ID de l'abonnement à modifier: ");
        String id = scanner.nextLine().trim();

        Optional<Abonnement> abonnementOpt = abonnementService.obtenirAbonnement(id);
        if (!abonnementOpt.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        Abonnement abonnement = abonnementOpt.get();
        System.out.println("Abonnement actuel: " + abonnement);

        System.out.print("Nouveau nom du service [" + abonnement.getNomService() + "]: ");
        String nouveauNom = scanner.nextLine().trim();
        if (!nouveauNom.isEmpty() && ValidationUtil.isValidServiceName(nouveauNom)) {
            abonnement.setNomService(nouveauNom);
        }

        System.out.print("Nouveau montant mensuel [" + abonnement.getMontantMensuel() + "]: ");
        String nouveauMontantStr = scanner.nextLine().trim();
        if (!nouveauMontantStr.isEmpty() && ValidationUtil.isValidAmount(nouveauMontantStr)) {
            abonnement.setMontantMensuel(Double.parseDouble(nouveauMontantStr));
        }

        System.out.print("Nouveau statut (ACTIF/SUSPENDU/RESILIE) [" + abonnement.getStatut() + "]: ");
        String nouveauStatutStr = scanner.nextLine().trim();
        if (!nouveauStatutStr.isEmpty()) {
            Optional<StatutAbonnement> nouveauStatut = ValidationUtil.parseStatutAbonnement(nouveauStatutStr);
            if (nouveauStatut.isPresent()) {
                abonnement.setStatut(nouveauStatut.get());
            }
        }

        abonnementService.modifierAbonnement(abonnement);
    }

    private void supprimerAbonnement() {
        System.out.print("ID de l'abonnement à supprimer: ");
        String id = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(id);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        System.out.println("Abonnement à résilier: " + abonnement.get());
        System.out.print("Êtes-vous sûr ? (oui/non): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("oui".equals(confirmation)) {
            abonnementService.resilierAbonnement(id);
        } else {
            System.out.println("Résiliation annulée");
        }
    }

    private void resilierAbonnement() {
        System.out.print("ID de l'abonnement à résilier: ");
        String id = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(id);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        System.out.println("Abonnement à résilier: " + abonnement.get());
        System.out.print("Êtes-vous sûr ? (oui/non): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("oui".equals(confirmation)) {
            abonnementService.resilierAbonnement(id);
        } else {
            System.out.println("Résiliation annulée");
        }
    }

    private void listerAbonnementsActifs() {
        System.out.println("\n--- ABONNEMENTS ACTIFS ---");
        List<Abonnement> abonnements = abonnementService.obtenirAbonnementsActifs();

        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement actif trouvé");
            return;
        }

        abonnements.forEach(System.out::println);
        System.out.println("\nTotal: " + abonnements.size() + " abonnement(s) actif(s)");
    }



    private void genererEcheances() {
        System.out.print("ID de l'abonnement pour générer les échéances: ");
        String id = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(id);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        abonnementService.genererEcheances(id);
    }

    private void menuPaiements() {
        while (true) {
            System.out.println("\n" + "============================================");
            System.out.println("GESTION DES PAIEMENTS");
            System.out.println("==================================================");
            System.out.println("1. Enregistrer un paiement");
            System.out.println("2. Afficher les paiements d'un abonnement");
            System.out.println("3. Modifier un paiement");
            System.out.println("4. Supprimer un paiement");
            System.out.println("5. Consulter les paiements manqués");
            System.out.println("6. Afficher la somme payée d'un abonnement");
            System.out.println("7. Afficher les 5 derniers paiements");
            System.out.println("8. Lister tous les paiements");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    enregistrerPaiement();
                    break;
                case "2":
                    afficherPaiementsAbonnement();
                    break;
                case "3":
                    modifierPaiement();
                    break;
                case "4":
                    supprimerPaiement();
                    break;
                case "5":
                    consulterPaiementsManques();
                    break;
                case "6":
                    afficherSommePaye();
                    break;
                case "7":
                    afficherDerniersPaiements();
                    break;
                case "8":
                    listerTousPaiements();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void enregistrerPaiement() {
        System.out.println("\n--- ENREGISTRER UN PAIEMENT ---");

        System.out.print("ID de l'abonnement: ");
        String idAbonnement = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(idAbonnement);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        System.out.print("Date d'échéance (dd/MM/yyyy): ");
        String dateEcheanceStr = scanner.nextLine().trim();
        Optional<LocalDate> dateEcheance = DateUtil.parseDate(dateEcheanceStr);
        if (!dateEcheance.isPresent()) {
            System.out.println("Date d'échéance invalide");
            return;
        }

        System.out.print("Montant: ");
        String montantStr = scanner.nextLine().trim();
        if (!ValidationUtil.isValidAmount(montantStr)) {
            System.out.println("Montant invalide");
            return;
        }
        double montant = Double.parseDouble(montantStr);

        Paiement paiement = new Paiement(idAbonnement, dateEcheance.get(), montant);

        System.out.print("Le paiement a-t-il été effectué ? (oui/non): ");
        String paye = scanner.nextLine().trim().toLowerCase();

        if ("oui".equals(paye)) {
            System.out.print("Date de paiement (dd/MM/yyyy): ");
            String datePaiementStr = scanner.nextLine().trim();
            Optional<LocalDate> datePaiement = DateUtil.parseDate(datePaiementStr);
            if (datePaiement.isPresent()) {
                paiement.setDatePaiement(datePaiement.get());
                paiement.setStatut(StatutPaiement.PAYE);

                System.out.println("Types de paiement disponibles:");
                ValidationUtil.displayTypePaiementOptions();
                System.out.print("Type de paiement: ");
                String typeStr = scanner.nextLine().trim();
                Optional<TypePaiement> type = ValidationUtil.parseTypePaiement(typeStr);
                if (type.isPresent()) {
                    paiement.setTypePaiement(type.get());
                }
            }
        }

        paiementService.enregistrerPaiement(paiement);
    }

    private void afficherPaiementsAbonnement() {
        System.out.print("ID de l'abonnement: ");
        String idAbonnement = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(idAbonnement);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        System.out.println("\nAbonnement: " + abonnement.get());
        System.out.println("\n--- PAIEMENTS ---");

        List<Paiement> paiements = paiementService.obtenirPaiementsParAbonnement(idAbonnement);
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé pour cet abonnement");
            return;
        }

        paiements.forEach(System.out::println);
        System.out.println("\nTotal: " + paiements.size() + " paiement(s)");
    }

    private void modifierPaiement() {
        System.out.print("ID du paiement à modifier: ");
        String id = scanner.nextLine().trim();

        Optional<Paiement> paiementOpt = paiementService.obtenirPaiement(id);
        if (!paiementOpt.isPresent()) {
            System.out.println("Paiement non trouvé");
            return;
        }

        Paiement paiement = paiementOpt.get();
        System.out.println("Paiement actuel: " + paiement);

        System.out.print("Nouveau statut (PAYE/NON_PAYE/EN_RETARD) [" + paiement.getStatut() + "]: ");
        String nouveauStatutStr = scanner.nextLine().trim();
        if (!nouveauStatutStr.isEmpty()) {
            Optional<StatutPaiement> nouveauStatut = ValidationUtil.parseStatutPaiement(nouveauStatutStr);
            if (nouveauStatut.isPresent()) {
                paiement.setStatut(nouveauStatut.get());

                if (nouveauStatut.get() == StatutPaiement.PAYE && paiement.getDatePaiement() == null) {
                    System.out.print("Date de paiement (dd/MM/yyyy): ");
                    String datePaiementStr = scanner.nextLine().trim();
                    Optional<LocalDate> datePaiement = DateUtil.parseDate(datePaiementStr);
                    if (datePaiement.isPresent()) {
                        paiement.setDatePaiement(datePaiement.get());
                    }

                    if (paiement.getTypePaiement() == null) {
                        System.out.println("Types de paiement disponibles:");
                        ValidationUtil.displayTypePaiementOptions();
                        System.out.print("Type de paiement: ");
                        String typeStr = scanner.nextLine().trim();
                        Optional<TypePaiement> type = ValidationUtil.parseTypePaiement(typeStr);
                        if (type.isPresent()) {
                            paiement.setTypePaiement(type.get());
                        }
                    }
                }
            }
        }

        paiementService.modifierPaiement(paiement);
    }

    private void supprimerPaiement() {
        System.out.print("ID du paiement à supprimer: ");
        String id = scanner.nextLine().trim();

        Optional<Paiement> paiement = paiementService.obtenirPaiement(id);
        if (!paiement.isPresent()) {
            System.out.println("Paiement non trouvé");
            return;
        }

        System.out.println("Paiement à supprimer: " + paiement.get());
        System.out.print("Êtes-vous sûr ? (oui/non): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if ("oui".equals(confirmation)) {
            paiementService.supprimerPaiement(id);
        } else {
            System.out.println("Suppression annulée");
        }
    }

    private void consulterPaiementsManques() {
        System.out.println("\n--- PAIEMENTS MANQUÉS ---");
        List<Paiement> impayes = paiementService.obtenirPaiementsImpayes();

        if (impayes.isEmpty()) {
            System.out.println("Aucun paiement impayé trouvé");
            return;
        }

        Map<String, List<Paiement>> paiementsParAbonnement = impayes.stream()
                .collect(java.util.stream.Collectors.groupingBy(Paiement::getIdAbonnement));

        double totalImpaye = 0.0;

        for (Map.Entry<String, List<Paiement>> entry : paiementsParAbonnement.entrySet()) {
            String idAbonnement = entry.getKey();
            List<Paiement> paiements = entry.getValue();

            Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(idAbonnement);
            if (abonnement.isPresent()) {
                System.out.println("\nAbonnement: " + abonnement.get().getNomService());

                double montantAbonnementImpaye = paiements.stream()
                        .mapToDouble(Paiement::getMontant)
                        .sum();

                System.out.println("Montant impayé: " + String.format("%.2f€", montantAbonnementImpaye));
                paiements.forEach(p -> System.out.println("  " + p));

                totalImpaye += montantAbonnementImpaye;
            }
        }

        System.out.println("\n" + "===============================================");
        System.out.println("MONTANT TOTAL IMPAYÉ: " + String.format("%.2f€", totalImpaye));
        System.out.println("Nombre total de paiements impayés: " + impayes.size());
    }

    private void afficherSommePaye() {
        System.out.print("ID de l'abonnement: ");
        String idAbonnement = scanner.nextLine().trim();

        Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(idAbonnement);
        if (!abonnement.isPresent()) {
            System.out.println("Abonnement non trouvé");
            return;
        }

        double sommePaye = paiementService.calculerSommePaye(idAbonnement);
        System.out.println("\nAbonnement: " + abonnement.get().getNomService());
        System.out.println("Somme totale payée: " + String.format("%.2f€", sommePaye));
    }

    private void afficherDerniersPaiements() {
        System.out.println("\n--- LES 5 DERNIERS PAIEMENTS ---");
        List<Paiement> derniersPaiements = paiementService.obtenirDerniersPaiements(5);

        if (derniersPaiements.isEmpty()) {
            System.out.println("Aucun paiement effectué trouvé");
            return;
        }

        derniersPaiements.forEach(p -> {
            Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(p.getIdAbonnement());
            String nomService = abonnement.map(Abonnement::getNomService).orElse("Service inconnu");
            System.out.println(nomService + " - " + p);
        });
    }

    private void listerTousPaiements() {
        System.out.println("\n--- TOUS LES PAIEMENTS ---");
        List<Paiement> paiements = paiementService.obtenirTousLesPaiements();

        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement trouvé");
            return;
        }

        paiements.forEach(System.out::println);
        System.out.println("\nTotal: " + paiements.size() + " paiement(s)");
    }

    private void menuRapports() {
        while (true) {
            System.out.println("\n" + "==================================================");
            System.out.println("RAPPORTS ET ANALYSES");
            System.out.println("============================================================");
            System.out.println("1. Rapport mensuel");
            System.out.println("2. Rapport annuel");
            System.out.println("3. Analyse des impayés");
            System.out.println("4. Synthèse globale");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    genererRapportMensuel();
                    break;
                case "2":
                    genererRapportAnnuel();
                    break;
                case "3":
                    analyseImpayes();
                    break;
                case "4":
                    syntheseGlobale();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    private void genererRapportMensuel() {
        System.out.print("Mois (1-12): ");
        String moisStr = scanner.nextLine().trim();
        System.out.print("Année: ");
        String anneeStr = scanner.nextLine().trim();

        if (!ValidationUtil.isValidInteger(moisStr) || !ValidationUtil.isValidInteger(anneeStr)) {
            System.out.println("Mois ou année invalide");
            return;
        }

        int mois = Integer.parseInt(moisStr);
        int annee = Integer.parseInt(anneeStr);

        if (mois < 1 || mois > 12) {
            System.out.println("Mois invalide (1-12)");
            return;
        }

        Map<String, Double> rapport = paiementService.genererRapportMensuel(mois, annee);

        System.out.println("\n--- RAPPORT MENSUEL " + mois + "/" + annee + " ---");
        if (rapport.isEmpty()) {
            System.out.println("Aucun paiement trouvé pour cette période");
        } else {
            rapport.forEach((periode, montant) ->
                    System.out.println(periode + ": " + String.format("%.2f€", montant)));
        }
    }

    private void genererRapportAnnuel() {
        System.out.print("Année: ");
        String anneeStr = scanner.nextLine().trim();

        if (!ValidationUtil.isValidInteger(anneeStr)) {
            System.out.println("Année invalide");
            return;
        }

        int annee = Integer.parseInt(anneeStr);
        Map<String, Double> rapport = paiementService.genererRapportAnnuel(annee);

        System.out.println("\n--- RAPPORT ANNUEL " + annee + " ---");
        if (rapport.isEmpty()) {
            System.out.println("Aucun paiement trouvé pour cette année");
        } else {
            rapport.forEach((periode, montant) ->
                    System.out.println(periode + ": " + String.format("%.2f€", montant)));
        }
    }

    private void analyseImpayes() {
        System.out.println("\n--- ANALYSE DES IMPAYÉS ---");
        List<Paiement> impayes = paiementService.obtenirPaiementsImpayes();

        if (impayes.isEmpty()) {
            System.out.println("Aucun impayé détecté");
            return;
        }

        Map<StatutPaiement, Long> repartitionStatuts = impayes.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Paiement::getStatut,
                        java.util.stream.Collectors.counting()
                ));

        System.out.println("Répartition par statut:");
        repartitionStatuts.forEach((statut, nombre) ->
                System.out.println("- " + statut + ": " + nombre + " paiement(s)"));

        double montantTotal = impayes.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();

        System.out.println("\nMontant total impayé: " + String.format("%.2f€", montantTotal));
        System.out.println("Nombre total d'impayés: " + impayes.size());

        // Analyse par abonnement
        Map<String, List<Paiement>> paiementsParAbonnement = impayes.stream()
                .collect(java.util.stream.Collectors.groupingBy(Paiement::getIdAbonnement));

        System.out.println("\nTop 5 des abonnements avec le plus d'impayés:");
        paiementsParAbonnement.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(5)
                .forEach(entry -> {
                    String idAbonnement = entry.getKey();
                    List<Paiement> paiements = entry.getValue();
                    Optional<Abonnement> abonnement = abonnementService.obtenirAbonnement(idAbonnement);
                    String nomService = abonnement.map(Abonnement::getNomService).orElse("Service inconnu");
                    double montantImpaye = paiements.stream().mapToDouble(Paiement::getMontant).sum();
                    System.out.println("- " + nomService + ": " + paiements.size() +
                            " impayé(s) (" + String.format("%.2f€", montantImpaye) + ")");
                });
    }

    private void syntheseGlobale() {
        System.out.println("\n" + "==========================================");
        System.out.println("SYNTHÈSE GLOBALE");
        System.out.println("=====================================================");

        // Statistiques abonnements
        List<Abonnement> tousAbonnements = abonnementService.obtenirTousLesAbonnements();
        List<Abonnement> abonnementsActifs = abonnementService.obtenirAbonnementsActifs();

        System.out.println("ABONNEMENTS:");
        System.out.println("- Total: " + tousAbonnements.size());
        System.out.println("- Actifs: " + abonnementsActifs.size());
        System.out.println("- Inactifs: " + (tousAbonnements.size() - abonnementsActifs.size()));

        double montantMensuelTotal = abonnementsActifs.stream()
                .mapToDouble(Abonnement::getMontantMensuel)
                .sum();
        System.out.println("- Coût mensuel total des abonnements actifs: " +
                String.format("%.2f€", montantMensuelTotal));
        System.out.println("- Coût annuel estimé: " +
                String.format("%.2f€", montantMensuelTotal * 12));

        // Statistiques paiements
        List<Paiement> tousPaiements = paiementService.obtenirTousLesPaiements();
        List<Paiement> paiementsEffectues = tousPaiements.stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .collect(java.util.stream.Collectors.toList());

        System.out.println("\nPAIEMENTS:");
        System.out.println("- Total des paiements: " + tousPaiements.size());
        System.out.println("- Paiements effectués: " + paiementsEffectues.size());
        System.out.println("- Paiements en attente: " + (tousPaiements.size() - paiementsEffectues.size()));

        double montantTotalPaye = paiementsEffectues.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
        System.out.println("- Montant total payé: " + String.format("%.2f€", montantTotalPaye));

        List<Paiement> impayes = paiementService.obtenirPaiementsImpayes();
        double montantImpaye = impayes.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
        System.out.println("- Montant impayé: " + String.format("%.2f€", montantImpaye));

        // Répartition par type d'abonnement
        Map<String, Long> repartitionTypes = tousAbonnements.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Abonnement::getTypeAbonnement,
                        java.util.stream.Collectors.counting()
                ));

        System.out.println("\nRÉPARTITION PAR TYPE:");
        repartitionTypes.forEach((type, nombre) ->
                System.out.println("- " + type + ": " + nombre + " abonnement(s)"));

        System.out.println("\n" + "=====================================================");
    }
}