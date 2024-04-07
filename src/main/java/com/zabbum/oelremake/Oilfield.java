package com.zabbum.oelremake;

import java.util.Random;
import java.util.Scanner;

public class Oilfield {

    // Object variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;
    public int oilAmount;
    public boolean canExtractOil;
    public int requiredDepth;
    public int currentDepth;
    public int oilExtracted;
    public int oilAvailabletoSell;

    // Amounts of exploitation stuff
    public int carsAmount;
    public int drillAmount;
    public int pumpAmount;

    // Reference to the next object
    // (stricte for ownership data structure)
    public Oilfield next;

    // Constructor
    public Oilfield(String name) {
        Random random = new Random();

        this.name = name;
        this.price = random.nextInt(70000) + 29900;
        this.isBought = false;
        this.ownership = null;
        this.oilAmount = (this.price - random.nextInt(9999) + 1) * 10;
        this.canExtractOil = false;
        this.requiredDepth = random.nextInt(3666) + 1;
        this.currentDepth = 0;
        this.oilExtracted = 0;
        this.oilAvailabletoSell = 0;
        this.carsAmount = 0;
        this.drillAmount = 0;
        this.pumpAmount = 0;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Price getter
    public int getPrice() {
        return price;
    }

    // Price setter (ONLY FOR SABOTAGE PURPOSES)
    public void setPriceSabotage(int price) {
        this.price = price;
    }

    public static Oilfield[] initialize() {
        Oilfield[] oilfields = new Oilfield[12];

        // Oilfields initialization
        oilfields[0] = new Oilfield("JASNY GWINT");
        oilfields[1] = new Oilfield("WIELKA DZIURA");
        oilfields[2] = new Oilfield("WIERTOWISKO");
        oilfields[3] = new Oilfield("SMAK WALUTY");
        oilfields[4] = new Oilfield("MIŁA ZIEMIA");
        oilfields[5] = new Oilfield("BORUJ-BORUJ");
        oilfields[6] = new Oilfield("KRASNY POTOK");
        oilfields[7] = new Oilfield("PŁYTKIE DOŁY");
        oilfields[8] = new Oilfield("ŚLADY OLEJU");
        oilfields[9] = new Oilfield("NICZYJ GRUNT");
        oilfields[10] = new Oilfield("DZIKIE PSY");
        oilfields[11] = new Oilfield("UGORY NAFTOWE");

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
        possibleActionsLength++;

        // Create possible actions array
        // Every 'uninitialized' slot's value is '0'
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }

        // Display every available oilfield
        for (int i = 0; i < oilfields.length; i++) {
            // Display oilfield info
            System.out.print(" " + (i+1));
            System.out.print("\t");
            System.out.print(ANSI.BLUE + " " + oilfields[i].getName() + " " + ANSI.RESET);
            System.out.print("\t");
            if (oilfields[i].isBought) {
                // If oilfield is bought, do not display it
                System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT + ANSI.CYAN_BRIGHT + "---" + ANSI.RESET);
            }
            else {
                System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT + ANSI.BLACK + " " + oilfields[i].getPrice() + ANSI.RESET);
                System.out.print(" ");
                System.out.print(ANSI.PURPLE_BACKGROUND_BRIGHT + ANSI.BLACK + "$" + ANSI.RESET);

                // Add oilfield to array
                //     Find the latest 'uninitialized' slot
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                        break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println();

        // Get the action
        int selectedOilfieldIndex = Prompt.promptInt(possibleActions, scanner);

        // If 0 selected, return
        if (selectedOilfieldIndex == -1) {
            return;
        }

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
