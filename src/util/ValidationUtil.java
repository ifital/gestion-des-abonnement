package util;


import entity.enums.StatutAbonnement;
import entity.enums.StatutPaiement;
import entity.enums.TypePaiement;

import java.util.Arrays;
import java.util.Optional;

public class ValidationUtil {

    public static boolean isValidAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr.trim());
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidInteger(String intStr) {
        if (intStr == null || intStr.trim().isEmpty()) {
            return false;
        }

        try {
            int value = Integer.parseInt(intStr.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidServiceName(String serviceName) {
        return serviceName != null && !serviceName.trim().isEmpty() && serviceName.trim().length() >= 2;
    }

    public static Optional<StatutAbonnement> parseStatutAbonnement(String statutStr) {
        if (statutStr == null || statutStr.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(StatutAbonnement.valueOf(statutStr.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<StatutPaiement> parseStatutPaiement(String statutStr) {
        if (statutStr == null || statutStr.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(StatutPaiement.valueOf(statutStr.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<TypePaiement> parseTypePaiement(String typeStr) {
        if (typeStr == null || typeStr.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(TypePaiement.valueOf(typeStr.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static void displayStatutAbonnementOptions() {
        System.out.println("Statuts disponibles:");
        Arrays.stream(StatutAbonnement.values())
                .forEach(statut -> System.out.println("- " + statut.name()));
    }

    public static void displayTypePaiementOptions() {
        System.out.println("Types de paiement disponibles:");
        Arrays.stream(TypePaiement.values())
                .forEach(type -> System.out.println("- " + type.name()));
    }

    public static void displayStatutPaiementOptions() {
        System.out.println("Statuts de paiement disponibles:");
        Arrays.stream(StatutPaiement.values())
                .forEach(statut -> System.out.println("- " + statut.name()));
    }
}


