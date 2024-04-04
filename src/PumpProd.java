import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PumpProd {

    // Local variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;
    public int amount;
    public double pumpPrice;

    // Reference to the next object
    // (stricte for ownership data structure)
    PumpProd next;

    // Constructor
    public PumpProd(String name, int price, int amount) {
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

    public static PumpProd[] initialize() {
        PumpProd[] pumpProds = new PumpProd[2];

        // Pump productions initialization
        pumpProds[0] = new PumpProd("ZASSANICKI GMBH", 59829, 60);
        pumpProds[1] = new PumpProd("DR PUMPENER",     54754, 60);

        return pumpProds;
    }

    // Menu for buying pump productions
    public static void buyProd(Player player, Scanner scanner, PumpProd[] pumpProds) {
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "KUPOWANIE FABRYK POMP" + ANSI.RESET);
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

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        char[] possibleActions = new char[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = '0';
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
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + " " + pumpProds[i].getPrice() + ANSI.RESET);
            System.out.print(" ");
            System.out.print(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "$" + ANSI.RESET);
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
        System.out.println("Którą fabrykę chcesz kupić?");  
        char action = ' ';
        while (Arrays.binarySearch(possibleActions, action) < 0) {
            System.out.print("  ? ");
            action = scanner.nextLine().toUpperCase().charAt(0);
        }

        int selectedProd = (int) (action-48-1);

        // Note purchase
        PumpProd tmp = player.ownedPumpProd;
        player.ownedPumpProd = pumpProds[selectedProd];
        player.ownedPumpProd.next = tmp;

        pumpProds[selectedProd].isBought = true;
        pumpProds[selectedProd].ownership = player;

        player.balance -= pumpProds[selectedProd].getPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(pumpProds[selectedProd].getName());
        System.out.println();
        System.out.println("Proszę podać swoją cenę pompy.");

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
        pumpProds[selectedProd].pumpPrice = proposedPrice;

    }
}
