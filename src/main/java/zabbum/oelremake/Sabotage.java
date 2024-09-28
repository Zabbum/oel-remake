package zabbum.oelremake;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import zabbum.oelremake.plants.industries.Cars.CarsIndustry;
import zabbum.oelremake.plants.industries.Drills.DrillsIndustry;
import zabbum.oelremake.plants.industries.Pumps.PumpsIndustry;
import zabbum.oelremake.plants.oilfield.Oilfield;

import java.util.List;
import java.util.Random;

public class Sabotage {

    // Do a sabotage
    public static void doSabotage(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT));

        // Display sabotage shade
        if (!gameProperties.isInDevMode) {
            for (int i = 0; i < 23; i++) {
                contentPanel.addComponent(
                        new Label(" ".repeat(i + 1) + gameProperties.langMap.get("sabotageFancy")));
                Thread.sleep(50);
            }
            Thread.sleep(1000);
        }
        contentPanel.removeAllComponents();

        // Blue button theme
        Theme blueButton =
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLUE,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLUE,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLACK_BRIGHT);
        // Yellow button theme
        Theme yellowButton =
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLACK_BRIGHT);

        // Inform user what can they do
        contentPanel.addComponent(new Label(gameProperties.langMap.get("sabotagePossibilities")));
        contentPanel.addComponent(new EmptySpace());

        ConfirmAction tmpConfirm = new ConfirmAction();

        // Display options
        Component firstButton =
                new Button(
                        gameProperties.langMap.get("sabotageOilfield"),
                        () -> tmpConfirm.confirm("0"))
                        .setTheme(blueButton);
        contentPanel.addComponent(firstButton);
        ((Interactable) firstButton).takeFocus();

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("sabotagePumpsIndustry"),
                        () -> tmpConfirm.confirm("1"))
                        .setTheme(yellowButton));

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("sabotageCarsIndustry"),
                        () -> tmpConfirm.confirm("2"))
                        .setTheme(blueButton));

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("sabotageDrillsIndustry"),
                        () -> tmpConfirm.confirm("3"))
                        .setTheme(yellowButton));

        // Wait for response
        tmpConfirm.waitForConfirm();
        contentPanel.removeAllComponents();

        // Redirect to valid menu
        int action = Integer.parseInt(tmpConfirm.getAction());

        switch (action) {
            case 0 -> {
                attemptOilfieldSabotage(player, gameProperties);
            }
            case 1 -> {
                attemptPumpsIndustrySabotage(player, gameProperties);
            }
            case 2 -> {
                attemptCarsIndustrySabotage(player, gameProperties);
            }
            case 3 -> {
                attemptDrillsIndustrySabotage(player, gameProperties);
            }
            default -> {
            }
        }
        // Clean up
        contentPanel.removeAllComponents();
    }

    // Attempt a oilfield sabotage
    static void attemptOilfieldSabotage(Player player, GameProperties gameProperties)
            throws InterruptedException {

        Oilfield[] oilfields = gameProperties.oilfieldOperations.getOilfields();

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT));

        contentPanel.addComponent(
                new Label(String.format(gameProperties.langMap.get("oilfieldSabotageChances"), 50)));
        contentPanel.addComponent(new EmptySpace());

        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Prepare new graphical settings
        contentPanel.removeAllComponents();

        // Generate is succeed
        Random random = new Random();
        boolean isSucceed = random.nextBoolean();

        // If action is succeed
        if (isSucceed) {
            // Setup colors
            gameProperties.window.setTheme(
                    SimpleTheme.makeTheme(
                            false,
                            TextColor.ANSI.WHITE,
                            TextColor.ANSI.BLACK_BRIGHT,
                            TextColor.ANSI.BLACK_BRIGHT,
                            TextColor.ANSI.WHITE,
                            TextColor.ANSI.CYAN,
                            TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.BLACK_BRIGHT));

            contentPanel.addComponent(
                    new Label("HELLO! I'M AGENT FUNNY HAPPY BEAR")
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT)));
            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("whichOilfieldToSabotage"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT)));
            contentPanel.addComponent(new EmptySpace());

            // Display all of the oilfields

            // Create table
            Table<String> oilfieldsTable =
                    new Table<String>(
                            "NR", gameProperties.langMap.get("name"), gameProperties.langMap.get("property"));

            // Add every available oilfield to table
            oilfieldsTable.getTableModel().addRow("0", "-", "-");
            for (int oilfieldIndex = 0;
                 oilfieldIndex < oilfields.length;
                 oilfieldIndex++) {
                // If oilfield is bought, display the name
                String ownerName = "---";

                if (oilfields[oilfieldIndex].isBought()) {
                    ownerName = oilfields[oilfieldIndex].getOwnership().getName();
                }

                oilfieldsTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(oilfieldIndex + 1),
                                oilfields[oilfieldIndex].getName(),
                                ownerName);
            }

            tmpConfirm = new Confirm();

            oilfieldsTable.setSelectAction(tmpConfirm::confirm);

            // Display table
            contentPanel.addComponent(oilfieldsTable);
            oilfieldsTable.takeFocus();

            // Wait for selection
            tmpConfirm.waitForConfirm();
            int selectedOilfieldIndex =
                    Integer.parseInt(
                            oilfieldsTable.getTableModel().getRow(oilfieldsTable.getSelectedRow()).get(0))
                            - 1;

            // If 0 selected, return
            if (selectedOilfieldIndex == -1) {
                // Clean up
                contentPanel.removeAllComponents();
                return;
            }

            // Generate fees
            int fees1 = random.nextInt(40000) + 40000;
            int fees2 = random.nextInt(40000) + 1;

            // Inform user about the costs
            contentPanel.removeAllComponents();
            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("sabotageOilfieldData"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT)));
            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("sabotageOilfieldCosts"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT)));

            Panel costPanel = new Panel(new GridLayout(3));

            costPanel.addComponent(new Label(gameProperties.langMap.get("sabotageOilfieldBribes")));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees1) + " $"));

            costPanel.addComponent(new Label(gameProperties.langMap.get("sabotageOilfieldBombs")));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees2) + " $"));

            costPanel.addComponent(new Label(gameProperties.langMap.get("summingUp")));
            costPanel.addComponent(new Label("="));
            costPanel.addComponent(new Label(String.valueOf(fees1 + fees2) + " $"));

            contentPanel.addComponent(costPanel);

            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("chancesAre") + " 33%.")
                            .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT)));

            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("yourBalance2") + " = " + player.getBalance() + "$")
                            .setTheme(
                                    new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT)));

            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(
                    new Label("BEAR \'" + gameProperties.langMap.get("sabotageOilfieldShouldIStart") + "\'"));

            Panel buttonPanel = new Panel(new GridLayout(2));

            tmpConfirm = new Confirm();

            Button declineButton =
                    new Button(
                            gameProperties.langMap.get("no"),
                            () -> {
                                // Clean up
                                contentPanel.removeAllComponents();
                                return;
                            });
            buttonPanel.addComponent(declineButton);
            buttonPanel.addComponent(
                    new Button(
                            gameProperties.langMap.get("yes"), tmpConfirm::confirm));

            contentPanel.addComponent(buttonPanel);
            contentPanel.addComponent(new EmptySpace());

            tmpConfirm.waitForConfirm();

            // Reduce player's balance
            player.decreaseBalance(fees1 + fees2);

            // Generate if action is succeed or not
            int isSucceedSabotage = random.nextInt(3);

            // Animated element
            Component animatedLabel =
                    new Label(gameProperties.langMap.get("sabotageSuccessful"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));

            contentPanel.addComponent(animatedLabel);

            // Generating animation
            for (int i = 0; i < 50; i++) {
                if (i % 2 == 1) {
                    ((Label) animatedLabel).setText(gameProperties.langMap.get("sabotageUnsuccessful"));
                    animatedLabel.setTheme(
                            new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
                } else {
                    ((Label) animatedLabel).setText(gameProperties.langMap.get("sabotageSuccessful"));
                    animatedLabel.setTheme(
                            new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
                }

                Thread.sleep(100);
            }

            // Take actions based on sabotage succession
            if (isSucceedSabotage == 2) {
                // Inform user about status
                ((Label) animatedLabel).setText(gameProperties.langMap.get("success"));
                animatedLabel.setTheme(
                        new SimpleTheme(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));

                // Set new oilfield price
                oilfields[selectedOilfieldIndex].setPlantPrice(random.nextInt(50000) + 30001);
                // Set new oil amount
                oilfields[selectedOilfieldIndex].setTotalOilAmount(
                        random.nextInt(200000) + 1);
                // Revoke ownership
                oilfields[selectedOilfieldIndex].setOwnership(null);
                // Revoke ability to pump oil
                oilfields[selectedOilfieldIndex].setExploitable(false);
                // Set new requred depth to pump oil
                oilfields[selectedOilfieldIndex].setRequiredDepth(random.nextInt(4500) + 1);
                // Reset amount of pumps
                oilfields[selectedOilfieldIndex].setPumpsAmount(0);
                // Reset amount of cars
                oilfields[selectedOilfieldIndex].setCarsAmount(0);
                // Reset amount of drills
                oilfields[selectedOilfieldIndex].setDrillsAmount(0);
                // Reset current digging depth
                oilfields[selectedOilfieldIndex].setCurrentDepth(0);
                // Reset amount of oil pumped out
                oilfields[selectedOilfieldIndex].setOilExtracted(0);
                // Reset amount of oil available to sell
                oilfields[selectedOilfieldIndex].setOilAvailabletoSell(0);

            } else {
                // Inform user about status
                ((Label) animatedLabel).setText(gameProperties.langMap.get("failure"));
                animatedLabel.setTheme(
                        new SimpleTheme(TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK_BRIGHT));
            }

            Thread.sleep(3000);
        }

        // Clean up
        contentPanel.removeAllComponents();
        return;
    }

    // Attempt a pump industry sabotage
    static void attemptPumpsIndustrySabotage(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Variable to store all the industries
        PumpsIndustry[] pumpsIndustries = gameProperties.pumpsIndustryOperations.getIndustries();

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT));

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("whichPumpsIndustryToSabotage")));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable =
                new Table<String>(
                        "NR",
                        gameProperties.langMap.get("name"),
                        gameProperties.langMap.get("price"),
                        gameProperties.langMap.get("property"));

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0;
             industryIndex < pumpsIndustries.length;
             industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (pumpsIndustries[industryIndex].isBought()) {
                ownerName = pumpsIndustries[industryIndex].getOwnership().getName();
            }

            industriesTable
                    .getTableModel()
                    .addRow(
                            String.valueOf(industryIndex + 1),
                            pumpsIndustries[industryIndex].getName(),
                            String.valueOf(pumpsIndustries[industryIndex].getPlantPrice())
                                    + " $",
                            ownerName);
        }

        Confirm tmpConfirm = new Confirm();

        industriesTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

        // Wait for selection
        tmpConfirm.waitForConfirm();
        int selectedIndustryIndex =
                Integer.parseInt(
                        industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.decreaseBalance(
                pumpsIndustries[selectedIndustryIndex].getPlantPrice() * finalResult);
        if (finalResult < 1) {
            pumpsIndustries[selectedIndustryIndex].setOwnership(null);
            pumpsIndustries[selectedIndustryIndex].setPlantPrice(
                    random.nextInt(100000) + 1);
            pumpsIndustries[selectedIndustryIndex].setProductPrice(0);
            pumpsIndustries[selectedIndustryIndex].setProductsAmount(
                    (int) (pumpsIndustries[selectedIndustryIndex].getPlantPrice() / 10000));
        }
    }

    // Attempt a car industry sabotage
    static void attemptCarsIndustrySabotage(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Variable to store all the industries
        CarsIndustry[] carsIndustries = gameProperties.carsIndustryOperations.getIndustries();

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK_BRIGHT));

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("whichCarsIndustryToSabotage"))
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT)));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable =
                new Table<String>(
                        "NR",
                        gameProperties.langMap.get("name"),
                        gameProperties.langMap.get("price"),
                        gameProperties.langMap.get("property"));

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0;
             industryIndex < carsIndustries.length;
             industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (carsIndustries[industryIndex].isBought()) {
                ownerName = carsIndustries[industryIndex].getOwnership().getName();
            }

            industriesTable
                    .getTableModel()
                    .addRow(
                            String.valueOf(industryIndex + 1),
                            carsIndustries[industryIndex].getName(),
                            String.valueOf(carsIndustries[industryIndex].getPlantPrice())
                                    + " $",
                            ownerName);
        }

        Confirm tmpConfirm = new Confirm();

        industriesTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

        // Wait for selection
        tmpConfirm.waitForConfirm();
        int selectedIndustryIndex =
                Integer.parseInt(
                        industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.decreaseBalance(
                carsIndustries[selectedIndustryIndex].getPlantPrice() * finalResult);
        if (finalResult < 1) {
            carsIndustries[selectedIndustryIndex].setOwnership(null);
            carsIndustries[selectedIndustryIndex].setPlantPrice(
                    random.nextInt(200000) + 1);
            carsIndustries[selectedIndustryIndex].setProductPrice(0);
            carsIndustries[selectedIndustryIndex].setProductsAmount(
                    (int) (carsIndustries[selectedIndustryIndex].getPlantPrice() / 10000));
        }
    }

    // Attempt a drill industry sabotage
    static void attemptDrillsIndustrySabotage(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Variable to store all the industries
        DrillsIndustry[] drillsIndustries = gameProperties.drillsIndustryOperations.getIndustries();

        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE,
                        TextColor.ANSI.WHITE,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE));

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("whichDrillsIndustryToSabotage")));
        contentPanel.addComponent(new EmptySpace());

        // Display all of the industries

        // Create table
        Table<String> industriesTable =
                new Table<String>(
                        "NR",
                        gameProperties.langMap.get("name"),
                        gameProperties.langMap.get("price"),
                        gameProperties.langMap.get("property"));

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0;
             industryIndex < drillsIndustries.length;
             industryIndex++) {
            // If industry is bought, display the name
            String ownerName = "---";

            if (drillsIndustries[industryIndex].isBought()) {
                ownerName = drillsIndustries[industryIndex].getOwnership().getName();
            }

            industriesTable
                    .getTableModel()
                    .addRow(
                            String.valueOf(industryIndex + 1),
                            drillsIndustries[industryIndex].getName(),
                            String.valueOf(drillsIndustries[industryIndex].getPlantPrice())
                                    + " $",
                            ownerName);
        }

        Confirm tmpConfirm = new Confirm();

        industriesTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();

        // Wait for selection
        tmpConfirm.waitForConfirm();
        int selectedIndustryIndex =
                Integer.parseInt(
                        industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Get result
        double finalResult = sabotageGenerateResult(gameProperties) + 1;

        // Take actions
        Random random = new Random();

        player.decreaseBalance(
                drillsIndustries[selectedIndustryIndex].getPlantPrice() * finalResult);
        if (finalResult < 1) {
            drillsIndustries[selectedIndustryIndex].setOwnership(null);
            drillsIndustries[selectedIndustryIndex].setPlantPrice(
                    random.nextInt(100000) + 1);
            drillsIndustries[selectedIndustryIndex].setProductPrice(0);
            drillsIndustries[selectedIndustryIndex].setProductsAmount(
                    (int)
                            (drillsIndustries[selectedIndustryIndex].getPlantPrice() / 10000));
        }
    }

    static double sabotageGenerateResult(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK));

        // Clean up
        contentPanel.removeAllComponents();

        contentPanel.addComponent(new Label(gameProperties.langMap.get("hereWillBeResolution")));
        contentPanel.addComponent(new EmptySpace());

        // Create panel for generating options
        Table<String> optionsTable = new Table<String>(" ", "  ", " ", " ");
        optionsTable.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT));

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

        contentPanel.addComponent(new Label(gameProperties.langMap.get("pressButton")));

        // Button for confirmation
        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Cycle through options until selection isn't selected
        while (!tmpConfirm.isConfirmed()) {
            // Cycle through options
            for (int i = 0; i < results.length; i++) {
                // If button is pressed, break
                if (tmpConfirm.isConfirmed()) {
                    break;
                }

                // Remove arrow from previous option
                if (i == 0) {
                    optionsTable.getTableModel().setCell(3, results.length - 1, " ");
                } else {
                    optionsTable.getTableModel().setCell(3, i - 1, " ");
                }

                // Add arrow to current option
                optionsTable.getTableModel().setCell(3, i, "");
            }
        }

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
        resultPanel.addComponent(new Label(gameProperties.langMap.get("sabotageResolution") + " :"));

        // Format result
        String resultString = "";

        if (result < 0) {
            resultString += "-";
        } else {
            resultString += "+";
        }
        resultString += String.valueOf(Math.abs((int) (100 * result)));
        resultString += "%";
        resultPanel.addComponent(new Label(resultString));

        // Display result
        contentPanel.addComponent(resultPanel);

        if (result > 0) {
            contentPanel.addComponent(new Label(gameProperties.langMap.get("sabotageUnsuccessful2")));
        } else {
            contentPanel.addComponent(new Label(gameProperties.langMap.get("sabotageSuccessful2")));
        }

        // Button for confirmation
        tmpConfirm = new Confirm();
        confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Clean up
        contentPanel.removeAllComponents();

        // Return
        return result;
    }
}
