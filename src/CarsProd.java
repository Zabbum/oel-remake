import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarsProd {

    // Local variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;
    public int amount;
    public double carPrice;

    // Reference to the next object
    // (stricte for ownership data structure)
    public CarsProd next;

    // Constructor
    public CarsProd(String name, int price, int amount) {
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

    public static CarsProd[] initialize() {
        CarsProd[] carsProds = new CarsProd[4];

        // Cars productions initialization
        carsProds[0] = new CarsProd("WÓZ-PRZEWÓZ",     56700, 30);
        carsProds[1] = new CarsProd("WAGONENSITZ",     94830, 42);
        carsProds[2] = new CarsProd("WORLD CO.",       62596, 33);
        carsProds[3] = new CarsProd("DRINK TANK INC.", 81217, 39);

        return carsProds;
    }

    // Menu for buying cars productions
    public static void buyProd(Player player, Scanner scanner, CarsProd[] carsProds) {
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
        char[] possibleActions = new char[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = '0';
        }

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
            System.out.print(ANSI.WHITE_BACKGROUND + ANSI.BLUE + " " + carsProds[i].getPrice() + ANSI.RESET);
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
        CarsProd tmp = player.ownedCarsProd;
        player.ownedCarsProd = carsProds[selectedProdIndex];
        player.ownedCarsProd.next = tmp;

        carsProds[selectedProdIndex].isBought = true;
        carsProds[selectedProdIndex].ownership = player;

        player.balance -= carsProds[selectedProdIndex].getPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem fabryki:");
        System.out.println(carsProds[selectedProdIndex].getName());
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
        carsProds[selectedProdIndex].carPrice = proposedPrice;

    }
}
