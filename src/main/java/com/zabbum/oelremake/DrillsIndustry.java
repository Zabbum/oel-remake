package com.zabbum.oelremake;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.table.Table;

public class DrillsIndustry extends AbstractIndustry {
    // Constructor
    public DrillsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.industryPrice = random.nextInt(50000) + 10000;
        this.productsAmount = random.nextInt(((int)(this.industryPrice/10000)) * 8 + 25);
    }

    public static DrillsIndustry[] initialize() {
        DrillsIndustry[] drillProds = new DrillsIndustry[3];

        // Dril productions initialization
        drillProds[0] = new DrillsIndustry("TURBOWIERT");
        drillProds[1] = new DrillsIndustry("NA B£YSK INC.");
        drillProds[2] = new DrillsIndustry("PET SHOP&BOYS");

        return drillProds;
    }

    // Menu for buying drill productions
    public static void buyIndustry(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLUE,
            TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN, TextColor.ANSI.BLUE)
            );

        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label("SPRZEDAZ FABRYK WIERTE£"));
        titlePanel.addComponent(new Label("SALDO KONTA: " + String.valueOf(player.balance) + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "L. PROD.", "CENA");

        // Add every available drill production to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < gameProperties.drillIndustries.length; industryIndex++) {
            if (gameProperties.drillIndustries[industryIndex].isBought) {}
            // If table is not bought, make it possible to buy it
            industryTable.getTableModel().addRow(
                String.valueOf(industryIndex+1),
                gameProperties.drillIndustries[industryIndex].name,
                String.valueOf(gameProperties.drillIndustries[industryIndex].productsAmount),
                String.valueOf(gameProperties.drillIndustries[industryIndex].industryPrice)+"$"
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
        gameProperties.drillIndustries[selectedIndustryIndex].isBought = true;
        gameProperties.drillIndustries[selectedIndustryIndex].ownership = player;

        player.balance -= gameProperties.drillIndustries[selectedIndustryIndex].industryPrice;

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("JESTE$ W£A$CICIELEM FABRYKI: "));
        contentPanel.addComponent(new Label(gameProperties.drillIndustries[selectedIndustryIndex].name));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("PROSZE PODAC SWOJA CENE NA RURY O D£UGO$CI 500 M."));
    
        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        gameProperties.tmpActionInt = -1;
        while (gameProperties.tmpActionInt < 0 || gameProperties.tmpActionInt > 60000) {
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
        gameProperties.drillIndustries[selectedIndustryIndex].productPrice = gameProperties.tmpActionInt;

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Menu for buying drills
    public static void buyProduct(Player player, Scanner scanner, DrillsIndustry[] drillProds, Oilfield[] oilfields) {
        // Inform user where they are
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.WHITE + "TU MOŻESZ KUPIĆ WIERTŁA:" + ANSI.RESET );
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.WHITE + "SALDO KONTA:\t" + player.balance + "$" + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "Nr\tFirma\t\tSzt.\tCena" + ANSI.RESET);

        // Calculate how many actions are possible
        // e.g. if there is production that is not bought yet,
        // don't make it possible to buy its product.
        int possibleActionsLength = 0;
        for (DrillsIndustry drillProd : drillProds) {
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
