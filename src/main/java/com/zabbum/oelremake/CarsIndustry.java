package com.zabbum.oelremake;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.table.Table;

public class CarsIndustry extends AbstractIndustry {

    // Constructor
    public CarsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.industryPrice = random.nextInt(55000) + 45000;
        this.productsAmount = ((int)(this.industryPrice/10000)) * 3  + 15;
    }

    public static CarsIndustry[] initialize() {
        CarsIndustry[] carsProds = new CarsIndustry[4];

        // Cars productions initialization
        carsProds[0] = new CarsIndustry("WOZ-PRZEWOZ");
        carsProds[1] = new CarsIndustry("WAGONENSITZ");
        carsProds[2] = new CarsIndustry("WORLD CO.");
        carsProds[3] = new CarsIndustry("DRINK TANK INC.");

        return carsProds;
    }

    // Menu for buying cars productions
    public static void buyIndustry(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, TextColor.ANSI.BLUE, TextColor.ANSI.BLUE_BRIGHT,
            TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT)
            );

        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLUE));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label("SPRZEDAZ FABRYK WAGONOW"));
        titlePanel.addComponent(new Label("SALDO KONTA: " + String.valueOf(player.balance) + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "L. PROD.", "CENA");

        // Add every available cars industry to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < gameProperties.carsIndustries.length; industryIndex++) {
            if (gameProperties.carsIndustries[industryIndex].isBought) {}
            // If industry is not bought, make it possible to buy it
            industryTable.getTableModel().addRow(
                String.valueOf(industryIndex+1),
                gameProperties.carsIndustries[industryIndex].name,
                String.valueOf(gameProperties.carsIndustries[industryIndex].productsAmount),
                String.valueOf(gameProperties.carsIndustries[industryIndex].industryPrice)+"$"
                );
        }

        industryTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(industryTable.getTableModel().getRow(industryTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(industryTable);
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("KTORA FIRME CHCESZ KUPIC?"));

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex = gameProperties.tmpActionInt;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Note purchase
        gameProperties.carsIndustries[selectedIndustryIndex].isBought = true;
        gameProperties.carsIndustries[selectedIndustryIndex].ownership = player;

        player.balance -= gameProperties.carsIndustries[selectedIndustryIndex].industryPrice;

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("JESTE$ W£A$CICIELEM FABRYKI: "));
        contentPanel.addComponent(new Label(gameProperties.carsIndustries[selectedIndustryIndex].name));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("PROSZE PODAC SWOJA CENE WAGONU."));
    
        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        gameProperties.tmpActionInt = -1;
        while (gameProperties.tmpActionInt < 0 || gameProperties.tmpActionInt > 50000) {
            // Prompt for price
            contentPanel.addComponent(new EmptySpace());
            productPriceBox = new TextBox(new TerminalSize(6, 1));
            productPriceBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productPriceBox);
            contentPanel.addComponent(
                new Button("GOTOWE", new Runnable() {
                    @Override
                    public void run() {
                        gameProperties.tmpConfirm = true;
                    }
                })
            );

            // Wait for selection
            productPriceBox.takeFocus();
            Game.waitForConfirm(gameProperties);
            try {
                gameProperties.tmpActionInt = Integer.parseInt(productPriceBox.getText());
            } catch (Exception e) {
                // If a bad value has been provided
                gameProperties.tmpActionInt = -1;
            }
            
        }

        // Set the price
        gameProperties.carsIndustries[selectedIndustryIndex].productPrice = gameProperties.tmpActionInt;

        // Clean up
        contentPanel.removeAllComponents();
    }

        // Menu for buying cars
    public static void buyProduct(Player player, Scanner scanner, CarsIndustry[] pumpProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK_BRIGHT + " **   SPRZEDAŻ POMP   ** " + ANSI.RESET );
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLACK_BACKGROUND_BRIGHT + ANSI.WHITE + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (PumpsIndustry pumpProd : pumpProds) {
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
    public static void buyProduct(Player player, Scanner scanner, CarsIndustry[] carsProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.YELLOW_BACKGROUND_BRIGHT + ANSI.RED + " **   SPRZEDAŻ WAGONÓW   ** " + ANSI.RESET );
        System.out.println(ANSI.RED_BACKGROUND + ANSI.YELLOW_BRIGHT + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.RED_BACKGROUND + ANSI.YELLOW_BRIGHT + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (CarsIndustry carsProd : carsProds) {
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
        int selectedIndustryIndex = Prompt.promptInt(possibleActions, scanner);

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
        oilfields[selectedOilfieldIndex].carsAmount += selectedCarsAmount; // Place cars in the oilfield
    }
}
