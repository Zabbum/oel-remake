package zabbum.oelremake;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import zabbum.oelremake.artloader.ArtObject;
import zabbum.oelremake.plants.oilfield.Oilfield;

import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Pattern;

public class Game {

    // Sleep
    public static void sleep(long time, GameProperties gameProperties) throws InterruptedException {
        if (!gameProperties.isInDevMode) {
            Thread.sleep(time);
        }
    }

    // Finish game
    public static void finishGame(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.removeAllComponents();
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.ANSI.BLACK,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.ANSI.BLACK));

        contentPanel.addComponent(new Label(gameProperties.langMap.get("gameResult") + ":"));

        contentPanel.addComponent(new EmptySpace());

        // Create table for results
        Table<String> resultsTable =
                new Table<>(
                        gameProperties.langMap.get("player").toUpperCase(),
                        gameProperties.langMap.get("loan"),
                        gameProperties.langMap.get("balance"));

        // Add every player to table
        for (Player player : gameProperties.players) {
            resultsTable
                    .getTableModel()
                    .addRow(
                            player.getName(),
                            String.valueOf(player.getDebt()),
                            String.valueOf(player.getBalance()));
        }

        contentPanel.addComponent(resultsTable);

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label(gameProperties.langMap.get("congratulationsToTheWinners")));

        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();
    }

    // Display OEL logo
    public static void oelLogo(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.RED));

        Game.timeBuffor();
        try {
            // Get OEL logo ASCII art
            InputStream oelLogoFileStream =
                    Application.class.getClassLoader().getResourceAsStream("arts/oel.json");
            contentPanel.addComponent(new ArtObject(Objects.requireNonNull(oelLogoFileStream)).getImageComponent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            contentPanel.addComponent(new Label("OEL"));
            contentPanel.addComponent(new Label("CR. COMP.& TRANSL. BY MI$ AL"));
            contentPanel.addComponent(new Label("REMADE IN JAVA BY ZABBUM"));
        }

        Game.sleep(5000, gameProperties);

        // Clean up
        contentPanel.removeAllComponents();

        // Display game motto
        contentPanel.addComponent(new Label("THE BIG GAME AND BIG MONEY."));

        Game.sleep(3000, gameProperties);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Prompt for players amount
    public static void promptPlayerAmount(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(2));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.RED));

        contentPanel.addComponent(new Label(gameProperties.langMap.get("howManyPlayers") + " (2-6)"));
        TextBox playerAmountTextBox = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
        contentPanel.addComponent(playerAmountTextBox);
        playerAmountTextBox.takeFocus();

        contentPanel.addComponent(new EmptySpace());

        Confirm tmpConfirm = new Confirm();

        contentPanel.addComponent(Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done")));

        // If confirm button is pressed and choise is valid, let it be
        do {
            tmpConfirm.waitForConfirm();
            tmpConfirm.setConfirmStatus(false);
        } while (!(SimpleLogic.isValid(playerAmountTextBox.getText(), 2, new int[]{6})));

        gameProperties.playerAmount = Integer.parseInt(playerAmountTextBox.getText());
        System.out.println(gameProperties.playerAmount + " players");

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Intro info for player and prompt for names
    public static void promptPlayerNames(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.RED));

        contentPanel.addComponent(new Label(gameProperties.langMap.get("wereCurrentlyIn") + ":"));
        Game.timeBuffor();
        contentPanel.addComponent(new EmptySpace());

        // Fancy 1986
        try {
            // Get 1986 ASCII art
            InputStream startYearInputStream =
                    Application.class.getClassLoader().getResourceAsStream("arts/1986.json");
            contentPanel.addComponent(new ArtObject(startYearInputStream).getImageComponent());
        } catch (Exception e) {
            contentPanel.addComponent(new Label("1986"));
        }
        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label(gameProperties.langMap.get("gameEndsIn") + " 2020"));
        contentPanel.addComponent(new Label(gameProperties.langMap.get("playersWillBe") + ":"));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(gameProperties.langMap.get("enterNames")));

        // Prompt for names
        Panel promptPanel = new Panel(new GridLayout(2));
        TextBox[] playerNames = new TextBox[gameProperties.playerAmount];

        // Display correct amount of textboxes
        for (int i = 0; i < gameProperties.playerAmount; i++) {
            promptPanel.addComponent(new Label("?"));
            playerNames[i] =
                    new TextBox(gameProperties.langMap.get("player") + " " + (i + 1));
            promptPanel.addComponent(playerNames[i]);
        }

        contentPanel.addComponent(promptPanel);
        // Set focus to first textbox
        playerNames[0].takeFocus();

        // Confirmation button
        Confirm tmpConfirm = new Confirm();
        contentPanel.addComponent(Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done")));

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Create player objects
        gameProperties.players = new Player[gameProperties.playerAmount];
        for (int i = 0; i < playerNames.length; i++) {
            System.out.println(i + ": " + playerNames[i].getText());
            gameProperties.players[i] = new Player(playerNames[i].getText());
        }

        // Inform about money amount
        contentPanel.removeAllComponents();
        Game.timeBuffor();
        contentPanel.addComponent(
                new Label(String.format(gameProperties.langMap.get("everyPlayerHas"), 123421)));
        contentPanel.addComponent(new EmptySpace());

        tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Inform about money amount and oil prices
    public static void moneyInfo(GameProperties gameProperties) throws InterruptedException {
        Panel contentPanel = gameProperties.contentPanel;

        // Inform about money amount
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT));

        contentPanel.addComponent(new Label(gameProperties.langMap.get("theWinnerWillBe")));
        Game.timeBuffor();
        contentPanel.addComponent(new Label(gameProperties.langMap.get("theWinnerWillBe2")));
        contentPanel.addComponent(new EmptySpace());
        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Prepare new graphical settings
        contentPanel.removeAllComponents();
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.RED,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.RED));

        // Inform user about oil prices
        Panel titlePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.YELLOW_BRIGHT));
        titlePanel.addComponent(new Label(gameProperties.langMap.get("predictedPrices")));
        titlePanel.addComponent(new Label(gameProperties.langMap.get("trendsInYears")));
        contentPanel.addComponent(titlePanel);
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new EmptySpace());

        // Draw oil prices graph
        ImageComponent oilGraph = new ImageComponent();
        oilGraph.setTextImage(
                Oil.oilGraph(gameProperties, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.RED));
        contentPanel.addComponent(oilGraph);
        contentPanel.addComponent(new EmptySpace());

        // Reduce oil prices
        Oil.reducePrices(gameProperties.oilPrices);
        System.out.println("Generated oil prices");

        tmpConfirm = new Confirm();
        Button confirmButton1 = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));

        contentPanel.addComponent(confirmButton1);
        confirmButton1.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Play single round
    public static void playRound(GameProperties gameProperties) throws InterruptedException {
        gameProperties.currentRound++;

        for (Player player : gameProperties.players) {
            // Display the main menu
            mainMenu(player, gameProperties);


        }
        endRound(gameProperties);
    }

    // Actions to take at end of the round
    static void endRound(GameProperties gameProperties) throws InterruptedException {
        Panel contentPanel = gameProperties.contentPanel;

        // Actions for every player
        for (Player player : gameProperties.players) {

            // Oilfields overview
            for (Oilfield oilfield : gameProperties.oilfieldOperations.getOilfields()) {
                // Clean up
                contentPanel.removeAllComponents();

                // If user is not owner of the field, move on to the next
                if (oilfield.getOwnership() != player) {
                    continue;
                }

                // If oilfield is able to pump oil
                if (oilfield.isExploitable()) {
                    oilfieldManagementMenu(player, oilfield, gameProperties);
                }

                // If oilfield is not able to pump oil
                else {
                    drillingMenu(player, oilfield, gameProperties);
                }
            }
        }

        // Balance overview
        overview(gameProperties);

        // Debt mechanics
        for (Player player : gameProperties.players) {
            debt(player, gameProperties);
        }
    }

    // Menu for displaying drilling progress
    static void drillingMenu(Player player, Oilfield oilfield, GameProperties gameProperties)
            throws InterruptedException {
        // Prepare graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.GREEN,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.GREEN,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT));

        // Inform user about status
        Panel headerPanel = new Panel(new GridLayout(2));
        try {
            // Get drill ASCII art
            InputStream drillArtInputStream =
                    Application.class.getClassLoader().getResourceAsStream("arts/drill.json");
            headerPanel.addComponent(new ArtObject(drillArtInputStream).getImageComponent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Panel textPanel = new Panel(new GridLayout(1));

        textPanel.addComponent(
                new Label(gameProperties.langMap.get("drillingOn") + ":")
                        .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));

        textPanel.addComponent(
                new Label(oilfield.getName())
                        .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.WHITE_BRIGHT)));

        textPanel.addComponent(new EmptySpace());

        textPanel.addComponent(
                new Panel(new GridLayout(2))
                        .addComponent(
                                new Label(gameProperties.langMap.get("property") + ": ")
                                        .setTheme(
                                                new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.WHITE_BRIGHT)))
                        .addComponent(
                                new Label(player.getName())
                                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT))));

        headerPanel.addComponent(textPanel);
        contentPanel.addComponent(headerPanel);

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("yourPeopleFromOilfield"))
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT)));
        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("areReporting"))
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT)));

        contentPanel.addComponent(new EmptySpace());

        // If no drills, inform player
        if (oilfield.getDrillsAmount() <= 0) {
            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("drillingImpossible"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));
            contentPanel.addComponent(
                    new Label(" " + gameProperties.langMap.get("youNeedToDoSomething"))
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));
            contentPanel.addComponent(new EmptySpace());
        }

        // If there are drills, drill
        else {
            oilfield.dig();
        }

        // In both cases, inform about current progess
        contentPanel.addComponent(
                new Label(
                        gameProperties.langMap.get("currentDepth") + ": " + oilfield.getCurrentDepth() + "M"));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(
                new Label(
                        gameProperties.langMap.get("canDrillFor") + ": " + oilfield.getDrillsAmount() + "M"));
        contentPanel.addComponent(new EmptySpace());

        // If reached the point that makes it available to extract oil,
        // take actions
        if (oilfield.getCurrentDepth() >= oilfield.getRequiredDepth()) {
            contentPanel.addComponent(new Label(gameProperties.langMap.get("gushed")));
            contentPanel.addComponent(new EmptySpace());
            oilfield.setExploitable(true);
        }

        // Button for confirmation
        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Menu for oilfield management
    static void oilfieldManagementMenu(
            Player player, Oilfield oilfield, GameProperties gameProperties) throws InterruptedException {
        // Routine actions
        if (oilfield.getOilExtracted() <= oilfield.getTotalOilAmount()) {
            oilfield.extractOil();
        }

        // Prepare graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.YELLOW,
                        TextColor.ANSI.YELLOW,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.YELLOW));

        // Inform user
        Panel headerPanel = new Panel(new GridLayout(2));
        Panel imagePanel = new Panel(new GridLayout(2));
        Panel oilfieldInfoPanel = new Panel(new GridLayout(1));

        // Display ASCII arts
        try {
            // Get ASCII arts
            InputStream pumpjackArtInputStream =
                    Application.class.getClassLoader().getResourceAsStream("arts/pumpjack.json");
            InputStream truckArtInputStream =
                    Application.class.getClassLoader().getResourceAsStream("arts/truck.json");

            imagePanel.addComponent(new ArtObject(pumpjackArtInputStream).getImageComponent());
            imagePanel.addComponent(new ArtObject(truckArtInputStream).getImageComponent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Display general oilfield info
        oilfieldInfoPanel.addComponent(
                new Label("  " + gameProperties.langMap.get("oilfield") + " : ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)));
        oilfieldInfoPanel.addComponent(
                new Label(oilfield.getName())
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));
        oilfieldInfoPanel.addComponent(new Label("▔".repeat(17)));
        oilfieldInfoPanel.addComponent(
                new Label(" " + gameProperties.langMap.get("oilfieldOwner") + " ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)));
        oilfieldInfoPanel.addComponent(new Label(player.getName()));
        oilfieldInfoPanel.addComponent(new Label("▔".repeat(17)));

        headerPanel.addComponent(imagePanel);
        headerPanel.addComponent(oilfieldInfoPanel);
        contentPanel.addComponent(headerPanel);

        contentPanel.addComponent(
                new Panel(new GridLayout(2))
                        .addComponent(new Label(gameProperties.langMap.get("year") + ": "))
                        .addComponent(
                                new Label(String.valueOf(gameProperties.currentRound + 1985))
                                        .setTheme(
                                                new SimpleTheme(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.YELLOW))));

        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(
                new Label("MI$ & RY$ & SONS -")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));
        contentPanel.addComponent(
                new Label(
                        gameProperties.langMap.get("oilSellPrice")
                                + " = "
                                + gameProperties.oilPrices[gameProperties.currentRound - 1]
                                + " $")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(
                new Panel(new GridLayout(2))
                        .addComponent(new Label(gameProperties.langMap.get("pumpAmount")))
                        .addComponent(new Label(": " + oilfield.getPumpsAmount()))
                        .addComponent(
                                new Label(gameProperties.langMap.get("pumpedOut"))
                                        .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(
                                new Label(": " + oilfield.getOilAvailabletoSell())
                                        .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label(gameProperties.langMap.get("carsAmount")))
                        .addComponent(new Label(": " + oilfield.getCarsAmount()))
                        .addComponent(
                                new Label(gameProperties.langMap.get("maxExport"))
                                        .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(
                                new Label(": " + oilfield.getCarsAmount() * 7000)
                                        .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label(gameProperties.langMap.get("yourBalance")))
                        .addComponent(new Label(": " + player.getBalance())));

        contentPanel.addComponent(new EmptySpace());

        // If oilfield is out of oil, inform user
        if (oilfield.getOilExtracted() > oilfield.getTotalOilAmount()) {
            contentPanel.addComponent(new Label(gameProperties.langMap.get("sourceExhausted")));
            contentPanel.addComponent(new EmptySpace());
        }

        // If can sell oil
        if (oilfield.getOilAvailabletoSell() > 0 && oilfield.getCarsAmount() > 0) {
            // Ask for amount of oil
            contentPanel.addComponent(new Label(gameProperties.langMap.get("howMuchOilAreYouSelling")));

            TextBox oilAmountToSellTextBox =
                    new TextBox().setValidationPattern(Pattern.compile("[0-9]*"));

            contentPanel.addComponent(oilAmountToSellTextBox);
            oilAmountToSellTextBox.takeFocus();

            Confirm tmpConfirm = new Confirm();
            contentPanel.addComponent(Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done")));

            // If confirm button is pressed and choise is valid, let it be
            do {
                tmpConfirm.waitForConfirm();
                tmpConfirm.setConfirmStatus(false);
            } while (!(SimpleLogic.isValid(
                    oilAmountToSellTextBox.getText(),
                    0,
                    new int[]{oilfield.getCarsAmount() * 7000, oilfield.getOilAvailabletoSell()})));

            // Take actions
            int oilAmount = Integer.parseInt(oilAmountToSellTextBox.getText());
            player.increaseBalance(oilAmount * gameProperties.oilPrices[gameProperties.currentRound - 1]);
            oilfield.sellOil(oilAmount);
        }

        // If cannot sell oil
        else {
            Confirm tmpConfirm = new Confirm();

            Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
            contentPanel.addComponent(confirmButton);
            confirmButton.takeFocus();

            // Wait for confirmation
            tmpConfirm.waitForConfirm();
        }

        // Clean up
        contentPanel.removeAllComponents();
    }

    static void overview(GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.WHITE_BRIGHT));

        // Inform about current balances
        contentPanel.addComponent(
                new Label(
                        gameProperties.langMap.get("year")
                                + ": "
                                + (1985 + gameProperties.currentRound)));
        contentPanel.addComponent(new EmptySpace());

        Game.timeBuffor();

        Panel playersPanel = new Panel(new GridLayout(2));
        for (Player player : gameProperties.players) {
            playersPanel.addComponent(new Label(player.getName()));
            playersPanel.addComponent(
                    new Label(
                            gameProperties.langMap.get("balance") + ": " + player.getBalance()));
        }

        contentPanel.addComponent(playersPanel);

        contentPanel.addComponent(new EmptySpace());

        Confirm tmpConfirm = new Confirm();
        Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        tmpConfirm.waitForConfirm();

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Debt system
    static void debt(Player player, GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;

        // If user has a loan
        if (player.getDebt() > 0) {
            // Pay it off
            player.payOffDebt();

            // Display debtor menu

            // Prepare new graphical settings
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            gameProperties.window.setTheme(
                    SimpleTheme.makeTheme(
                            false,
                            TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.BLACK,
                            TextColor.ANSI.BLACK,
                            TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.CYAN,
                            TextColor.ANSI.BLACK));

            contentPanel.addComponent(
                    new Label(" 'SONS & FATHERS` : " + gameProperties.langMap.get("creditCard") + " ")
                            .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.BLUE_BRIGHT)));

            contentPanel.addComponent(new EmptySpace());

            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("debtor") + ": " + player.getName()));

            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(new EmptySpace());

            Panel loanPanel = new Panel(new GridLayout(2));

            loanPanel.addComponent(new Label(gameProperties.langMap.get("toPay") + ":"));
            loanPanel.addComponent(new Label(player.getDebt() + " $"));
            loanPanel.addComponent(new Label(gameProperties.langMap.get("nextRate") + ":"));
            loanPanel.addComponent(new Label("5000 $"));
            loanPanel.addComponent(new Label(gameProperties.langMap.get("yourBalance") + ":"));
            loanPanel.addComponent(new Label(player.getBalance() + " $"));

            contentPanel.addComponent(loanPanel);

            contentPanel.addComponent(new EmptySpace());

            Confirm tmpConfirm = new Confirm();
            Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
            contentPanel.addComponent(confirmButton);
            confirmButton.takeFocus();

            // Wait for confirmation
            tmpConfirm.waitForConfirm();

            // Clean up
            contentPanel.removeAllComponents();
        }

        // If user has negative money, take debt
        if (player.getBalance() < 0) {
            // Prepare new graphical settings
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            gameProperties.window.setTheme(
                    SimpleTheme.makeTheme(
                            false,
                            TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.BLACK,
                            TextColor.ANSI.BLACK,
                            TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.CYAN,
                            TextColor.ANSI.BLACK));

            // Inform about loan
            Game.timeBuffor();
            contentPanel.addComponent(
                    new Label(gameProperties.langMap.get("itsBad") + " " + player.getName()));
            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(new Label(gameProperties.langMap.get("toPlayYouNeedToBorrow")));
            contentPanel.addComponent(new Label(gameProperties.langMap.get("inBankForASum")));
            contentPanel.addComponent(
                    new Label(String.format(gameProperties.langMap.get("xDolarsPaymentRateIs"), 20000)));
            contentPanel.addComponent(
                    new Label(String.format(gameProperties.langMap.get("xDolars"), 5000)));

            contentPanel.addComponent(new EmptySpace());

            // Take debt
            player.takeDebt();

            Confirm tmpConfirm = new Confirm();
            Button confirmButton = Elements.newConfirmButton(tmpConfirm, gameProperties.langMap.get("done"));
            contentPanel.addComponent(confirmButton);
            confirmButton.takeFocus();

            // Wait for confirmation
            tmpConfirm.waitForConfirm();

            // Clean up
            contentPanel.removeAllComponents();
        }
    }

    // Display main menu and get action
    public static void mainMenu(Player player, GameProperties gameProperties)
            throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        gameProperties.window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.MAGENTA,
                        TextColor.ANSI.MAGENTA,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.MAGENTA));

        // Create theme for black buttons not to make them different when selected
        Theme blackButton =
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.MAGENTA,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.MAGENTA);

        // Display options
        contentPanel.addComponent(
                new Label(gameProperties.langMap.get("itsYourDecision"))
                        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.WHITE_BRIGHT)));

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(
                new Label(
                        gameProperties.langMap.get("player").toUpperCase()
                                + ": "
                                + player.getName()
                                + " $= "
                                + player.getBalance())
                        .setTheme(new SimpleTheme(TextColor.ANSI.CYAN_BRIGHT, TextColor.ANSI.MAGENTA)));

        contentPanel.addComponent(new EmptySpace());

        // Confirm variable
        ConfirmAction tmpConfirm = new ConfirmAction();

        // Options pt. 1
        contentPanel.addComponent(
                new Label(" " + gameProperties.langMap.get("buying") + " ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.BLUE)));

        Component firstButton =
                new Button(
                        gameProperties.langMap.get("drillsIndustries"),
                        () -> tmpConfirm.confirm("A"))
                        .setTheme(blackButton);
        contentPanel.addComponent(firstButton);
        ((Interactable) firstButton).takeFocus();

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("pumpsIndustries"),
                        () -> tmpConfirm.confirm("B")));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("carsIndustries"),
                        () -> tmpConfirm.confirm("C"))
                        .setTheme(blackButton));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("oilfields"),
                        () -> tmpConfirm.confirm("D")));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("drills"),
                        () -> tmpConfirm.confirm("E"))
                        .setTheme(blackButton));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("pumps"),
                        () -> tmpConfirm.confirm("F")));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("cars"),
                        () -> tmpConfirm.confirm("G"))
                        .setTheme(blackButton));

        // Space
        contentPanel.addComponent(new EmptySpace());

        // Options pt. 2
        contentPanel.addComponent(
                new Label(" " + gameProperties.langMap.get("otherPossibilities") + " ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.BLUE)));

        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("nextPlayer"),
                        () -> tmpConfirm.confirm("H")));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("attemptSabotage"),
                        () -> tmpConfirm.confirm("I"))
                        .setTheme(blackButton));
        contentPanel.addComponent(
                new Button(
                        gameProperties.langMap.get("changePrices"),
                        () -> tmpConfirm.confirm("J")));

        tmpConfirm.waitForConfirm();
        contentPanel.removeAllComponents();

        // Redirect to the valid menu
        switch (tmpConfirm.getAction()) {
            case "A" -> // Drills productions
                    gameProperties.drillsIndustryOperations.buyIndustryMenu(player,
                            gameProperties.window, gameProperties.langMap);
            case "B" -> // Pumps productions
                    gameProperties.pumpsIndustryOperations.buyIndustryMenu(player,
                            gameProperties.window, gameProperties.langMap);
            case "C" -> // Cars productions
                    gameProperties.carsIndustryOperations.buyIndustryMenu(player,
                            gameProperties.window, gameProperties.langMap);
            case "D" -> // Oilfields
                    gameProperties.oilfieldOperations.buyOilfieldMenu(player,
                            gameProperties.window, gameProperties.langMap);
            case "E" -> // Drills
                    gameProperties.drillsIndustryOperations.buyProductsMenu(player,
                            gameProperties.oilfieldOperations.getOilfields(),
                            gameProperties.window,
                            gameProperties.langMap);
            case "F" -> // Pumps
                    gameProperties.pumpsIndustryOperations.buyProductsMenu(player,
                            gameProperties.oilfieldOperations.getOilfields(),
                            gameProperties.window, gameProperties.langMap);
            case "G" -> // Cars
                    gameProperties.carsIndustryOperations.buyProductsMenu(player,
                            gameProperties.oilfieldOperations.getOilfields(),
                            gameProperties.window, gameProperties.langMap);
            case "H" -> {
                // Pass
            }
            case "I" -> // Attempt sabotage
                    Sabotage.doSabotage(player, gameProperties);
            case "J" -> // Change prices
                    ChangePrices.menu(player, gameProperties);
            default -> System.out.println("No value provided. This could be an error.");
        }
    }

    public static void timeBuffor() throws InterruptedException {
        Thread.sleep(1);
    }
}
