package dao.impl;

import config.DatabaseConfig;
import dao.AbonnementDAO;
import entity.abonnement.Abonnement;
import entity.abonnement.AbonnementAvecEngagement;
import entity.abonnement.AbonnementSansEngagement;
import entity.enums.StatutAbonnement;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AbonnementDAOImpl implements AbonnementDAO {
    @Override
    public void create(Abonnement abonnement) {
        String sql = "INSERT INTO abonnement (id, nomService, montantMensuel, dateDebut, dateFin, statut, typeAbonnement, dureeEngagementMois) VALUE (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql); ){
            stmt.setString(1, abonnement.getId());
            stmt.setString(2, abonnement.getNomService());
            stmt.setDouble(3, abonnement.getMontantMensuel());
            stmt.setDate(4, Date.valueOf(abonnement.getDateDebut()));
            stmt.setDate(5, abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
            stmt.setString(6, abonnement.getStatut().name());
            stmt.setString(7, abonnement.getTypeAbonnement());

            if (abonnement instanceof AbonnementAvecEngagement){
                stmt.setInt(8, ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois());
            }else {
                stmt.setNull(8, Types.INTEGER);
            }
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Erreur lors de la création de l'abonnement", e);
        }

    }

    @Override
    public Optional<Abonnement> findById(String id) {
        String sql = "SELECT * FROM abonnement WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAbonnement(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'abonnement", e);
        }
    }


    @Override
    public List<Abonnement> findAll() {
        String sql = "SELECT * FROM abonnement ORDER BY nomService";
        List<Abonnement> abonnements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                abonnements.add(mapResultSetToAbonnement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recuperation des abonnements", e);
        }

        return abonnements;
    }

    @Override
    public void update(Abonnement abonnement) {
        String sql = "UPDATE abonnement SET nomService = ?, montantMensuel = ?, dateDebut = ?, dateFin = ?, statut = ?, dureeEngagementMois = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abonnement.getNomService());
            stmt.setDouble(2, abonnement.getMontantMensuel());
            stmt.setDate(3, Date.valueOf(abonnement.getDateDebut()));
            stmt.setDate(4, abonnement.getDateFin() != null ? Date.valueOf(abonnement.getDateFin()) : null);
            stmt.setString(5, abonnement.getStatut().name());

            if (abonnement instanceof AbonnementAvecEngagement) {
                stmt.setInt(6, ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setString(7, abonnement.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'abonnment", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM abonnement WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l abonnement", e);
        }
    }

    @Override
    public List<Abonnement> findActiveSubscriptions() {
        String sql = "SELECT * FROM abonnement WHERE statut = 'ACTIF' ORDER BY nomService";
        List<Abonnement> abonnements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                abonnements.add(mapResultSetToAbonnement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des abonnements actifs", e);
        }

        return abonnements;
    }

    @Override
    public List<Abonnement> findByType(String type) {
        String sql = "SELECT * FROM abonnement WHERE typeAbonnement = ? ORDER BY nomService";
        List<Abonnement> abonnements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                abonnements.add(mapResultSetToAbonnement(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par type", e);
        }

        return abonnements;
    }

    private Abonnement mapResultSetToAbonnement(ResultSet rs) throws SQLException {
        String type = rs.getString("typeAbonnement");
        Abonnement abonnement;

        if ("AVEC_ENGAGEMENT".equals(type)) {
            AbonnementAvecEngagement avecEngagement = new AbonnementAvecEngagement();
            avecEngagement.setDureeEngagementMois(rs.getInt("dureeEngagementMois"));
            abonnement = avecEngagement;
        } else {
            abonnement = new AbonnementSansEngagement();
        }

        abonnement.setId(rs.getString("id"));
        abonnement.setNomService(rs.getString("nomService"));
        abonnement.setMontantMensuel(rs.getDouble("montantMensuel"));
        abonnement.setDateDebut(rs.getDate("dateDebut").toLocalDate());

        Date dateFin = rs.getDate("dateFin");
        if (dateFin != null) {
            abonnement.setDateFin(dateFin.toLocalDate());
        }

        abonnement.setStatut(StatutAbonnement.valueOf(rs.getString("statut")));

        return abonnement;
    }
}
