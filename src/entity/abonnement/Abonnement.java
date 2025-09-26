package entity.abonnement;

import enums.StatutAbonnement;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {
    protected String id;
    protected String nomService;
    protected double montantMensuel;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected StatutAbonnement statut;

    public Abonnement() {
        this.id = UUID.randomUUID().toString();
        this.statut = StatutAbonnement.ACTIF;
    }

    public Abonnement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin) {
        this();
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomService() { return nomService; }
    public void setNomService(String nomService) { this.nomService = nomService; }

    public double getMontantMensuel() { return montantMensuel; }
    public void setMontantMensuel(double montantMensuel) { this.montantMensuel = montantMensuel; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutAbonnement getStatut() { return statut; }
    public void setStatut(StatutAbonnement statut) { this.statut = statut; }

    public abstract String getTypeAbonnement();

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f€/mois (%s -> %s) [%s]",
                getTypeAbonnement(), nomService, montantMensuel,
                dateDebut, dateFin, statut);
    }
}