package entity.paiement;

import entity.enums.StatutPaiement;
import entity.enums.TypePaiement;

import javax.print.DocFlavor;
import java.util.UUID;

public class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private String dateEcheance;
    private String datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statut;
    private double montant;

    public Paiement(){
        this.idPaiement = UUID.randomUUID().toString();
        this.statut = StatutPaiement.NON_PAYE;
    }

    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(String idAbennement) {
        this.idAbonnement = idAbennement;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statutPaiement) {
        this.statut = statutPaiement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Paiement(String dateEcheance, String datePaiement, double montant) {
        this();
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement='" + idPaiement + '\'' +
                ", idAbennement='" + idAbonnement + '\'' +
                ", dateEcheance='" + dateEcheance + '\'' +
                ", datePaiement='" + datePaiement + '\'' +
                ", typePaiement=" + typePaiement +
                ", statutPaiement=" + statut +
                ", montant=" + montant +
                '}';
    }
}
