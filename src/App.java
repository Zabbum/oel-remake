public class App {
    public static void main(String[] args) {
        // Intro
        System.out.println("OEL");
        System.out.println("The big game and the big money.\n");

        // Prompt for players amount
        int playerAmount = Prompt.playerAmount(); // IOSC sz
        System.out.println();

        // Initialize industries
        Oilfield[] oilfields = Oilfield.initialize();
        CarsProd[] carsProds = CarsProd.initialize();
        PumpProd[] pumpProds = PumpProd.initialize();
        DrillProd[] drillProds = DrillProd.initialize();
        
        // Info for player
        System.out.println("Znajdujemy się obecnie w roku:");
        System.out.println("1986\n");
        System.out.println("Gra kończy się w roku 2020");
        System.out.println("i odbędzie się przy udziale:");
        System.out.println("Proszę wpisać imiona");
        
        System.out.println("Każdy gracz posiada 124321$ kapitału.\n");
        System.out.println("Wygrywa ten, kto osiągnie największy kapitał na końcu gry.");
        
    }
}
