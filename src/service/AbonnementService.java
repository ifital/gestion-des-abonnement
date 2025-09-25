package service;

import java.util.List;
import java.util.Optional;
import entity.abonnement.Abonnement;

public interface AbonnementService {
    void creerAbonnement(Abonnement abonnement);
    Optional<Abonnement> obtenirAbonnement(String id);
    List<Abonnement> obtenirTousLesAbonnements();
    void modifierAbonnement(Abonnement abonnement);
    void supprimerAbonnement(String id);
    void resilierAbonnement(String id);
    List<Abonnement> obtenirAbonnementsActifs();
    List<Abonnement> obtenirAbonnementsParType(String type);
    void genererEcheances(String idAbonnement);
}



