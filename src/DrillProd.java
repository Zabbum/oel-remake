import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class DrillProd {

    // Local variables
    private String name;
    private int industryPrice;
    public boolean isBought;
    public Player ownership;
    public int amount;
    public double productPrice;

    // Reference to the next object
    // (stricte for ownership data structure)
    public DrillProd next;

    // Constructor
    public DrillProd(String name) {
        Random random = new Random();

        this.name = name;
        this.industryPrice = random.nextInt(50000) + 10000;
        this.isBought = false;
        this.ownership = null;
        this.amount = random.nextInt(((int)(this.industryPrice/10000)) * 8 + 25);
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Industry price getter
    public int getIndustryPrice() {
        return industryPrice;
    }

    // Industry price setter (ONLY FOR SABOTAGE PURPOSES)
    public void setIndustryPriceSabotage(int industryPrice) {
        this.industryPrice = industryPrice;
    }

    public static DrillProd[] initialize() {
        DrillProd[] drillProds = new DrillProd[3];

        // Dril productions initialization
        drillProds[0] = new DrillProd("TURBOWIERT");
        drillProds[1] = new DrillProd("NA BŁYSK INC.");
        drillProds[2] = new DrillProd("PET SHOP&BOYS");

        return drillProds;
    }

    // Menu for buying drill productions
    public static void buyIndustry(Player player, Scanner scanner, DrillProd[] drillProds) {
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "SPRZEDAŻ WIERTEŁ" + ANSI.RESET);
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "Saldo konta:" + ANSI.RESET + " " +
                           ANSI.WHITE_BACKGROUND + ANSI.BLUE + player.balance + "$" + ANSI.RESET);
        System.out.println();

        // Calculate how many actions are possible
        // e.g. if there is production that is already
        // bought, don't make it possible to buy it.
        int possibleActionsLength = 0;
        for (DrillProd drillProd : drillProds) {
            if (!drillProd.isBought) {
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

        // Display every available Drill production
        for (int i = 0; i < drillProds.length; i++) {
            // If Drill production is bought, skip it
            if (drillProds[i].isBought) {
                System.out.println("\n");
                continue;
            }

            // Display production info
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + (i+1) + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + drillProds[i].getName() + " " + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + drillProds[i].amount+ ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + drillProds[i].getIndustryPrice() + ANSI.RESET);
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
        System.out.println("Którą firmę chcesz kupić?");
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Note purchase
        DrillProd tmp = player.ownedDrillProd;
        player.ownedDrillProd = drillProds[selectedIndustryIndex];
        player.ownedDrillProd.next = tmp;

        drillProds[selectedIndustryIndex].isBought = true;
        drillProds[selectedIndustryIndex].ownership = player;

        player.balance -= drillProds[selectedIndustryIndex].getIndustryPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(drillProds[selectedIndustryIndex].getName());
        System.out.println();
        System.out.println("Proszę podać swoją cenę na rury o długości 500m.");

        // Get price from user
        double proposedPrice = -1;
        while (proposedPrice < 0 || proposedPrice > 60000) {
            System.out.print("  ? ");
            try {
                proposedPrice = scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next();
            }
        }

        // Set the price
        drillProds[selectedIndustryIndex].productPrice = proposedPrice;
    }

    // Menu for buying drills
    public static void buyProduct(Player player, Scanner scanner, DrillProd[] drillProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.WHITE + "TU MOŻESZ KUPIĆ WIERTŁA:" + ANSI.RESET );
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.WHITE + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (DrillProd drillProd : drillProds) {
            if (drillProd.isBought) {
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

        // Display every single Drill production
        for (int i = 0; i < drillProds.length; i++) {
            // Display production info
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + (i+1));
            System.out.print("\t");
            System.out.print(" " + drillProds[i].getName() + " ");
            System.out.print("\t");
            System.out.print(" " + drillProds[i].amount);
            System.out.print("\t");
            System.out.print(" ");

            if (drillProds[i].isBought) { // If industry is not bought, do not display price
                System.out.print(drillProds[i].productPrice);
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
        System.out.println(ANSI.RED + "Zakup z której firmy?" + ANSI.RESET);  
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            return;
        }

        // Prompt for drills amount
        System.out.println("Ile rur 500 metrowych kupujesz? (limit 10)");
        int selectedDrillAmount = -1;
        while (selectedDrillAmount < 0 ||
               selectedDrillAmount > drillProds[selectedIndustryIndex].amount ||
               selectedDrillAmount > 10) {
            
                System.out.print("  ? ");
                selectedDrillAmount = scanner.nextInt();
        }
        if (selectedDrillAmount == 0) {
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
        drillProds[selectedIndustryIndex].amount -= selectedDrillAmount; // Reduce amount of available drills in industry
        player.balance -= selectedDrillAmount * drillProds[selectedIndustryIndex].productPrice; // Reduce player's balance
        if (drillProds[selectedIndustryIndex].ownership == player) {
            // If player is buying product from theirself, give them the money * 0.2
            player.balance += 0.2 * selectedDrillAmount * drillProds[selectedIndustryIndex].productPrice;
        }
        else {
            // Give owner of industry the money
            drillProds[selectedIndustryIndex].ownership.balance += selectedDrillAmount * drillProds[selectedIndustryIndex].productPrice;
        }

        if (selectedOilfieldIndex == -1) {
            // If player entered 0, so bought drills and
            // didn't place them anywhere, return
            return;
        }
        oilfields[selectedOilfieldIndex].drillAmount += selectedDrillAmount * 500; // Place drills in the oilfield
    }
}
