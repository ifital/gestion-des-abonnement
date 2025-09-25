package service;

import entity.paiement.Paiement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PaiementService {
    void enregistrerPaiement(Paiement paiement);
    Optional<Paiement> obtenirPaiement(String id);
    List<Paiement> obtenirPaiementsParAbonnement(String idAbonnement);
    List<Paiement> obtenirTousLesPaiements();
    void modifierPaiement(Paiement paiement);
    void supprimerPaiement(String id);
    List<Paiement> detecterImpayes();
    List<Paiement> obtenirDerniersPaiements(int nombre);
    double calculerMontantTotalImpaye(String idAbonnement);
    double calculerSommePaye(String idAbonnement);
    void marquerPaiementCommeRetard();
    Map<String, Double> genererRapportMensuel(int mois, int annee);
    Map<String, Double> genererRapportAnnuel(int annee);
    List<Paiement> obtenirPaiementsImpayes();
}
