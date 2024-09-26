package zabbum.oelremake;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;

import java.util.regex.Pattern;

public class ChangePrices {
    public static void menu(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.RED));

        // Inform user
        contentPanel.addComponent(new Label(gameProperties.langMap.get("whatPriceWillYouBeChanging")));
        contentPanel.addComponent(new EmptySpace());

        // Display options
        Component firstButton =
                new Button(
                        gameProperties.langMap.get("pumpsPrices"),
                        () -> {
                            gameProperties.tmpAction = "0";
                            gameProperties.tmpConfirm = true;
                        });
        contentPanel.addComponent(firstButton);
        ((Interactable) firstButton).takeFocus();

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("carsPrices"),
                        () -> {
                            gameProperties.tmpAction = "1";
                            gameProperties.tmpConfirm = true;
                        }));

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("drillsPrices"),
                        () -> {
                            gameProperties.tmpAction = "2";
                            gameProperties.tmpConfirm = true;
                        }));

        // Wait for response
        Game.waitForConfirm(gameProperties);
        contentPanel.removeAllComponents();

        int action = Integer.parseInt(gameProperties.tmpAction);

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
    static void pumps(Player player, GameProperties gameProperties) throws InterruptedException {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.pumpsIndustryOperations.getIndustries();

        // Create table
        Table<String> industryTable =
                new Table<String>(
                        "NR", gameProperties.langMap.get("industryName"), gameProperties.langMap.get("price"));

        // Add every available industry to table
        industryTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought()) {
                // If industry is not bought, make it possible to buy it
                industryTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(industryIndex + 1),
                                industries[industryIndex].getName(),
                                String.valueOf(industries[industryIndex].getIndustryPrice()) + "$");
            }
        }

        industryTable.setSelectAction(
                () -> {
                    gameProperties.tmpConfirm = true;
                });

        // Display table
        contentPanel.addComponent(industryTable);
        industryTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex =
                Integer.parseInt(
                        industryTable.getTableModel().getRow(industryTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        double proposedPrice = -1;
        while (proposedPrice < 0 || proposedPrice > 50000) {
            // Prompt for price
            contentPanel.addComponent(new EmptySpace());
            productPriceBox = new TextBox(new TerminalSize(6, 1));
            productPriceBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productPriceBox);
            contentPanel.addComponent(Elements.confirmButton(gameProperties));

            // Wait for selection
            productPriceBox.takeFocus();
            Game.waitForConfirm(gameProperties);
            try {
                proposedPrice = Integer.parseInt(productPriceBox.getText());
            } catch (NumberFormatException e) {
                // If a bad value has been provided
                proposedPrice = -1;
            }
        }

        // Set a new price
        gameProperties.drillsIndustryOperaions.getIndustries()[selectedIndustryIndex].setProductPrice(proposedPrice);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Change cars prices
    static void cars(Player player, GameProperties gameProperties) throws InterruptedException {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.carsIndustryOperations.getIndustries();

        // Create table
        Table<String> industryTable =
                new Table<String>(
                        "NR", gameProperties.langMap.get("industryName"), gameProperties.langMap.get("price"));

        // Add every available industry to table
        industryTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought()) {
                // If industry is not bought, make it possible to buy it
                industryTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(industryIndex + 1),
                                industries[industryIndex].getName(),
                                String.valueOf(industries[industryIndex].getIndustryPrice()) + "$");
            }
        }

        industryTable.setSelectAction(
                () -> {
                    gameProperties.tmpConfirm = true;
                });

        // Display table
        contentPanel.addComponent(industryTable);
        industryTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex =
                Integer.parseInt(
                        industryTable.getTableModel().getRow(industryTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        double proposedPrice = -1;
        while (proposedPrice < 0 || proposedPrice > 60000) {
            // Prompt for price
            contentPanel.addComponent(new EmptySpace());
            productPriceBox = new TextBox(new TerminalSize(6, 1));
            productPriceBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productPriceBox);
            contentPanel.addComponent(Elements.confirmButton(gameProperties));

            // Wait for selection
            productPriceBox.takeFocus();
            Game.waitForConfirm(gameProperties);
            try {
                proposedPrice = Integer.parseInt(productPriceBox.getText());
            } catch (NumberFormatException e) {
                // If a bad value has been provided
                proposedPrice = -1;
            }
        }

        // Set a new price
        gameProperties.carsIndustryOperations.getIndustries()[selectedIndustryIndex].setProductPrice(proposedPrice);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Change drills prices
    static void drills(Player player, GameProperties gameProperties) throws InterruptedException {
        Panel contentPanel = gameProperties.contentPanel;
        AbstractIndustry[] industries = gameProperties.drillsIndustryOperaions.getIndustries();

        // Create table
        Table<String> industryTable =
                new Table<String>(
                        "NR", gameProperties.langMap.get("industryName"), gameProperties.langMap.get("price"));

        // Add every available industry to table
        industryTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought()) {
                // If industry is not bought, make it possible to buy it
                industryTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(industryIndex + 1),
                                industries[industryIndex].getName(),
                                String.valueOf(industries[industryIndex].getIndustryPrice()) + "$");
            }
        }

        industryTable.setSelectAction(
                () -> {
                    gameProperties.tmpConfirm = true;
                });

        // Display table
        contentPanel.addComponent(industryTable);
        industryTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedIndustryIndex =
                Integer.parseInt(
                        industryTable.getTableModel().getRow(industryTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Prompt for price until provided value is valid
        TextBox productPriceBox = null;
        double proposedPrice = -1;
        while (proposedPrice < 0 || proposedPrice > 60000) {
            // Prompt for price
            contentPanel.addComponent(new EmptySpace());
            productPriceBox = new TextBox(new TerminalSize(6, 1));
            productPriceBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productPriceBox);
            contentPanel.addComponent(Elements.confirmButton(gameProperties));

            // Wait for selection
            productPriceBox.takeFocus();
            Game.waitForConfirm(gameProperties);
            try {
                proposedPrice = Integer.parseInt(productPriceBox.getText());
            } catch (Exception e) {
                // If a bad value has been provided
                proposedPrice = -1;
            }
        }

        // Set a new price
        gameProperties.drillsIndustryOperaions.getIndustries()[selectedIndustryIndex].setProductPrice(proposedPrice);
        ;

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Generate possible actions
    static String[] generatePossibleActions(
            int possibleActionsLength, AbstractIndustry[] industries, Player player) {
        String[] possibleActions = new String[possibleActionsLength];
        for (int i = 0; i < possibleActions.length; i++) {
            possibleActions[i] = "0";
        }
        for (int i = 0; i < industries.length; i++) {
            if (industries[i].getOwnership() == player) {
                for (int j = 0; j < possibleActions.length; j++) {
                    if (possibleActions[j].equals("0")) {
                        possibleActions[j] = String.valueOf(i + 1);
                        break;
                    }
                }
            }
        }
        return possibleActions;
    }
}
