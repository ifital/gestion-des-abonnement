package dao.impl;

import config.DatabaseConfig;
import dao.PaiementDAO;
import entity.paiement.Paiement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void create(Paiement paiement) {
    }

    @Override
    public Optional<Paiement> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Paiement> findByAbonnement(String idAbonnement) {
        return Collections.emptyList();
    }

    @Override
    public List<Paiement> findAll() {
        return Collections.emptyList();
    }

    @Override
    public void update(Paiement paiement) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public List<Paiement> findUnpaidAbonnment(String idAbonnement) {
        return Collections.emptyList();
    }

    @Override
    public List<Paiement> findLastPaiment(int limit) {
        return Collections.emptyList();
    }
}
