package dao;

import entity.abonnement.Abonnement;
import entity.paiement.Paiement;

import java.util.List;
import java.util.Optional;

public interface PaiementDAO {
    void create(Paiement paiement);
    Optional<Paiement> findById(String id);
    List<Paiement> findByAbonnement(String idAbonnement);
    List<Paiement> findAll();
    void update(Paiement paiement);
    void delete(String id);
    List<Paiement> findUnpaidByAbonnement(String idAbonnement);
    List<Paiement> findLastPayments(int limit);
}
