import java.util.Random;
import java.util.Scanner;

public class Sabotage {
    
    // Do a sabotage
    public static void doSabotage(Player player, Scanner scanner, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK_BRIGHT + "SABOTAŻ SABOTAŻ SABOTAŻ" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Masz teraz następujące możliwości:" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLUE_BRIGHT + "1 - Zatrudniasz super agenta na pole naftowe konkurenta" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "2 - Będziesz sabotował fabrykę pomp" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLUE_BRIGHT + "3 - Możesz doprowadzić do ruiny fabrykę wagonów" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "4 - Sabotaż fabryki wierteł" + ANSI.RESET);
        System.out.println();

        char possibleActions[] = {'0', '1', '2', '3', '4'};
        int action = Prompt.promptInt(possibleActions, scanner);
        action++;

        switch (action) {
            case 0:
                // If 0 selected, return
                return;
            case 1:
                // If 1 selected, attempt oilfield sabotage
                attemptOilfieldSabotage(player, scanner, oilfields);
                break;
            default:
                break;
        }
    }

    // Attempt a oilfield sabotage
    static void attemptOilfieldSabotage(Player player, Scanner scanner, Oilfield[] oilfields) {
        System.out.println("Tu masz tylko 50% szans!");
        Random random = new Random();

        boolean isSucceed = random.nextBoolean();
        if (isSucceed) {
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED + "Hello! I'm agent Funny Happy Bear!" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED + "Które pole mam sabotować, my friend?" + ANSI.RESET);
            System.out.println();

            // Print all the oilfields and their owners
            for (int i = 0; i < oilfields.length; i++) {
                // Display oilfield info
                System.out.print(ANSI.BLACK_BACKGROUND + ANSI.RED + " " + (i+1) + ANSI.RESET);
                System.out.print("\t");
                System.out.print(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + " " + oilfields[i].getName() + " " + ANSI.RESET);
                System.out.print("\t");
                if (oilfields[i].isBought) {
                    System.out.print(oilfields[i].ownership.name);
                }
                System.out.println();
            }

            // Generate the possible actions
            char possibleActions[] = new char[oilfields.length + 1];
            for (int i = 0; i < possibleActions.length; i++) {
                if (i < oilfields.length) {
                    possibleActions[i] = (char)(i+48+1);
                }
                else {
                    possibleActions[i] = '0';
                }
            }

            // Get the action
            System.out.println("Które pole sabotować?");
            int sabotedOilfieldIndex = Prompt.promptInt(possibleActions, scanner);

            // If 0 selected, return
            if (sabotedOilfieldIndex == -1) {
                return;
            }

            // Generate fees
            int fees1 = random.nextInt(40000) + 40000;
            int fees2 = random.nextInt(40000) + 1;

            // Inform user about the costs
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED_BRIGHT + "Dane do sabotażu pola naftowego." + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED_BRIGHT + "Musisz ponieść następujące koszty:" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Opłaty, łapówki itp. = " + fees1 + "$" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Zatkania, wysadzenia itp. = " + fees2 + "$" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "W sumie = " + (fees1 + fees2) + "$" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + "Szanse powodzenia kształtują się w granicach 33%." + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "Twoje saldo\t" + player.balance + "$" + ANSI.RESET);
            System.out.println();
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Bear: \"Mam zacząć działać?\"" + ANSI.RESET);
            System.out.println("(0 - anuluj, 1 - działaj)");

            // Prompt for an action
            possibleActions = new char[]{'0', '1'};
            int action = Prompt.promptInt(possibleActions, scanner);

            // If 0 selected, return
            if (action == -1) {
                return;
            }

            // Reduce player's balance
            player.balance -= (fees1 + fees2);

            // Generate if action is succeed or not
            int isSucceedSabotage = random.nextInt(3);

            // Inform about action's result and take actions
            if (isSucceedSabotage == 2) {
                System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.GREEN_BRIGHT + "Udało się!" + ANSI.RESET);

                // Set new oilfield price
                oilfields[sabotedOilfieldIndex].setPriceSabotage(random.nextInt(50000) + 30001);
                // Set new oil amount
                oilfields[sabotedOilfieldIndex].oilAmount = random.nextInt(200000) + 1;
                // Revoke ownership
                oilfields[sabotedOilfieldIndex].isBought = false;
                oilfields[sabotedOilfieldIndex].ownership = null;
                // Revoke ability to pump oil
                oilfields[sabotedOilfieldIndex].canExtractOil = false;
                // Set new requred depth to pump oil
                oilfields[sabotedOilfieldIndex].requiredDepth = random.nextInt(4500) + 1;
                // Reset amount of pumps
                oilfields[sabotedOilfieldIndex].pumpAmount = 0;
                // Reset amount of cars
                oilfields[sabotedOilfieldIndex].carsAmount = 0;
                // Reset amount of drills
                oilfields[sabotedOilfieldIndex].drillAmount = 0;
                // Reset current digging depth
                oilfields[sabotedOilfieldIndex].currentDepth = 0;
                // Reset amount of oil pumped out
                oilfields[sabotedOilfieldIndex].oilExtracted = 0;
                // Reset amount of oil available to sell
                oilfields[sabotedOilfieldIndex].oilAvailabletoSell = 0;
            }
            System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED_BRIGHT + "Nie udało się!" + ANSI.RESET);
        }
        else {
            // Inform user about failure
            for (int i = 0; i < 12; i++) {
                System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + "Masz pecha!" + ANSI.RESET);
            }
        }
    }
}
