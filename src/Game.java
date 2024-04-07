import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {
    public static int roundCount;
    public static int currentRound;

    public static void playRound(Player[] players, Scanner scanner, Oilfield[] oilfields, CarsProd[] carsProds, PumpProd[] pumpProds, DrillProd[] drillProds) {
        Game.currentRound += 1;

        for (Player player : players) {
            // Display the main menu
            mainMenu(player, scanner);

            // Get the action
            char action = ' ';
            char[] possibleActions = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
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
                    // Attempt sabotage
                    Sabotage.doSabotage(player, scanner, oilfields, pumpProds, carsProds, drillProds);
                }
                case 'J' -> {
                    // Change prices
                    ChangePrices.menu(player, scanner, pumpProds, carsProds, drillProds);
                }
                default -> {
                    System.out.println(ANSI.RED + "No value provided. This could be an error.\n" + ANSI.RESET);
                }
            }
        }
        endRound(players, scanner, oilfields, carsProds, pumpProds, drillProds);
    }

    // Actions to take at end of the round
    static void endRound(Player[] players, Scanner scanner, Oilfield[] oilfields, CarsProd[] carsProds, PumpProd[] pumpProds, DrillProd[] drillProds) {
        Random random = new Random();

        // Actions for every player
        for (Player player : players) {
            
            // Oilfields overview
            for (Oilfield oilfield : oilfields) {
                // If user is not owner of the field, move on to the next
                if (oilfield.ownership != player) {
                    continue;
                }

                // If oilfield is able to pump oil
                if (oilfield.canExtractOil) {
                    System.out.println(ANSI.BLACK_BACKGROUND + ANSI.YELLOW + "Pole naftowe:" + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + oilfield.getName() + ANSI.RESET);
                    System.out.println(ANSI.BLACK_BACKGROUND + ANSI.YELLOW + "Właściciel pola: "+ ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + player.name + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Rok: " + ANSI.WHITE + (Game.currentRound + 1986) + ANSI.RESET);

                    // TODO: Finish this
                }

                // If oilfield is not able to pump oil
                else {
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Wiercenie na polu:" + ANSI.RESET);
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN_BRIGHT + oilfield.getName() + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.PURPLE_BRIGHT + "Właśność: " + ANSI.BLACK + player.name + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK + "Twoi ludzie z pola naftowego meldują co następuje:" + ANSI.RESET);
                    System.out.println();

                    // If no drills, inform user
                    if (oilfield.drillAmount <= 0) { 
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Wiercenia niemożliwe - brak wierteł!" + ANSI.RESET);
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Trzeba coś przedsięwziąć!" + ANSI.RESET);
                        System.out.println();
                    }

                    // If there are drills, drill
                    else {
                        oilfield.drillAmount -= 500;
                        oilfield.currentDepth += 500 - (random.nextInt(30)+1);
                    }

                    // In both cases
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Aktualna głębokość wierceń: " + oilfield.currentDepth + "m" + ANSI.RESET);
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Jeszcze starczy ci na: " + oilfield.drillAmount + "m" + ANSI.RESET);
                    
                    // If reached the point that makes it available to extract oil,
                    // take actions
                    if (oilfield.currentDepth >= oilfield.requiredDepth) {
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Trysnęło!!!" + ANSI.RESET);
                        oilfield.canExtractOil = true;
                    }
                    System.out.println();
                }
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
