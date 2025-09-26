package service.impl;

import dao.PaiementDAO;

import enums.StatutPaiement;
import entity.paiement.Paiement;
import service.PaiementService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaiementServiceImpl implements PaiementService {
    private final PaiementDAO paiementDAO;

    public PaiementServiceImpl(PaiementDAO paiementDAO) {
        this.paiementDAO = paiementDAO;
    }

    @Override
    public void enregistrerPaiement(Paiement paiement) {
        try {
            paiementDAO.create(paiement);
            System.out.println("Paiement enregistré avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du paiement: " + e.getMessage());
            throw new RuntimeException("Impossible d'enregistrer le paiement", e);
        }
    }

    @Override
    public Optional<Paiement> obtenirPaiement(String id) {
        try {
            return paiementDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche du paiement: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Paiement> obtenirPaiementsParAbonnement(String idAbonnement) {
        try {
            return paiementDAO.findByAbonnement(idAbonnement);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des paiements: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les paiements", e);
        }
    }

    @Override
    public List<Paiement> obtenirTousLesPaiements() {
        try {
            return paiementDAO.findAll();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des paiements: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les paiements", e);
        }
    }

    @Override
    public void modifierPaiement(Paiement paiement) {
        try {
            paiementDAO.update(paiement);
            System.out.println("Paiement modifié avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du paiement: " + e.getMessage());
            throw new RuntimeException("Impossible de modifier le paiement", e);
        }
    }

    @Override
    public void supprimerPaiement(String id) {
        try {
            paiementDAO.delete(id);
            System.out.println("Paiement supprimé avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du paiement: " + e.getMessage());
            throw new RuntimeException("Impossible de supprimer le paiement", e);
        }
    }

    @Override
    public List<Paiement> detecterImpayes() {
        try {
            return paiementDAO.findAll().stream()
                    .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE || p.getStatut() == StatutPaiement.EN_RETARD)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur lors de la détection des impayés: " + e.getMessage());
            throw new RuntimeException("Impossible de détecter les impayés", e);
        }
    }

    @Override
    public List<Paiement> obtenirDerniersPaiements(int nombre) {
        try {
            return paiementDAO.findLastPayments(nombre);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des derniers paiements: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les derniers paiements", e);
        }
    }

    @Override
    public double calculerMontantTotalImpaye(String idAbonnement) {
        try {
            return paiementDAO.findUnpaidByAbonnement(idAbonnement).stream()
                    .mapToDouble(Paiement::getMontant)
                    .sum();
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul du montant impayé: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public double calculerSommePaye(String idAbonnement) {
        try {
            return paiementDAO.findByAbonnement(idAbonnement).stream()
                    .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                    .mapToDouble(Paiement::getMontant)
                    .sum();
        } catch (Exception e) {
            System.err.println("Erreur lors du calcul de la somme payée: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public void marquerPaiementCommeRetard() {
        try {
            LocalDate aujourd = LocalDate.now();
            List<Paiement> paiements = paiementDAO.findAll().stream()
                    .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE)
                    .filter(p -> p.getDateEcheance().isBefore(aujourd))
                    .collect(Collectors.toList());

            paiements.forEach(p -> {
                p.setStatut(StatutPaiement.EN_RETARD);
                paiementDAO.update(p);
            });

            if (!paiements.isEmpty()) {
                System.out.println(paiements.size() + " paiement(s) marqué(s) en retard");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des retards: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Double> genererRapportMensuel(int mois, int annee) {
        try {
            return paiementDAO.findAll().stream()
                    .filter(p -> p.getDatePaiement() != null)
                    .filter(p -> p.getDatePaiement().getMonthValue() == mois && p.getDatePaiement().getYear() == annee)
                    .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                    .collect(Collectors.groupingBy(
                            p -> "Mois " + mois + "/" + annee,
                            Collectors.summingDouble(Paiement::getMontant)
                    ));
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport mensuel: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Double> genererRapportAnnuel(int annee) {
        try {
            return paiementDAO.findAll().stream()
                    .filter(p -> p.getDatePaiement() != null)
                    .filter(p -> p.getDatePaiement().getYear() == annee)
                    .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                    .collect(Collectors.groupingBy(
                            p -> "Année " + annee,
                            Collectors.summingDouble(Paiement::getMontant)
                    ));
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du rapport annuel: " + e.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public List<Paiement> obtenirPaiementsImpayes() {
        try {
            return detecterImpayes();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des impayés: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les paiements impayés", e);
        }
    }
}
