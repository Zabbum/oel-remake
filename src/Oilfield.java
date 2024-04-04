import java.util.Arrays;
import java.util.Scanner;

public class Oilfield {

    // Private variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;

    // Reference to the next object
    // (stricte for ownership data structure)
    Oilfield next;

    // Constructor
    public Oilfield(String name, int price) {
        this.name = name;
        this.price = price;
        this.isBought = false;
        this.ownership = null;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Price getter
    public int getPrice() {
        return price;
    }

    public static Oilfield[] initialize() {
        Oilfield[] oilfields = new Oilfield[12];

        // Oilfields initialization
        oilfields[0] = new Oilfield("JASNY GWINT",    42889);
        oilfields[1] = new Oilfield("WIELKA DZIURA",  87842);
        oilfields[2] = new Oilfield("WIERTOWISKO",    92706);
        oilfields[3] = new Oilfield("SMAK WALUTY",    88622);
        oilfields[4] = new Oilfield("MIŁA ZIEMIA",    43086);
        oilfields[5] = new Oilfield("BORUJ-BORUJ",    84250);
        oilfields[6] = new Oilfield("KRASNY POTOK",   87949);
        oilfields[7] = new Oilfield("PŁYTKIE DOŁY",   97598);
        oilfields[8] = new Oilfield("ŚLADY OLEJU",    55396);
        oilfields[9] = new Oilfield("NICZYJ GRUNT",   94501);
        oilfields[10] = new Oilfield("DZIKIE PSY",    87149);
        oilfields[11] = new Oilfield("UGORY NAFTOWE", 68383);

        return oilfields;
    }

    public static void buyField(Player player, Scanner scanner, Oilfield[] oilfields) {
        System.out.println(ANSI.YELLOW_BACKGROUND_BRIGHT + ANSI.PURPLE_BRIGHT + "WYPRZEDAŻ PÓL NAFTOWYCH" + ANSI.RESET);
        System.out.println(ANSI.YELLOW_BACKGROUND_BRIGHT + ANSI.PURPLE_BRIGHT + "Saldo konta:" + ANSI.RESET + " " +
                           ANSI.YELLOW_BACKGROUND_BRIGHT + ANSI.PURPLE_BRIGHT + player.balance + "$" + ANSI.RESET);
        System.out.println();

        // Calculate how many actions are possible
        // e.g. if there is field that is already
        // bought, don't make it possible to buy it.
        int possibleActionsLength = 0;
        for (Oilfield oilfield : oilfields) {
            if (!oilfield.isBought) {
                possibleActionsLength += 1;
            }
        }

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        char[] possibleActions = new char[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = '0';
        }

        // Display every available oilfield
        for (int i = 0; i < oilfields.length; i++) {
            // If oilfield is bought, skip it
            if (oilfields[i].isBought) {
                System.out.println("\n");
                continue;
            }

            // Display production info
            System.out.print(" " + (i+1));
            System.out.print("\t");
            System.out.print(ANSI.BLUE + " " + oilfields[i].getName() + " " + ANSI.RESET);
            System.out.print("\t");
            System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT + ANSI.BLACK + " " + oilfields[i].getPrice() + ANSI.RESET);
            System.out.print(" ");
            System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT + ANSI.BLACK + "$" + ANSI.RESET);
            System.out.println();

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
        System.out.println("Którą pole chcesz wykupić?");  
        char action = ' ';
        while (Arrays.binarySearch(possibleActions, action) < 0) {
            System.out.print("  ? ");
            action = scanner.nextLine().toUpperCase().charAt(0);
        }

        int selectedOilfieldIndex = (int) (action-48-1);

        // Note purchase
        Oilfield tmp = player.ownedOilfield;
        player.ownedOilfield = oilfields[selectedOilfieldIndex];
        player.ownedOilfield.next = tmp;

        oilfields[selectedOilfieldIndex].isBought = true;
        oilfields[selectedOilfieldIndex].ownership = player;

        player.balance -= oilfields[selectedOilfieldIndex].getPrice();

        // Inform user about purchase
        System.out.println("Jesteś właścicielem pola:");
        System.out.println(oilfields[selectedOilfieldIndex].getName());

    }
}
