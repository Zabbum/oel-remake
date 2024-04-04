import java.util.Arrays;
import java.util.Scanner;

public class Game {
    public static int roundCount;
    public static int currentRound;

    public static void playRound(Player[] players, Scanner scanner,
                                 Oilfield[] oilfields, CarsProd[] carsProds,
                                 PumpProd[] pumpProds, DrillProd[] drillProds
                                 ) {
        for (Player player : players) {
            // Display the main menu
            mainMenu(player, scanner);

            // Get the action
            char action = ' ';
            char[] possibleActions = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};
            // Verify that the action is correct,
            // if not then wait for another
            while (Arrays.binarySearch(possibleActions, action) < 0) {
                String input = scanner.nextLine().toUpperCase();
                if (input.length() >= 1) {
                    action = input.charAt(0);
                }
            }

            // Redirect to the valid menu
            if (action == 'A') {
                // Drill productions
                DrillProd.buyProd(player, scanner, drillProds);
            }
            if (action == 'B') {
                // Pump productions
                PumpProd.buyProd(player, scanner, pumpProds);
            }
            if (action == 'C') {
                // Cars productions
                CarsProd.buyProd(player, scanner, carsProds);
            }
        }
    }

    public static void mainMenu(Player player, Scanner scanner) {
        // Display the menu
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.PURPLE + "DECYZJA NALEŻY DO CIEBIE!" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "Gracz: " + player.name + " $= " + player.balance + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLUE_BACKGROUND + " KUPOWANIE " + ANSI.RESET);
        System.out.println("A = Fabryki wierteł");
        System.out.println("B = Zakłady pomp");
        System.out.println("C = Firmy wagonowe");
        System.out.println("D = Pola naftowe");
        System.out.println("E = Wiertła");
        System.out.println("F = Pompy");
        System.out.println("G = Wagony");
        System.out.println();
        System.out.println(ANSI.BLUE_BACKGROUND + " POZOSTAŁE MOŻLIWOŚCI " + ANSI.RESET);
        System.out.println("H = Następny gracz");
        System.out.println("I = Próba sabotażu");
        System.out.println("J = Zmiana ceny");
        System.out.println("K = Przeczekanie");
        System.out.println();
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK + "PRZYCIŚNIJ DOWOLNY KLAWISZ" + ANSI.RESET);
    }
}
