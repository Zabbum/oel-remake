import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class PumpProd extends Industry {

    // Local variables
    private int industryPrice;
    public int amount;
    public double productPrice;

    // Reference to the next object
    // (stricte for ownership data structure)
    public PumpProd next;

    // Constructor
    public PumpProd(String name) {
        super(name);
        Random random = new Random();

        this.industryPrice = random.nextInt(80000) + 36000;
        this.amount = (int)(this.industryPrice/10000) * 7 + 25;
    }

    // Industry price getter
    public int getIndustryPrice() {
        return industryPrice;
    }

    // Industry price setter (ONLY FOR SABOTAGE PURPOSES)
    public void setIndustryPriceSabotage(int industryPrice) {
        this.industryPrice = industryPrice;
    }

    public static PumpProd[] initialize() {
        PumpProd[] pumpProds = new PumpProd[2];

        // Pump productions initialization
        pumpProds[0] = new PumpProd("ZASSANICKI GMBH");
        pumpProds[1] = new PumpProd("DR PUMPENER");

        return pumpProds;
    }

    // Menu for buying pump productions
    public static void buyIndustry(Player player, Scanner scanner, PumpProd[] pumpProds) {
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "KUPOWANIE FABRYK POMP" + ANSI.RESET);
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "Saldo konta:" + ANSI.RESET + " " +
                           ANSI.YELLOW_BACKGROUND + ANSI.BLUE + player.balance + "$" + ANSI.RESET);
        System.out.println();

        // Calculate how many actions are possible
        // e.g. if there is production that is already
        // bought, don't make it possible to buy it.
        int possibleActionsLength = 0;
        for (PumpProd pumpProd : pumpProds) {
            if (!pumpProd.isBought) {
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

        // Display every available pump production
        for (int i = 0; i < pumpProds.length; i++) {
            // If pump production is bought, skip it
            if (pumpProds[i].isBought) {
                System.out.println("\n");
                continue;
            }

            // Display production info
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + (i+1) + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + pumpProds[i].getName() + " " + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + pumpProds[i].amount+ ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + pumpProds[i].getIndustryPrice() + ANSI.RESET);
            System.out.print(" ");
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "$" + ANSI.RESET);
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
        PumpProd tmp = player.ownedPumpProd;
        player.ownedPumpProd = pumpProds[selectedIndustryIndex];
        player.ownedPumpProd.next = tmp;

        pumpProds[selectedIndustryIndex].isBought = true;
        pumpProds[selectedIndustryIndex].ownership = player;

        player.balance -= pumpProds[selectedIndustryIndex].getIndustryPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(pumpProds[selectedIndustryIndex].getName());
        System.out.println();
        System.out.println("Proszę podać swoją cenę pompy.");

        // Get price from user
        double proposedPrice = -1;
        while (proposedPrice < 0 || proposedPrice > 50000) {
            System.out.print("  ? ");
            try {
                proposedPrice = scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next();
            }
        }

        // Set the price
        pumpProds[selectedIndustryIndex].productPrice = proposedPrice;
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
        System.out.println("Ile pomp kupujesz? (limit 15)");
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
        pumpProds[selectedIndustryIndex].amount -= selectedPumpAmount; // Reduce amount of available pumps in industry
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
}
