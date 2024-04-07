package com.zabbum.oelremake;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Sabotage {
    
    // Do a sabotage
    public static void doSabotage(Player player, Scanner scanner, Oilfield[] oilfields, PumpProd[] pumpProds, CarsProd[] carsProds, DrillProd[] drillProds) {
        // Inform user where they are
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK_BRIGHT + "SABOTAŻ SABOTAŻ SABOTAŻ" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Masz teraz następujące możliwości:" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLUE_BRIGHT + "1 - Zatrudniasz super agenta na pole naftowe konkurenta" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "2 - Będziesz sabotował fabrykę pomp" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLUE_BRIGHT + "3 - Możesz doprowadzić do ruiny fabrykę wagonów" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "4 - Sabotaż fabryki wierteł" + ANSI.RESET);
        System.out.println();

        String possibleActions[] = {"0", "1", "2", "3", "4"};
        int action = Prompt.promptInt(possibleActions, scanner);
        action++;

        switch (action) {
            case 0 -> {
                // If 0 selected, return
                return;
            }
            case 1 -> {
                // If 1 selected, attempt oilfield sabotage
                attemptOilfieldSabotage(player, scanner, oilfields);
            }
            case 2 -> {
                // If 2 selected, attempt pump industry sabotage
                attemptPumpIndustrySabotage(player, scanner, pumpProds);
            }
            case 3 -> {
                // If 3 selected attempt cars industry sabotage
                attemptCarsIndustrySabotage(player, scanner, carsProds);
            }
            case 4 -> {
                // If 3 selected attempt drill industry sabotage
                attemptDrillIndustrySabotage(player, scanner, drillProds);
            }
            default -> {}
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
            String possibleActions[] = new String[oilfields.length + 1];
            for (int i = 0; i < possibleActions.length; i++) {
                if (i < oilfields.length) {
                    possibleActions[i] = String.valueOf(i+1);
                }
                else {
                    possibleActions[i] = "0";
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
            possibleActions = new String[]{"0", "1"};
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
            else {
                // Inform user about failure
                for (int i = 0; i < 12; i++) {
                    System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.RED_BRIGHT + "Nie udało się!" + ANSI.RESET);
                }
            }
        }
    }

    // Attempt a pump industry sabotage
    static void attemptPumpIndustrySabotage(Player player, Scanner scanner, PumpProd[] pumpProds) {
        Random random = new Random();

        // Create possible actions array
        int possibleActionsLength = 1;
        for (PumpProd pumpProd : pumpProds) {
            if (pumpProd.isBought) {
                possibleActionsLength += 1;
            }
        }
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Inform user
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + "Którą z następujących firm chcesz zasabotować?" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "Twoje saldo\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "\tFabryka\t\tCena\tWłasność" + ANSI.RESET);
        
        for (int i = 0; i < pumpProds.length; i++) {
            System.out.print(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + (i+1) + "\t");
            System.out.print(pumpProds[i].getName() + "\t");
            System.out.print(pumpProds[i].getIndustryPrice() + "$\t");
            if (pumpProds[i].isBought) {
                System.out.print(pumpProds[i].ownership.name);

                // Find latest uninitialized slot in array and change its value
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                    }
                }
            }
            System.out.println(ANSI.RESET);
        }

        // Get the action
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "Która firma?" + ANSI.RESET);
        int sabotedPumpProdIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (sabotedPumpProdIndex == -1) {
            return;
        }

        // Generate result and Take actions
        int finalResult = sabotageGenerateResult() + 100;
        player.balance -= pumpProds[sabotedPumpProdIndex].getIndustryPrice() * finalResult / 100;
        if (finalResult < 100) {
            pumpProds[sabotedPumpProdIndex].ownership = null;
            pumpProds[sabotedPumpProdIndex].isBought = false;
            pumpProds[sabotedPumpProdIndex].setIndustryPriceSabotage(random.nextInt(100000)+1);
            pumpProds[sabotedPumpProdIndex].productPrice = 0;
            pumpProds[sabotedPumpProdIndex].amount = (int)(pumpProds[sabotedPumpProdIndex].getIndustryPrice()/10000);
            return;
        }
    }

    // Attempt a car industry sabotage
    static void attemptCarsIndustrySabotage(Player player, Scanner scanner, CarsProd[] carsProds) {
        Random random = new Random();

        // Create possible actions array
        int possibleActionsLength = 1;
        for (CarsProd carsProd : carsProds) {
            if (carsProd.isBought) {
                possibleActionsLength += 1;
            }
        }
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Inform user
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.YELLOW_BRIGHT + "Którą z następujących fabryk wagonów będziesz sabotował?" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.CYAN_BRIGHT + "Twoje saldo\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "\tFabryka\t\tCena\tWłasność" + ANSI.RESET);

        for (int i = 0; i < carsProds.length; i++) {
            System.out.print(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + (i+1) + "\t");
            System.out.print(carsProds[i].getName() + "\t");
            System.out.print(carsProds[i].getIndustryPrice() + "$\t");
            if (carsProds[i].isBought) {
                System.out.print(carsProds[i].ownership.name);

                // Find latest uninitialized slot in array and change its value
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                    }
                }
            }
            System.out.println(ANSI.RESET);
        }

        // Get the action
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "Która firma?" + ANSI.RESET);
        int sabotedCarsProdIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (sabotedCarsProdIndex == -1) {
            return;
        }

        // Generate result and Take actions
        int finalResult = sabotageGenerateResult() + 100;
        player.balance -= carsProds[sabotedCarsProdIndex].getIndustryPrice() * finalResult / 100;
        if (finalResult < 100) {
            carsProds[sabotedCarsProdIndex].ownership = null;
            carsProds[sabotedCarsProdIndex].isBought = false;
            carsProds[sabotedCarsProdIndex].setIndustryPriceSabotage(random.nextInt(200000)+1);
            carsProds[sabotedCarsProdIndex].productPrice = 0;
            carsProds[sabotedCarsProdIndex].amount = (int)(carsProds[sabotedCarsProdIndex].getIndustryPrice()/10000);
            return;
        }
    }

    // Attempt a drill industry sabotage
    static void attemptDrillIndustrySabotage(Player player, Scanner scanner, DrillProd[] drillProds) {
        Random random = new Random();

        // Create possible actions array
        int possibleActionsLength = 1;
        for (DrillProd drillProd : drillProds) {
            if (drillProd.isBought) {
                possibleActionsLength += 1;
            }
        }
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Inform user
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + "Którą z następujących fabryk wierteł będziesz sabotował?" + ANSI.RESET);
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.CYAN_BRIGHT + "Twoje saldo\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "\tFabryka\t\tCena\tWłasność" + ANSI.RESET);

        for (int i = 0; i < drillProds.length; i++) {
            System.out.print(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.BLACK + (i+1) + "\t");
            System.out.print(drillProds[i].getName() + "\t");
            System.out.print(drillProds[i].getIndustryPrice() + "$\t");
            if (drillProds[i].isBought) {
                System.out.print(drillProds[i].ownership.name);

                // Find latest uninitialized slot in array and change its value
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                    }
                }
            }
            System.out.println(ANSI.RESET);
        }

        // Get the action
        System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLACK_BRIGHT + "Która firma?" + ANSI.RESET);
        int sabotedDrillProdIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (sabotedDrillProdIndex == -1) {
            return;
        }


        // Generate result and Take actions
        int finalResult = sabotageGenerateResult() + 100;
        player.balance -= drillProds[sabotedDrillProdIndex].getIndustryPrice() * finalResult / 100;
        if (finalResult < 100) {
            drillProds[sabotedDrillProdIndex].ownership = null;
            drillProds[sabotedDrillProdIndex].isBought = false;
            drillProds[sabotedDrillProdIndex].setIndustryPriceSabotage(random.nextInt(100000)+1);
            drillProds[sabotedDrillProdIndex].productPrice = 0;
            drillProds[sabotedDrillProdIndex].amount = (int)(drillProds[sabotedDrillProdIndex].getIndustryPrice()/10000);
            return;
        }
    }

    static int sabotageGenerateResult() {
        int[] results = new int[]{50, -20, 40, -10, 30, -30, 10, -40, 20, -50};
        int resultIndex = 0;

        try {
            while (System.in.available() == 0) {
                if (resultIndex < 9) {
                    resultIndex++;
                }
                else {
                    resultIndex = 0;
                }
                
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println(ANSI.BLACK + ANSI.WHITE + "Tu padnie rozstrzygnięcie!" + ANSI.RESET);
                System.out.println(ANSI.BLACK + ANSI.WHITE + "Wciśnij ENTER w odpowiednim momencie" + ANSI.RESET);
                System.out.println(results[resultIndex]);
                Thread.sleep(60);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Inform user about the result
        System.out.print(ANSI.BLACK_BACKGROUND + ANSI.WHITE + "Wynik sabotażu: ");
        if (results[resultIndex] > 0) {
            System.out.print("+");
        }
        System.out.println(results[resultIndex] + "%" + ANSI.RESET);
        if (results[resultIndex] > 0) {
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.RED_BRIGHT + "Dopłacasz do interesu!" + ANSI.RESET);
        }
        else {
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.GREEN_BRIGHT + "Udane przedsięwzięcie!" + ANSI.RESET);
        }

        return results[resultIndex];
    }
}
