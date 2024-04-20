package com.zabbum.oelremake;

import java.util.regex.Pattern;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.table.Table;

public class ChangePrices {
    public static void menu(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.RED,
                TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.RED
            )
        );

        // Inform user
        contentPanel.addComponent(new Label("JAKA CENE BEDZIESZ ZMIENIAC?"));
        contentPanel.addComponent(new EmptySpace());

        // Reset temporary select option
        gameProperties.tmpActionInt = -1;

        // Display options
        Component firstButton = new Button("CENY POMP",
            () -> {
                gameProperties.tmpActionInt = 0;
                gameProperties.tmpConfirm = true;
            }
        );
        contentPanel.addComponent(firstButton);
        ((Interactable)firstButton).takeFocus();

        contentPanel.addComponent(new Button("CENY WAGONOW",
            () -> {
                gameProperties.tmpActionInt = 1;
                gameProperties.tmpConfirm = true;
            }
        ));

        contentPanel.addComponent(new Button("CENY WIERTE£",
            () -> {
                gameProperties.tmpActionInt = 2;
                gameProperties.tmpConfirm = true;
            }
        ));

        // Wait for response
        Game.waitForConfirm(gameProperties);
        contentPanel.removeAllComponents();

        int action = gameProperties.tmpActionInt;
        gameProperties.tmpActionInt = -1;

        switch (action) {
            case 0 -> {
                return;
            }
            case 1 -> {
                pumps(player, gameProperties);
            }
            case 2 -> {
                cars(player, gameProperties);
            }
            case 3 -> {
                drills(player, gameProperties);
            }
            default -> {
            }
        }
    }
    
    // Change pumps prices
    static void pumps(Player player, GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.pumpsIndustries;

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "CENA");

        // Add every available industry to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought) {
                // If industry is not bought, make it possible to buy it
                industryTable.getTableModel().addRow(
                    String.valueOf(industryIndex+1),
                    industries[industryIndex].name,
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

        // Set a new price
        double proposedPrice = gameProperties.tmpActionInt;
        gameProperties.drillsIndustries[selectedIndustryIndex].productPrice = proposedPrice;

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
    }

    // Change cars prices
    static void cars(Player player, GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.carsIndustries;

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "CENA");

        // Add every available industry to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought) {
                // If industry is not bought, make it possible to buy it
                industryTable.getTableModel().addRow(
                    String.valueOf(industryIndex+1),
                    industries[industryIndex].name,
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

        // Set a new price
        double proposedPrice = gameProperties.tmpActionInt;
        gameProperties.carsIndustries[selectedIndustryIndex].productPrice = proposedPrice;

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
    }

    // Change drills prices
    static void drills(Player player, GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.drillsIndustries;

        // Create table
        Table<String> industryTable = new Table<String>("NR", "NAZWA FIRMY", "CENA");

        // Add every available industry to table
        industryTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought) {
                // If industry is not bought, make it possible to buy it
                industryTable.getTableModel().addRow(
                    String.valueOf(industryIndex+1),
                    industries[industryIndex].name,
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

        // Set a new price
        double proposedPrice = gameProperties.tmpActionInt;
        gameProperties.drillsIndustries[selectedIndustryIndex].productPrice = proposedPrice;

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
    }

    // Generate possible actions
    static String[] generatePossibleActions(int possibleActionsLength, AbstractIndustry[] industries, Player player) {
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }
        for (int i = 0; i < industries.length; i++) {
            if (industries[i].ownership == player) {
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i+1);
                        break;
                    }
                }
            }
        }
        return possibleActions;
    }
}
