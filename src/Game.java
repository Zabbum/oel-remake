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
            System.out.println();
            
            // Redirect to the valid menu
            switch (action) {
                case 'A' -> {
                    // Drill productions
                    DrillProd.buyIndustry(player, scanner, drillProds);
                }
                case 'B' -> {
                    // Pump productions
                    PumpProd.buyIndustry(player, scanner, pumpProds);
                }
                case 'C' -> {
                    // Cars productions
                    CarsProd.buyIndustry(player, scanner, carsProds);
                }
                case 'D' -> {
                    // Oilfields
                    Oilfield.buyField(player, scanner, oilfields);
                }
                case 'E' -> {
                    // Drills
                    DrillProd.buyProduct(player, scanner, drillProds, oilfields);
                }
                case 'F' -> {
                    // Pumps
                    PumpProd.buyProduct(player, scanner, pumpProds, oilfields);
                }
                case 'G' -> {
                    // Cars
                    CarsProd.buyProduct(player, scanner, carsProds, oilfields);
                }
                case 'H' -> {
                    // Pass
                }
                case 'I' -> {
                    // Sabotage
                    Sabotage.doSabotage(player, scanner, oilfields);
                }
                default -> {
                    System.out.println(ANSI.RED + "No value provided. This could be an error.\n" + ANSI.RESET);
                }
            }

            Game.currentRound += 1;
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

    public static void oelLogo() {
        // Display the oel logo

        // 1. row
        System.out.print(ANSI.RED_BACKGROUND);
        for (int i = 0; i < 40; i++) {
            System.out.print(" ");
        }
        System.out.println(ANSI.RESET);

        // 2. and 3. rows
        for (int i = 0; i < 2; i++) {
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 12; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 12; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.println(ANSI.RESET);
        }
        
        // 4. 5. 6. 7. 8. 9. and 10. rows
        for (int i = 0; i < 7; i++) {
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 6; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.println(ANSI.RESET);
        }

        // 11. 12. and 13. rows
        for (int i = 0; i < 3; i++) {
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 6; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 8; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 5; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.println(ANSI.RESET);            
        }

        // 14. 15. 16. 17. 18. 19. and 20. rows
        for (int i = 0; i < 7; i++) {
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 6; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND);
            for (int j = 0; j < 10; j++) {
                System.out.print(" ");
            }
            System.out.println(ANSI.RESET);
        }

        // 21. 22. and 23. rows
        for (int i = 0; i < 3; i++) {
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 12; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 12; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.print(ANSI.BLUE_BACKGROUND_BRIGHT);
            for (int j = 0; j < 12; j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI.RED_BACKGROUND + " ");
            System.out.println(ANSI.RESET);
        }

        // 24. row
        System.out.print(ANSI.RED_BACKGROUND);
        for (int i = 0; i < 40; i++) {
            System.out.print(" ");
        }
        
        System.out.println(ANSI.RESET);
    }
}
