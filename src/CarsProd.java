import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class CarsProd {

    // Local variables
    private String name;
    private int industryPrice;
    public boolean isBought;
    public Player ownership;
    public int amount;
    public double productPrice;

    // Reference to the next object
    // (stricte for ownership data structure)
    public CarsProd next;

    // Constructor
    public CarsProd(String name) {
        Random random = new Random();

        this.name = name;
        this.industryPrice = random.nextInt(55000) + 45000;
        this.isBought = false;
        this.ownership = null;
        this.amount = ((int)(this.industryPrice/10000)) * 3  + 15;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Price getter
    public int getIndustryPrice() {
        return industryPrice;
    }

    public static CarsProd[] initialize() {
        CarsProd[] carsProds = new CarsProd[4];

        // Cars productions initialization
        carsProds[0] = new CarsProd("WÓZ-PRZEWÓZ");
        carsProds[1] = new CarsProd("WAGONENSITZ");
        carsProds[2] = new CarsProd("WORLD CO.");
        carsProds[3] = new CarsProd("DRINK TANK INC.");

        return carsProds;
    }

    // Menu for buying cars productions
    public static void buyIndustry(Player player, Scanner scanner, CarsProd[] carsProds) {
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "SPRZEDAŻ WIERTEŁ" + ANSI.RESET);
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "Saldo konta:" + ANSI.RESET + " " +
                           ANSI.WHITE_BACKGROUND + ANSI.BLUE + player.balance + "$" + ANSI.RESET);
        System.out.println();

        // Calculate how many actions are possible
        // e.g. if there is production that is already
        // bought, don't make it possible to buy it.
        int possibleActionsLength = 0;
        for (CarsProd carsProd : carsProds) {
            if (!carsProd.isBought) {
                possibleActionsLength += 1;
            }
        }

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }
        possibleActionsLength++;

        // Display every available cars production
        for (int i = 0; i < carsProds.length; i++) {
            // If cars production is bought, skip it
            if (carsProds[i].isBought) {
                System.out.println("\n");
                continue;
            }

            // Display production info
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + (i+1) + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + carsProds[i].getName() + " " + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + carsProds[i].amount+ ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + carsProds[i].getIndustryPrice() + ANSI.RESET);
            System.out.print(" ");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "$" + ANSI.RESET);
            System.out.println("\n");

            // Add production to array
            //     Find the latest 'uninitialized' slot
            for (int j = 0; j < possibleActions.length; j++) {
                if (possibleActions[j].equals("0")) {
                    possibleActions[j] = String.valueOf(i+1);
                    break;
                }
            }
        }
        System.out.println();

        // Get the action
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Note purchase
        CarsProd tmp = player.ownedCarsProd;
        player.ownedCarsProd = carsProds[selectedIndustryIndex];
        player.ownedCarsProd.next = tmp;

        carsProds[selectedIndustryIndex].isBought = true;
        carsProds[selectedIndustryIndex].ownership = player;

        player.balance -= carsProds[selectedIndustryIndex].getIndustryPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(carsProds[selectedIndustryIndex].getName());
        System.out.println();
        System.out.println("Proszę podać swoją cenę za wagon.");

        // Get price from user
        double proposedPrice = -1;
        while (proposedPrice < 0) {
            System.out.print("  ? ");
            try {
                proposedPrice = scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next();
            }
        }

        // Set the price
        carsProds[selectedIndustryIndex].productPrice = proposedPrice;
    }

        // Menu for buying pumps
    public static void buyProduct(Player player, Scanner scanner, PumpProd[] pumpProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK_BRIGHT + " **   SPRZEDAŻ POMP   ** " + ANSI.RESET );
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (PumpProd pumpProd : pumpProds) {
            if (pumpProd.isBought) {
                possibleActionsLength += 1;
            }
        }
        possibleActionsLength++;

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Display every single Pump production
        for (int i = 0; i < pumpProds.length; i++) {
            // Display production info
            System.out.print(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + " " + (i+1));
            System.out.print("\t");
            System.out.print(" " + pumpProds[i].getName() + " ");
            System.out.print("\t");
            System.out.print(" " + pumpProds[i].amount);
            System.out.print("\t");
            System.out.print(" ");

            if (pumpProds[i].isBought) { // If industry is not bought, do not display price
                System.out.print(pumpProds[i].productPrice);
                System.out.print(" ");
                System.out.print("$");

                // Add production to array
                //     Find the latest 'uninitialized' slot
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                        break;
                    }
                }
            }
            else {
                System.out.print("---");
            }
            
            System.out.println(ANSI.RESET + "\n");
        }

        // Get the action
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Prompt for pumps amount
        System.out.println("Ile rur pomp kupujesz? (limit 15)");
        int selectedPumpAmount = -1;
        while (selectedPumpAmount < 0 ||
               selectedPumpAmount > pumpProds[selectedIndustryIndex].amount ||
               selectedPumpAmount > 15) {
            
                System.out.print("  ? ");
                selectedPumpAmount = scanner.nextInt();
        }
        if (selectedPumpAmount == 0) {
            return;
        }

        // Prompt for oilfield

        // Display all of the oilfields
        for (int j = 0; j < oilfields.length; j++) {
            // Display index
            System.out.print((j+1) + "\t");
            // Display name and ownership, if is bought
            if (oilfields[j].isBought) {
                System.out.print(oilfields[j].getName() + "\t" + oilfields[j].ownership.name);
            }
            System.out.println();
        }
        // Ask for a number
        int selectedOilfieldIndex = -2;
        while (selectedOilfieldIndex < -1 || selectedOilfieldIndex > oilfields.length - 1) {
            System.out.print("  ? ");
            selectedOilfieldIndex = scanner.nextInt() - 1;
        }

        // Take actions
        pumpProds[selectedIndustryIndex].amount -= selectedPumpAmount; // Reduce amount of available drills in industry
        player.balance -= selectedPumpAmount * pumpProds[selectedIndustryIndex].productPrice; // Reduce player's balance
        if (pumpProds[selectedIndustryIndex].ownership == player) {
            // If player is buying product from theirself, give them the money * 0.2
            player.balance += 0.2 * selectedPumpAmount * pumpProds[selectedIndustryIndex].productPrice;
        }
        else {
            // Give owner of industry the money
            pumpProds[selectedIndustryIndex].ownership.balance += selectedPumpAmount * pumpProds[selectedIndustryIndex].productPrice;
        }

        if (selectedOilfieldIndex == -1) {
            // If player entered 0, so bought pumps and
            // didn't place them anywhere, return
            return;
        }
        oilfields[selectedOilfieldIndex].drillAmount += selectedPumpAmount; // Place pumps in the oilfield
    }

    // Menu for buying cars
    public static void buyProduct(Player player, Scanner scanner, CarsProd[] carsProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.YELLOW_BACKGROUND_BRIGHT + ANSI.RED + " **   SPRZEDAŻ WAGONÓW   ** " + ANSI.RESET );
        System.out.println(ANSI.RED_BACKGROUND + ANSI.YELLOW_BRIGHT + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.RED_BACKGROUND + ANSI.YELLOW_BRIGHT + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (CarsProd carsProd : carsProds) {
            if (carsProd.isBought) {
                possibleActionsLength += 1;
            }
        }
        possibleActionsLength++;

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Display every single car production
        for (int i = 0; i < carsProds.length; i++) {
            // Display production info
            System.out.print(ANSI.RED_BACKGROUND + ANSI.YELLOW_BRIGHT + " " + (i+1));
            System.out.print("\t");
            System.out.print(" " + carsProds[i].getName() + " ");
            System.out.print("\t");
            System.out.print(" " + carsProds[i].amount);
            System.out.print("\t");
            System.out.print(" ");

            if (carsProds[i].isBought) { // If industry is not bought, do not display price
                System.out.print(carsProds[i].productPrice);
                System.out.print(" ");
                System.out.print("$");

                // Add production to array
                //     Find the latest 'uninitialized' slot
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                        break;
                    }
                }
            }
            else {
                System.out.print("---");
            }
            
            System.out.println(ANSI.RESET + "\n");
        }

        // Get the action and
        // verify that the action is correct,
        // if not then wait for another
        System.out.println(ANSI.RED + "Zakup z której firmy?" + ANSI.RESET);  
        char action = ' ';
        Arrays.sort(possibleActions);
        while (Arrays.binarySearch(possibleActions, action) < 0) {
            System.out.print("  ? ");
            action = scanner.nextLine().toUpperCase().charAt(0);
        }
        int selectedIndustryIndex = (int) (action-48-1);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Prompt for pumps amount
        System.out.println("Ile wagonów kupujesz? (limit 15)");
        int selectedCarsAmount = -1;
        while (selectedCarsAmount < 0 ||
               selectedCarsAmount > carsProds[selectedIndustryIndex].amount ||
               selectedCarsAmount > 15) {
            
                System.out.print("  ? ");
                selectedCarsAmount = scanner.nextInt();
        }
        if (selectedCarsAmount == 0) {
            return;
        }

        // Prompt for oilfield

        // Display all of the oilfields
        for (int j = 0; j < oilfields.length; j++) {
            // Display index
            System.out.print((j+1) + "\t");
            // Display name and ownership, if is bought
            if (oilfields[j].isBought) {
                System.out.print(oilfields[j].getName() + "\t" + oilfields[j].ownership.name);
            }
            System.out.println();
        }
        // Ask for a number
        int selectedOilfieldIndex = -2;
        while (selectedOilfieldIndex < -1 || selectedOilfieldIndex > oilfields.length - 1) {
            System.out.print("  ? ");
            selectedOilfieldIndex = scanner.nextInt() - 1;
        }

        // Take actions
        carsProds[selectedIndustryIndex].amount -= selectedCarsAmount; // Reduce amount of available cars in industry
        player.balance -= selectedCarsAmount * carsProds[selectedIndustryIndex].productPrice; // Reduce player's balance
        if (carsProds[selectedIndustryIndex].ownership == player) {
            // If player is buying product from theirself, give them the money * 0.2
            player.balance += 0.2 * selectedCarsAmount * carsProds[selectedIndustryIndex].productPrice;
        }
        else {
            // Give owner of industry the money
            carsProds[selectedIndustryIndex].ownership.balance += selectedCarsAmount * carsProds[selectedIndustryIndex].productPrice;
        }

        if (selectedOilfieldIndex == -1) {
            // If player entered 0, so bought pumps and
            // didn't place them anywhere, return
            return;
        }
        oilfields[selectedOilfieldIndex].drillAmount += selectedCarsAmount; // Place cars in the oilfield
    }
}
