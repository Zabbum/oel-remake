package com.zabbum.oelremake;

import java.util.List;
import java.util.Random;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;

public class Sabotage {
    
    // Do a sabotage
    public static void doSabotage(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT
            )
        );

        // Display sabotage shade
        for (int i = 0; i < 23; i++) {
            contentPanel.addComponent(new Label(" ".repeat(i+1)+"S A B O T A Z"));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        contentPanel.removeAllComponents();

        // Blue button theme
        Theme blueButton = SimpleTheme.makeTheme(false,
            TextColor.ANSI.BLUE, TextColor.ANSI.BLACK_BRIGHT,
            TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLUE,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLACK_BRIGHT
        );
        // Yellow button theme
        Theme yellowButton = SimpleTheme.makeTheme(false,
            TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT,
            TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLACK_BRIGHT
        );

        // Inform user what can they do
        contentPanel.addComponent(new Label("MASZ TERAZ NASTEPUJACE MOZLIWOSCI"));
        contentPanel.addComponent(new EmptySpace());

        // Reset temporary select option
        gameProperties.tmpActionInt = -1;

        // Display options
        Component firstButton = new Button("ZATRUDNIASZ SUPER AGENTA NA POLE NAFTOWE KONKURENTA",
            () -> {
                gameProperties.tmpActionInt = 0;
                gameProperties.tmpConfirm = true;
            }
        ).setTheme(blueButton);
        contentPanel.addComponent(firstButton);
        ((Interactable)firstButton).takeFocus();

        contentPanel.addComponent(new Button("BEDZIESZ SABOTOWA£ FABRYKE POMP",
            () -> {
                gameProperties.tmpActionInt = 1;
                gameProperties.tmpConfirm = true;
            }
        ).setTheme(yellowButton));

        contentPanel.addComponent(new Button("MOZESZ DOPROWADZIC DO RUINY FABRYKE WAGONOW",
            () -> {
                gameProperties.tmpActionInt = 2;
                gameProperties.tmpConfirm = true;
            }
        ).setTheme(blueButton));

        contentPanel.addComponent(new Button("SABOTAZ FABRYKI WIERTE£",
            () -> {
                gameProperties.tmpActionInt = 3;
                gameProperties.tmpConfirm = true;
            }
        ).setTheme(yellowButton));

        // Wait for response
        Game.waitForConfirm(gameProperties);
        contentPanel.removeAllComponents();

        // Redirect to valid menu
        switch (gameProperties.tmpActionInt) {
            case 0 -> {
                gameProperties.tmpActionInt = -1;
                attemptOilfieldSabotage(player, gameProperties);
            }
            case 1 -> {
                gameProperties.tmpActionInt = -1;
                attemptPumpIndustrySabotage(player, gameProperties);
            }
            case 2 -> {
                gameProperties.tmpActionInt = -1;
                attemptCarsIndustrySabotage(player, gameProperties);
            }
            case 3 -> {
                gameProperties.tmpActionInt = -1;
                attemptDrillIndustrySabotage(player, gameProperties);
            }
            default -> {
                gameProperties.tmpActionInt = -1;
            }
        }
    }

    // Attempt a oilfield sabotage
    static void attemptOilfieldSabotage(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT
            )
        );

        contentPanel.addComponent(new Label("TU MASZ TYLKO 50% SZANS!"));
        contentPanel.addComponent(new EmptySpace());

        Button confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Prepare new graphical settings
        contentPanel.removeAllComponents();

        // Generate is succeed
        Random random = new Random();
        boolean isSucceed = random.nextBoolean();
        
        // If action is succeed
        if (isSucceed) {
            // Setup colors
            gameProperties.window.setTheme(
                SimpleTheme.makeTheme(false,
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT,
                    TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.WHITE,
                    TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                    TextColor.ANSI.BLACK_BRIGHT
                )
            );

            contentPanel.addComponent(new Label("HELLO! I'M AGENT FUNNY HAPPY BEAR")
                .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT))
            );
            contentPanel.addComponent(new Label("KTORE POLE MAM SABOTOWAC, MY FRIEND?")
                .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT))
            );
            contentPanel.addComponent(new EmptySpace());

            // Display all of the oilfields

            // Create table
            Table<String> oilfieldsTable = new Table<String>("NR", "NAZWA", "W£ASNO$C");

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

            // Generate fees
            int fees1 = random.nextInt(40000) + 40000;
            int fees2 = random.nextInt(40000) + 1;

            // Inform user about the costs
            contentPanel.removeAllComponents();
            contentPanel.addComponent(new Label("DANE DO SABOTAZU POLA NAFTOWEGO -")
                .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT))
            );
            contentPanel.addComponent(new Label("MUSISZ PONIE$C NASTEPUJACE KOSZTY:")
                .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT))
            );

            Panel costPanel = new Panel(new GridLayout(3));

            costPanel.addComponent(new Label("OP£ATY, £APOWKI ITP."));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees1) + " $"));

            costPanel.addComponent(new Label("ZATKANIA WYSADZENIA ITP."));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees2) + " $"));

            costPanel.addComponent(new Label("W SUMIE"));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees1 + fees2) + " $"));

            contentPanel.addComponent(costPanel);

            contentPanel.addComponent(new Label("SZANSE POWODZENIA KSZTALTUJA SIE W GRANICACH 33%.")
                .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT))
            );

            contentPanel.addComponent(new Label("TWOJE SALDO = " + player.balance + "$")
                .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT))
            );

            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(new Label("BEAR \'MAM ZACZAC DZIA£AC?\'"));

            Panel buttonPanel = new Panel(new GridLayout(2));

            Button declineButton = new Button("NIE", () -> {
                // Clean up
                gameProperties.tmpActionInt = -1;
                contentPanel.removeAllComponents();
                return;
            });
            buttonPanel.addComponent(declineButton);
            buttonPanel.addComponent(new Button("TAK", () -> {
                gameProperties.tmpConfirm = true;
            }));

            contentPanel.addComponent(buttonPanel);
            contentPanel.addComponent(new EmptySpace());

            Game.waitForConfirm(gameProperties);

            //Reduce player's balance
            player.balance -= (fees1 + fees2);

            // Generate if action is succeed or not
            int isSucceedSabotage = random.nextInt(3);

            // Animated element
            Component animatedLabel = new Label("SABOTAZ UDANY")
                .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));

            contentPanel.addComponent(animatedLabel);

            // Generating animation
            for (int i = 0; i < 50; i++) {
                if (i%2 == 1) {
                    ((Label)animatedLabel).setText("SABOTAZ NIEUDANY");
                    animatedLabel.setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
                }
                else {
                    ((Label)animatedLabel).setText("SABOTAZ UDANY");
                    animatedLabel.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Take actions based on sabotage succession
            if (isSucceedSabotage == 2) {
                // Inform user about status
                ((Label)animatedLabel).setText("UDA£O SIE");
                animatedLabel.setTheme(new SimpleTheme(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));

                // Set new oilfield price
                gameProperties.oilfields[selectedOilfieldIndex].price = random.nextInt(50000) + 30001;
                // Set new oil amount
                gameProperties.oilfields[selectedOilfieldIndex].oilAmount = random.nextInt(200000) + 1;
                // Revoke ownership
                gameProperties.oilfields[selectedOilfieldIndex].isBought = false;
                gameProperties.oilfields[selectedOilfieldIndex].ownership = null;
                // Revoke ability to pump oil
                gameProperties.oilfields[selectedOilfieldIndex].canExtractOil = false;
                // Set new requred depth to pump oil
                gameProperties.oilfields[selectedOilfieldIndex].requiredDepth = random.nextInt(4500) + 1;
                // Reset amount of pumps
                gameProperties.oilfields[selectedOilfieldIndex].pumpsAmount = 0;
                // Reset amount of cars
                gameProperties.oilfields[selectedOilfieldIndex].carsAmount = 0;
                // Reset amount of drills
                gameProperties.oilfields[selectedOilfieldIndex].drillsAmount = 0;
                // Reset current digging depth
                gameProperties.oilfields[selectedOilfieldIndex].currentDepth = 0;
                // Reset amount of oil pumped out
                gameProperties.oilfields[selectedOilfieldIndex].oilExtracted = 0;
                // Reset amount of oil available to sell
                gameProperties.oilfields[selectedOilfieldIndex].oilAvailabletoSell = 0;

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                // Inform user about status
                ((Label)animatedLabel).setText("NIE UDA£O SIE");
                animatedLabel.setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
            }

            
        }

        // Clean up
        gameProperties.tmpActionInt = -1;
        contentPanel.removeAllComponents();
        return;
    }

    // Attempt a pump industry sabotage
    static void attemptPumpIndustrySabotage(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT
            )
        );

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(new Label("KTORA Z NASTEPUJACYCH FIRM CHCESZ ZASABOTOWAC?"));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable = new Table<String>("NR", "NAZWA", "CENA", "W£ASNO$C");

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < gameProperties.pumpsIndustries.length; industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (gameProperties.pumpsIndustries[industryIndex].isBought) {
                ownerName = gameProperties.oilfields[industryIndex].ownership.name;
            }

            industriesTable.getTableModel().addRow(
                String.valueOf(industryIndex+1),
                gameProperties.pumpsIndustries[industryIndex].name,
                String.valueOf(gameProperties.pumpsIndustries[industryIndex].industryPrice) + " $",
                ownerName
            );
        }

        industriesTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

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

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.balance -= gameProperties.pumpsIndustries[selectedIndustryIndex].industryPrice * finalResult;
        if (finalResult < 1) {
            gameProperties.pumpsIndustries[selectedIndustryIndex].ownership = null;
            gameProperties.pumpsIndustries[selectedIndustryIndex].isBought = false;
            gameProperties.pumpsIndustries[selectedIndustryIndex].industryPrice = random.nextInt(100000)+1;
            gameProperties.pumpsIndustries[selectedIndustryIndex].productPrice = 0;
            gameProperties.pumpsIndustries[selectedIndustryIndex].productsAmount =
                (int)(gameProperties.pumpsIndustries[selectedIndustryIndex].industryPrice/10000);
        }
    }

    // Attempt a car industry sabotage
    static void attemptCarsIndustrySabotage(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT
            )
        );

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(new Label("KTORA Z NASTEPUJACYCH FABRYK WAGONOW BEDZIESZ SABOTOWA£?")
            .setTheme(new SimpleTheme(TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT)));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable = new Table<String>("NR", "NAZWA", "CENA", "W£ASNO$C");

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < gameProperties.carsIndustries.length; industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (gameProperties.carsIndustries[industryIndex].isBought) {
                ownerName = gameProperties.oilfields[industryIndex].ownership.name;
            }

            industriesTable.getTableModel().addRow(
                String.valueOf(industryIndex+1),
                gameProperties.carsIndustries[industryIndex].name,
                String.valueOf(gameProperties.carsIndustries[industryIndex].industryPrice) + " $",
                ownerName
            );
        }

        industriesTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

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

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.balance -= gameProperties.carsIndustries[selectedIndustryIndex].industryPrice * finalResult;
        if (finalResult < 1) {
            gameProperties.carsIndustries[selectedIndustryIndex].ownership = null;
            gameProperties.carsIndustries[selectedIndustryIndex].isBought = false;
            gameProperties.carsIndustries[selectedIndustryIndex].industryPrice = random.nextInt(200000)+1;
            gameProperties.carsIndustries[selectedIndustryIndex].productPrice = 0;
            gameProperties.carsIndustries[selectedIndustryIndex].productsAmount =
                (int)(gameProperties.carsIndustries[selectedIndustryIndex].industryPrice/10000);
        }
    }

    // Attempt a drill industry sabotage
    static void attemptDrillIndustrySabotage(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLACK, TextColor.ANSI.WHITE,
                TextColor.ANSI.WHITE, TextColor.ANSI.BLACK,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.WHITE
            )
        );

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(new Label("KTORA Z NASTEPUJACYCH FABRYK WIERTE£ BEDZIESZ SABOTOWAC?"));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable = new Table<String>("NR", "NAZWA", "CENA", "W£ASNO$C");

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0","-","-","-");
        for (int industryIndex = 0; industryIndex < gameProperties.drillsIndustries.length; industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (gameProperties.drillsIndustries[industryIndex].isBought) {
                ownerName = gameProperties.oilfields[industryIndex].ownership.name;
            }

            industriesTable.getTableModel().addRow(
                String.valueOf(industryIndex+1),
                gameProperties.drillsIndustries[industryIndex].name,
                String.valueOf(gameProperties.drillsIndustries[industryIndex].industryPrice) + " $",
                ownerName
            );
        }

        industriesTable.setSelectAction(() -> {
            gameProperties.tmpActionInt = Integer.parseInt(industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))-1;
            gameProperties.tmpConfirm = true;
        });

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

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

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.balance -= gameProperties.drillsIndustries[selectedIndustryIndex].industryPrice * finalResult;
        if (finalResult < 1) {
            gameProperties.drillsIndustries[selectedIndustryIndex].ownership = null;
            gameProperties.drillsIndustries[selectedIndustryIndex].isBought = false;
            gameProperties.drillsIndustries[selectedIndustryIndex].industryPrice = random.nextInt(100000)+1;
            gameProperties.drillsIndustries[selectedIndustryIndex].productPrice = 0;
            gameProperties.drillsIndustries[selectedIndustryIndex].productsAmount =
                (int)(gameProperties.drillsIndustries[selectedIndustryIndex].industryPrice/10000);
        }
    }

    static double sabotageGenerateResult(GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.BLACK
            )
        );

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(new Label("TU PADNIE ROZSTRZYGNIECIE!"));
        contentPanel.addComponent(new EmptySpace());

        // Create panel for generating options
        Table<String> optionsTable = new Table<String>(" ","  "," "," ");
        optionsTable.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.CYAN, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT
            )
        );

        int[] results = new int[]{50, -20, 40, -10, 30, -30, 10, -40, 20, -50};

        // Add elements to displayed grid
        for (int i = 0; i < results.length; i++) {
            // Variable to store symbol
            String symbol = "-";

            // Add symbol
            if (i % 2 == 0) {
                symbol = "+";
            }

            // Add values to the table
            optionsTable.getTableModel().addRow(symbol, String.valueOf(Math.abs(results[i])), "%", " ");
        }

        // Display the table
        contentPanel.addComponent(optionsTable);

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label("WCI$NIJ GUZIK W ODPOWIEDNIM MOMENCIE"));

        // Button for confirmation
        Button confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Cycle through options until selection isn't selected
        while (!gameProperties.tmpConfirm) {
            // Cycle through options
            for (int i = 0; i < results.length; i++) {
                // If button is pressed, break
                if (gameProperties.tmpConfirm) {
                    break;
                }

                // Remove arrow from previous option
                if (i == 0) {
                    optionsTable.getTableModel().setCell(3, results.length-1, " ");
                }
                else {
                    optionsTable.getTableModel().setCell(3, i-1, " ");
                }

                // Add arrow to current option
                optionsTable.getTableModel().setCell(3, i, "");
            }
        }
        gameProperties.tmpConfirm = false;

        double result = 0;

        // Get the result
        for (List<String> row : optionsTable.getTableModel().getRows()) {
            if (row.get(3).equals("")) {
                // Set the unsigned value
                result = Integer.parseInt(row.get(1));
                result /= 100;

                // Set the sign
                if (row.get(0).equals("-")) {
                    result *= -1;
                }
                break;
            }
        }

        // Inform about result
        Panel resultPanel = new Panel(new GridLayout(2));
        resultPanel.addComponent(new Label("WYNIK SABOTAZU :"));

        // Format result
        String resultString = "";

        if (result < 0) {
            resultString += "-";
        }
        else {
            resultString += "+";
        }
        resultString += String.valueOf(Math.abs((int)(100*result)));
        resultString += "%";
        resultPanel.addComponent(new Label(resultString));

        // Display result
        contentPanel.addComponent(resultPanel);
        
        if (result > 0) {
            contentPanel.addComponent(new Label("DOP£ACASZ DO INTERESU!"));
        }
        else {
            contentPanel.addComponent(new Label("UDANE PRZEDSIEWZIECIE!"));
        }

        // Button for confirmation
        confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Clean up
        contentPanel.removeAllComponents();

        // Return
        return result;
    }
}
