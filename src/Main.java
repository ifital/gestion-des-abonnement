import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initialisation de l'application...");

        try {
            ConsoleUI ui = new ConsoleUI();
            ui.demarrer();
        } catch (Exception e) {
            System.err.println("Erreur fatale lors du démarrage: " + e.getMessage());
            e.printStackTrace();
        }
    }
}