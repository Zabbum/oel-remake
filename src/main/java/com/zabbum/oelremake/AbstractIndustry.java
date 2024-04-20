package com.zabbum.oelremake;

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

public abstract class AbstractIndustry {
    protected String name;
    public int industryPrice;
    public boolean isBought;
    public Player ownership;

    public int productsAmount;
    public double productPrice;

    // Constructor
    public AbstractIndustry(String name) {
        this.name = name;
        this.isBought = false;
        this.ownership = null;
    }

    // Menu for buying industries
    public static void buyIndustry(
        // Mechanics
        Player player, GameProperties gameProperties,
        // Product type
        Class<?> industryType,
        // GUI colors
        TextColor baseForeground, TextColor baseBackground,
        TextColor editableForeground, TextColor editableBackground,
        TextColor selectedForeground, TextColor selectedBackground,
        TextColor guiBackground,
        // Texts
        String industrySale, String industryPrompt,
        String pricePrompt,
        // Values
        int maxPrice
    ) {

        // Create array of industries and products
        AbstractIndustry industries[];

        if (industryType.equals(DrillsIndustry.class)) {
            industries = gameProperties.drillsIndustries;
        } else if (industryType.equals(CarsIndustry.class)) {
            industries = gameProperties.carsIndustries;
        } else if (industryType.equals(PumpsIndustry.class)) {
            industries = gameProperties.pumpsIndustries;
        } else {
            return;
        }

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, baseForeground, baseBackground,
                editableForeground, editableBackground,
                selectedForeground, selectedBackground,
                guiBackground
            )
        );

        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(baseBackground, baseForeground));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label(industrySale));
        titlePanel.addComponent(new Label("SALDO KONTA: " + String.valueOf(player.balance) + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "L. PROD.", "CENA");

        // Add every available industry to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought) {
                // If industry is not bought, make it possible to buy it
                industryTable.getTableModel().addRow(
                    String.valueOf(industryIndex+1),
                    industries[industryIndex].name,
                    String.valueOf(industries[industryIndex].productsAmount),
                    String.valueOf(industries[industryIndex].industryPrice)+"$"
                );
            }
        }

        industryTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(industryTable.getTableModel().getRow(industryTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(industryTable);
        industryTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(industryPrompt));

        // Wait for selection
        gameProperties.tmpActionInt = -1;
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex = gameProperties.tmpActionInt;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            gameProperties.tmpActionInt = -1;
            contentPanel.removeAllComponents();
            return;
        }

        // Note purchase
        industries[selectedIndustryIndex].isBought = true;
        industries[selectedIndustryIndex].ownership = player;

        player.balance -= industries[selectedIndustryIndex].industryPrice;

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("JESTE$ W£A$CICIELEM FABRYKI: "));
        contentPanel.addComponent(new Label(gameProperties.drillsIndustries[selectedIndustryIndex].name));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(pricePrompt));

        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        gameProperties.tmpActionInt = -1;
        while (gameProperties.tmpActionInt < 0 || gameProperties.tmpActionInt > maxPrice) {
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
        industries[selectedIndustryIndex].productPrice = gameProperties.tmpActionInt;

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
    }



    // Menu for buying products
    public static void buyProduct(
        // Mechanics
        Player player, GameProperties gameProperties,
        // Product type
        Class<?> industryType,
        // GUI colors
        TextColor baseForeground, TextColor baseBackground,
        TextColor editableForeground, TextColor editableBackground,
        TextColor selectedForeground, TextColor selectedBackground,
        TextColor guiBackground, TextColor tableForeground,
        // Texts
        String hereYouCanBuy, String productAmountPrompt,
        // Values
        int maxAmount
    ) {

        // Create array of industries and products
        AbstractIndustry industries[];

        if (industryType.equals(DrillsIndustry.class)) {
            industries = gameProperties.drillsIndustries;
        } else if (industryType.equals(CarsIndustry.class)) {
            industries = gameProperties.carsIndustries;
        } else if (industryType.equals(PumpsIndustry.class)) {
            industries = gameProperties.pumpsIndustries;
        } else {
            return;
        }

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, baseForeground, baseBackground,
                editableForeground, editableBackground,
                selectedForeground, selectedBackground, 
                guiBackground
            )
        );
        
        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(baseBackground, baseForeground));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label(hereYouCanBuy));
        titlePanel.addComponent(new Label("SALDO KONTA: " + String.valueOf(player.balance) + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Create table
        Table<String> productsTable = new Table<String>("NR", "NAZWA FIRMY", "L. PROD.", "CENA");
        productsTable.setTheme(SimpleTheme.makeTheme(false, tableForeground, baseBackground,
            editableForeground, editableBackground,
            selectedForeground, selectedBackground,
            guiBackground)
            );
        
        // Add every available industry to table
        productsTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (industries[industryIndex].isBought) {
                // If industry is not bought, make it possible to buy it
                productsTable.getTableModel().addRow(
                    String.valueOf(industryIndex+1),
                    industries[industryIndex].name,
                    String.valueOf(industries[industryIndex].productsAmount),
                    String.valueOf(industries[industryIndex].productPrice)+"$"
                    );
            }
        }

        productsTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(productsTable.getTableModel().getRow(productsTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(productsTable);
        productsTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("ZAKUP Z KTOREJ FIRMY?"));

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex = gameProperties.tmpActionInt;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            gameProperties.tmpActionInt = -1;
            contentPanel.removeAllComponents();
            return;
        }

        // Clean temporary choice
        gameProperties.tmpActionInt = -1;

        // Prompt for drills amount
        contentPanel.addComponent(new Label(productAmountPrompt));

        // Prompt for product amount until provided value is valid
        TextBox productAmountBox = null;
        gameProperties.tmpActionInt = -1;
        while (gameProperties.tmpActionInt < 0 || gameProperties.tmpActionInt > maxAmount) {
            // Prompt for product amount
            contentPanel.addComponent(new EmptySpace());
            productAmountBox = new TextBox(new TerminalSize(6, 1));
            productAmountBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productAmountBox);
            contentPanel.addComponent(
                new Button("GOTOWE", new Runnable() {
                    @Override
                    public void run() {
                        gameProperties.tmpConfirm = true;
                    }
                })
            );

            // Wait for selection
            productAmountBox.takeFocus();
            Game.waitForConfirm(gameProperties);
            try {
                gameProperties.tmpActionInt = Integer.parseInt(productAmountBox.getText());
            } catch (Exception e) {
                // If a bad value has been provided
                gameProperties.tmpActionInt = -1;
            }
        }

        int selectedProductAmount = gameProperties.tmpActionInt;

        // Prompt for oilfield
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("NA KTORE POLE NAFTOWE?"));

        // Display all of the oilfields

        // Create table
        Table<String> oilfieldsTable = new Table<String>("NR", "NAZWA ", "W£ASNO$C");

        // Add every available oilfield to table
        oilfieldsTable.getTableModel().addRow("0","-","-");
        for (int oilfieldIndex = 0; oilfieldIndex < gameProperties.oilfields.length; oilfieldIndex++) {
            // If oilfield is bought, display the name
            String ownerName = "---";
            if (gameProperties.oilfields[oilfieldIndex].isBought) {
                ownerName = gameProperties.oilfields[oilfieldIndex].ownership.name;
            }
            oilfieldsTable.getTableModel().addRow(
                String.valueOf(oilfieldIndex+1),
                gameProperties.oilfields[oilfieldIndex].name,
                ownerName
                );
        }

        oilfieldsTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(oilfieldsTable.getTableModel().getRow(oilfieldsTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(oilfieldsTable);
        oilfieldsTable.takeFocus();

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedOilfieldIndex = gameProperties.tmpActionInt;

        // If 0 selected, return
        if (selectedOilfieldIndex == -1) {
            // Clean up
            gameProperties.tmpActionInt = -1;
            contentPanel.removeAllComponents();
            return;
        }

        // Take actions
        // Reduce amount of available products in industry
        industries[selectedIndustryIndex].productsAmount -= selectedProductAmount; 
        // Reduce player's balance
        player.balance -= selectedProductAmount * industries[selectedIndustryIndex].productPrice;
        if (industries[selectedIndustryIndex].ownership == player) {
            // If player is buying product from theirself, give them the money * 0.2
            player.balance += 0.2 * selectedProductAmount * industries[selectedIndustryIndex].productPrice;
        } else {
            // Give owner of industry the money
            industries[selectedIndustryIndex].ownership.balance +=
                selectedProductAmount * industries[selectedIndustryIndex].productPrice;
        }
        // Place products in the oilfield
        gameProperties.oilfields[selectedOilfieldIndex].addProductAmount(industryType, selectedProductAmount);

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
    }
}
