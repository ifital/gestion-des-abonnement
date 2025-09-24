package dao;

import entity.abonnement.Abonnement;

import java.util.List;
import java.util.Optional;

public interface AbonnementDAO {

    void create(Abonnement abonnement);
    Optional<Abonnement> findById(String id);
    List<Abonnement> findAll();
    void update(Abonnement abonnement);
    void delete(String id);
    List<Abonnement> findActiveSubscriptions();
    List<Abonnement> findByType(String type);

}
