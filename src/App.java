import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Global variables
        Game.roundCount = 34;
        Game.currentRound = 1;

        // Scanner class
        Scanner scanner = new Scanner(System.in);

        // Intro
        // TODO: fancy OEL logo
        System.out.println(ANSI.PURPLE + "OEL");
        System.out.println("CR. COMP. & TRANSL. BY " + ANSI.PURPLE_BACKGROUND + ANSI.RED + " MI$ AL " + ANSI.RESET + ANSI.PURPLE);
        System.out.println("Rewritten in Java by " + ANSI.PURPLE_BACKGROUND + ANSI.RED + " Zabbum " + ANSI.RESET + ANSI.PURPLE);
        System.out.println("\nThe big game and the big money.\n");

        // Prompt for players amount
        int playerAmount = Prompt.playerAmount(scanner); // IOSC sz
        System.out.println();

        // Initialize industries
        Oilfield[] oilfields = Oilfield.initialize();
        CarsProd[] carsProds = CarsProd.initialize();
        PumpProd[] pumpProds = PumpProd.initialize();
        DrillProd[] drillProds = DrillProd.initialize();

        // Generate oil prices
        double[] oilPrices = Oil.generatePrices(Game.roundCount);
        
        // Info for player
        System.out.println("Znajdujemy się obecnie w roku:");
        System.out.println("1986\n");
        System.out.println("Gra kończy się w roku 2020");
        System.out.println("i odbędzie się przy udziale:");
        System.out.println("Proszę wpisać imiona");

        // Create players objects
        Player[] players = Prompt.playerNames(playerAmount, scanner);
        
        // Info for player
        System.out.println("Każdy gracz posiada 124321$ kapitału.\n");
        System.out.println(ANSI.YELLOW + "Wygrywa ten, kto osiągnie największy kapitał na końcu gry.");

        // Info for player about oil prices
        System.out.println(ANSI.RED + "Przewidywane ceny ropy na rynku");
        System.out.println("(Jakie trendy w kolejnych latach :)\n");

        // Draw graph
        Oil.printGraph(oilPrices, ANSI.PURPLE_BACKGROUND);
        oilPrices = Oil.reducePrices(oilPrices);

        System.out.println();

        // Play
        Game.playRound(players, scanner, oilfields, carsProds, pumpProds, drillProds);

        // Close scanner object
        scanner.close();
    }
}
