package service.impl;

import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.abonnement.Abonnement;
import entity.paiement.Paiement;
import enums.StatutAbonnement;
import service.AbonnementService;
import util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AbonnementServiceImpl implements AbonnementService {
    private final AbonnementDAO abonnementDAO;
    private final PaiementDAO paiementDAO;

    public AbonnementServiceImpl(AbonnementDAO abonnementDAO, PaiementDAO paiementDAO) {
        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    @Override
    public void creerAbonnement(Abonnement abonnement) {
        try {
            abonnementDAO.create(abonnement);
            genererEcheances(abonnement.getId());
            System.out.println("Abonnement créé avec succès: " + abonnement.getNomService());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'abonnement: " + e.getMessage());
            throw new RuntimeException("Impossible de créer l'abonnement", e);
        }
    }

    @Override
    public Optional<Abonnement> obtenirAbonnement(String id) {
        try {
            return abonnementDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de l'abonnement: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Abonnement> obtenirTousLesAbonnements() {
        try {
            return abonnementDAO.findAll();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des abonnements: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les abonnements", e);
        }
    }

    @Override
    public void modifierAbonnement(Abonnement abonnement) {
        try {
            abonnementDAO.update(abonnement);
            System.out.println("Abonnement modifié avec succès: " + abonnement.getNomService());
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification de l'abonnement: " + e.getMessage());
            throw new RuntimeException("Impossible de modifier l'abonnement", e);
        }
    }

    @Override
    public void supprimerAbonnement(String id) {
        try {
            // Supprimer d'abord tous les paiements associés
            List<Paiement> paiements = paiementDAO.findByAbonnement(id);
            paiements.forEach(p -> paiementDAO.delete(p.getIdPaiement()));

            // Ensuite supprimer l'abonnement
            abonnementDAO.delete(id);
            System.out.println("Abonnement supprimé avec succès");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'abonnement: " + e.getMessage());
            throw new RuntimeException("Impossible de supprimer l'abonnement", e);
        }
    }

    @Override
    public void resilierAbonnement(String id) {
        try {
            Optional<Abonnement> abonnementOpt = abonnementDAO.findById(id);
            if (abonnementOpt.isPresent()) {
                Abonnement abonnement = abonnementOpt.get();
                abonnement.setStatut(StatutAbonnement.RESILIE);
                abonnement.setDateFin(LocalDate.now());
                abonnementDAO.update(abonnement);
                System.out.println("Abonnement résilié avec succès: " + abonnement.getNomService());
            } else {
                System.out.println("Abonnement non trouvé");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la résiliation de l'abonnement: " + e.getMessage());
            throw new RuntimeException("Impossible de résilier l'abonnement", e);
        }
    }

    @Override
    public List<Abonnement> obtenirAbonnementsActifs() {
        try {
            return abonnementDAO.findActiveSubscriptions();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des abonnements actifs: " + e.getMessage());
            throw new RuntimeException("Impossible de récupérer les abonnements actifs", e);
        }
    }

    @Override
    public List<Abonnement> obtenirAbonnementsParType(String type) {
        try {
            return abonnementDAO.findByType(type);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche par type: " + e.getMessage());
            throw new RuntimeException("Impossible de rechercher les abonnements par type", e);
        }
    }

    @Override
    public void genererEcheances(String idAbonnement) {
        try {
            Optional<Abonnement> abonnementOpt = abonnementDAO.findById(idAbonnement);
            if (!abonnementOpt.isPresent()) {
                System.out.println("Abonnement non trouvé pour la génération d'échéances");
                return;
            }

            Abonnement abonnement = abonnementOpt.get();
            LocalDate dateActuelle = abonnement.getDateDebut();
            LocalDate dateFin = abonnement.getDateFin() != null ? abonnement.getDateFin() : LocalDate.now().plusYears(1);

            int echeancesGenerees = 0;
            while (dateActuelle.isBefore(dateFin) || dateActuelle.equals(dateFin)) {
                // Vérifier si une échéance existe déjà pour cette date
                List<Paiement> paiementsExistants = paiementDAO.findByAbonnement(idAbonnement);
                LocalDate finalDateActuelle = dateActuelle;
                boolean echeanceExiste = paiementsExistants.stream()
                        .anyMatch(p -> p.getDateEcheance().equals(finalDateActuelle));

                if (!echeanceExiste) {
                    Paiement paiement = new Paiement(idAbonnement, dateActuelle, abonnement.getMontantMensuel());
                    paiementDAO.create(paiement);
                    echeancesGenerees++;
                }

                dateActuelle = DateUtil.getNextMonthDate(dateActuelle);
            }

            if (echeancesGenerees > 0) {
                System.out.println(echeancesGenerees + " échéance(s) générée(s) pour l'abonnement " + abonnement.getNomService());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération des échéances: " + e.getMessage());
            throw new RuntimeException("Impossible de générer les échéances", e);
        }
    }
}
