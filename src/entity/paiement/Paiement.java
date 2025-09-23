package entity.paiement;

import entity.enums.StatutPaiement;
import entity.enums.TypePaiement;

import javax.print.DocFlavor;
import java.util.UUID;

public class Paiement {
    private String idPaiement;
    private String idAbennement;
    private String dateEcheance;
    private String datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statutPaiement;
    private double montant;

    public Paiement(){
        this.idPaiement = UUID.randomUUID().toString();
        this.statutPaiement = StatutPaiement.NON_PAYE;
    }

    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getIdAbennement() {
        return idAbennement;
    }

    public void setIdAbennement(String idAbennement) {
        this.idAbennement = idAbennement;
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

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
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
                ", idAbennement='" + idAbennement + '\'' +
                ", dateEcheance='" + dateEcheance + '\'' +
                ", datePaiement='" + datePaiement + '\'' +
                ", typePaiement=" + typePaiement +
                ", statutPaiement=" + statutPaiement +
                ", montant=" + montant +
                '}';
    }
}
