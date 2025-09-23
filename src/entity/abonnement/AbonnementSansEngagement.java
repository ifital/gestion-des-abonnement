package entity.abonnement;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    public AbonnementSansEngagement() {
        super();
    }

    public AbonnementSansEngagement(String nomService, double montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin) {
        super(nomService, montantMensuel, dateDebut, dateFin);
    }

    @Override
    public String getTypeAbonnement() {
        return "SANS_ENGAGEMENT";
    }
}