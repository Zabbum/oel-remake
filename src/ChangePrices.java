import java.util.Scanner;

public class ChangePrices {
    public static void menu(Player player, Scanner scanner, PumpProd[] pumpProds, CarsProd[] carsProds, DrillProd[] drillProds) {
        // Inform user
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Jaką cenę będziesz zmieniać?" + ANSI.RESET);
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "1\tCeny pomp" + ANSI.RESET);
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "2\tCeny wagonów" + ANSI.RESET);
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "3\tCeny wierteł" + ANSI.RESET);
        
        // Get the action
        String[] possibleActions = new String[]{"1", "2", "3", "0"};
        int action = Prompt.promptInt(possibleActions, scanner);
        action++;

        switch (action) {
            case 0 -> {
                return;
            }
            case 1 -> {
                pumps(player, scanner, pumpProds);
            }
            case 2 -> {
                cars(player, scanner, carsProds);
            }
            case 3 -> {

            }
            default -> {
            }
        }
    }
    
    // Change pumps prices
    static void pumps(Player player, Scanner scanner, PumpProd[] pumpProds) {
        // Inform user
        int possibleActionsLength = 1;

        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Nr\tFirma\t\tCena pompy\tWłaściciel" + ANSI.RESET);
        for (int i = 0; i < pumpProds.length; i++) {
            System.out.print(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + (i+1) + "\t");
            System.out.print(pumpProds[i].getName() + "\t");
            System.out.print(pumpProds[i].productPrice + "\t\t");
            if (pumpProds[i].isBought) {
                System.out.print(pumpProds[i].ownership.name);
                if (pumpProds[i].ownership == player) {
                    possibleActionsLength++;
                }
            }
            System.out.println(ANSI.RESET);
        }

        String[] possibleActions = generatePossibleActions(possibleActionsLength, pumpProds, player);

        // Get the action
        System.out.println();
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Która firma?" + ANSI.RESET);
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Set a new price
        double proposedPrice = 50001;
        while (proposedPrice > 50000) {
            proposedPrice = Prompt.promptPrice(scanner);
        }
        pumpProds[selectedIndustryIndex].productPrice = proposedPrice;
    }

    // Change cars prices
    static void cars(Player player, Scanner scanner, CarsProd[] carsProds) {
        // Inform user
        int possibleActionsLength = 1;

        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Nr\tFirma\t\tCena wagonu\tWłaściciel" + ANSI.RESET);
        for (int i = 0; i < carsProds.length; i++) {
            System.out.print(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + (i+1) + "\t");
            System.out.print(carsProds[i].getName() + "\t");
            System.out.print(carsProds[i].productPrice + "\t\t");
            if (carsProds[i].isBought) {
                System.out.print(carsProds[i].ownership.name);
                if (carsProds[i].ownership == player) {
                    possibleActionsLength++;
                }
            }
            System.out.println(ANSI.RESET);
        }

        String[] possibleActions = generatePossibleActions(possibleActionsLength, carsProds, player);

        // Get the action
        System.out.println();
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Która firma?" + ANSI.RESET);
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Set a new price
        double proposedPrice = Prompt.promptPrice(scanner);
        carsProds[selectedIndustryIndex].productPrice = proposedPrice;
    }

    // Change drills prices
    static void drills(Player player, Scanner scanner, DrillProd[] drillProds) {
        // Inform user
        int possibleActionsLength = 1;

        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Nr\tFirma\t\tCena wiertła\tWłaściciel" + ANSI.RESET);
        for (int i = 0; i < drillProds.length; i++) {
            System.out.print(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + (i+1) + "\t");
            System.out.print(drillProds[i].getName() + "\t");
            System.out.print(drillProds[i].productPrice + "\t\t");
            if (drillProds[i].isBought) {
                System.out.print(drillProds[i].ownership.name);
                if (drillProds[i].ownership == player) {
                    possibleActionsLength++;
                }
            }
            System.out.println(ANSI.RESET);
        }

        String[] possibleActions = generatePossibleActions(possibleActionsLength, drillProds, player);

        // Get the action
        System.out.println();
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "Która firma?" + ANSI.RESET);
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Set a new price
        double proposedPrice = 60001;
        while (proposedPrice > 60000) {
            proposedPrice = Prompt.promptPrice(scanner);
        }
        drillProds[selectedIndustryIndex].productPrice = proposedPrice;
    }

    // Generate possible actions
    static String[] generatePossibleActions(int possibleActionsLength, Industry[] industries, Player player) {
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }
        for (int i = 0; i < industries.length; i++) {
            if (industries[i].ownership == player) {
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                        break;
                    }
                }
            }
        }
        return possibleActions;
    }
}
