import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DrillProd {

    // Local variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;
    public int amount;
    public double pipePrice500m;

    // Reference to the next object
    // (stricte for ownership data structure)
    DrillProd next;

    // Constructor
    public DrillProd(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.isBought = false;
        this.ownership = null;
        this.amount = amount;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Price getter
    public int getPrice() {
        return price;
    }

    public static DrillProd[] initialize() {
        DrillProd[] drillProds = new DrillProd[3];

        // Dril productions initialization
        drillProds[0] = new DrillProd("TURBOWIERT",    41124, 57);
        drillProds[1] = new DrillProd("NA BŁYSK INC.", 43149, 57);
        drillProds[2] = new DrillProd("PET SHOP&BOYS", 32919, 49);

        return drillProds;
    }

    // Menu for buying drill productions
    public static void buyProd(Player player, Scanner scanner, DrillProd[] drillProds) {
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

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        char[] possibleActions = new char[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = '0';
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
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + drillProds[i].getPrice() + ANSI.RESET);
            System.out.print(" ");
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + "$" + ANSI.RESET);
            System.out.println("\n");

            // Add production to array
            //     Find the latest 'uninitialized' slot
            for (int j = 0; j < possibleActions.length; j++) {
                if (possibleActions[j] == '0') {
                    possibleActions[j] = (char) (i+1+48);
                    break;
                }
            }
        }
        System.out.println();

        // Get the action and
        // verify that the action is correct,
        // if not then wait for another
        System.out.println("Którą firmę chcesz kupić?");  
        char action = ' ';
        while (Arrays.binarySearch(possibleActions, action) < 0) {
            System.out.print("  ? ");
            action = scanner.nextLine().toUpperCase().charAt(0);
        }

        int selectedProdIndex = (int) (action-48-1);

        // Note purchase
        DrillProd tmp = player.ownedDrillProd;
        player.ownedDrillProd = drillProds[selectedProdIndex];
        player.ownedDrillProd.next = tmp;

        drillProds[selectedProdIndex].isBought = true;
        drillProds[selectedProdIndex].ownership = player;

        player.balance -= drillProds[selectedProdIndex].getPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(drillProds[selectedProdIndex].getName());
        System.out.println();
        System.out.println("Proszę podać swoją cenę na rury o długości 500m.");

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
        drillProds[selectedProdIndex].pipePrice500m = proposedPrice;

    }
}
