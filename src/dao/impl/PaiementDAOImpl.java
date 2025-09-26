package dao.impl;

import config.DatabaseConfig;
import dao.PaiementDAO;
import enums.StatutPaiement;
import enums.TypePaiement;
import entity.paiement.Paiement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaiementDAOImpl implements PaiementDAO {

    @Override
    public void create(Paiement paiement) {
        String sql = "INSERT INTO paiement (idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut, montant) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paiement.getIdPaiement());
            stmt.setString(2, paiement.getIdAbonnement());
            stmt.setDate(3, Date.valueOf(paiement.getDateEcheance()));
            stmt.setDate(4, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            stmt.setString(5, paiement.getTypePaiement() != null ? paiement.getTypePaiement().name() : null);
            stmt.setString(6, paiement.getStatut().name());
            stmt.setDouble(7, paiement.getMontant());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du paiement", e);
        }
    }

    @Override
    public Optional<Paiement> findById(String id) {
        String sql = "SELECT * FROM paiement WHERE idPaiement = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToPaiement(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du paiement", e);
        }
    }

    @Override
    public List<Paiement> findByAbonnement(String idAbonnement) {
        String sql = "SELECT * FROM paiement WHERE idAbonnement = ? ORDER BY dateEcheance";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAbonnement);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des paiements", e);
        }

        return paiements;
    }

    @Override
    public List<Paiement> findAll() {
        String sql = "SELECT * FROM paiement ORDER BY dateEcheance DESC";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des paiements", e);
        }

        return paiements;
    }

    @Override
    public void update(Paiement paiement) {
        String sql = "UPDATE paiement SET dateEcheance = ?, datePaiement = ?, typePaiement = ?, statut = ?, montant = ? WHERE idPaiement = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(paiement.getDateEcheance()));
            stmt.setDate(2, paiement.getDatePaiement() != null ? Date.valueOf(paiement.getDatePaiement()) : null);
            stmt.setString(3, paiement.getTypePaiement() != null ? paiement.getTypePaiement().name() : null);
            stmt.setString(4, paiement.getStatut().name());
            stmt.setDouble(5, paiement.getMontant());
            stmt.setString(6, paiement.getIdPaiement());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du paiement", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM paiement WHERE idPaiement = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du paiement", e);
        }
    }

    @Override
    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) {
        String sql = "SELECT * FROM paiement WHERE idAbonnement = ? AND statut IN ('NON_PAYE', 'EN_RETARD') ORDER BY dateEcheance";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idAbonnement);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des impayés", e);
        }

        return paiements;
    }

    @Override
    public List<Paiement> findLastPayments(int limit) {
        String sql = "SELECT * FROM paiement WHERE datePaiement IS NOT NULL ORDER BY datePaiement DESC LIMIT ?";
        List<Paiement> paiements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des derniers paiements", e);
        }

        return paiements;
    }

    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();

        paiement.setIdPaiement(rs.getString("idPaiement"));
        paiement.setIdAbonnement(rs.getString("idAbonnement"));
        paiement.setDateEcheance(LocalDate.parse(String.valueOf(rs.getDate("dateEcheance").toLocalDate())));

        Date datePaiement = rs.getDate("datePaiement");
        if (datePaiement != null) {
            paiement.setDatePaiement(LocalDate.parse(String.valueOf(datePaiement.toLocalDate())));
        }

        String typePaiement = rs.getString("typePaiement");
        if (typePaiement != null) {
            paiement.setTypePaiement(TypePaiement.valueOf(typePaiement));
        }

        paiement.setStatut(StatutPaiement.valueOf(rs.getString("statut")));
        paiement.setMontant(rs.getDouble("montant"));

        return paiement;
    }
}