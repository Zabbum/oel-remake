package com.zabbum.oelremake;

import java.util.Random;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.ImageComponent;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.table.Table;

public class Game {

    // Insert empty space
    public static EmptySpace emptyLine(int horizontalSpan) {
        EmptySpace emptySpace = new EmptySpace();
        emptySpace.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(horizontalSpan));

        return emptySpace;
    }
    
    // Finish game
    public static void finishGame(GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.removeAllComponents();
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false,
                TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.ANSI.BLACK, TextColor.ANSI.BLUE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
                TextColor.ANSI.ANSI.BLACK
            )
        );

        contentPanel.addComponent(new Label("A OTO REZULTATY WALKI:"));

        contentPanel.addComponent(new EmptySpace());

        // Create table for results
        Table<String> resultsTable = new Table<String>("GRACZ", "POZYCZKA", "KAPITA£");

        // Add every player to table
        for (Player player : gameProperties.players) {
            resultsTable.getTableModel().addRow(player.name, String.valueOf(player.debt), String.valueOf(player.balance));
        }

        contentPanel.addComponent(resultsTable);

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label("GRATULUJE ZWYCIEZCOM !"));

        Button confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);
    }

    // Display shitty OEL logo
    // TODO: Display pretty OEL logo
    public static void oelLogo(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;
        Game.timeBuffor();

        Label oelLogo = new Label("OEL");
            oelLogo.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING,
                true,
                false,
                2,
                1));
            contentPanel.addComponent(oelLogo);
            contentPanel.addComponent(Game.emptyLine(2));
    }
    
    // Prompt for players amount
    public static void promptPlayerAmount(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;

        contentPanel.addComponent(new Label("ILU BEDZIE KAPITALISTOW (2-6)"));
        TextBox playerAmountTextBox = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
        contentPanel.addComponent(playerAmountTextBox);
        playerAmountTextBox.takeFocus();

        contentPanel.addComponent(new EmptySpace());
        
        contentPanel.addComponent(
            new Button("GOTOWE", () -> {
                try {
                    gameProperties.playerAmount = Integer.parseInt(playerAmountTextBox.getText());                    
                } catch (NumberFormatException e) {}
            })
        );

        while (!(gameProperties.playerAmount > 1 && gameProperties.playerAmount < 7)) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(gameProperties.playerAmount);
        contentPanel.removeAllComponents();
    }

    // Intro info for player and prompt for names
    public static void promptPlayerNames(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;

        contentPanel.setLayoutManager(new GridLayout(1));

        contentPanel.addComponent(new Label("ZNAJDUJEMY SIE OBECNIE W ROKU:"));
        Game.timeBuffor();
        contentPanel.addComponent(new EmptySpace());

        // TODO: Fancy 1986
        contentPanel.addComponent(new Label("1986"));
        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label("GRA KONCZY SIE W ROKU 2020"));
        contentPanel.addComponent(new Label("I ODBEDZIE SIE PRZY UDZIALE:"));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("PROSZE WPISAC IMIONA"));

        // Prompt for names
        Panel promptPanel = new Panel(new GridLayout(2));
        TextBox[] playerNames = new TextBox[gameProperties.playerAmount];
        
        // Display correct amount of textboxes
        for (int i = 0; i < gameProperties.playerAmount; i++) {
            promptPanel.addComponent(new Label("?"));
            playerNames[i] = new TextBox("Gracz " + String.valueOf(i+1));
            promptPanel.addComponent(playerNames[i]);
        }

        contentPanel.addComponent(promptPanel);
        // Set focus to first textbox
        playerNames[0].takeFocus();

        // Confirmation button
        gameProperties.tmpConfirm = false;
        contentPanel.addComponent(
            new Button("GOTOWE", new Runnable() {
                @Override
                public void run() {
                    gameProperties.players = new Player[gameProperties.playerAmount];
                    gameProperties.tmpConfirm = true;
                }
            })
        );

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Create player objects
        for (int i = 0; i < playerNames.length; i++) {
            System.out.println(playerNames[i].getText());
            gameProperties.players[i] = new Player(playerNames[i].getText());
        }

        // Inform about money amount
        contentPanel.removeAllComponents();
        contentPanel.addComponent(new Label("KAZDY GRACZ POSIADA 124321 $ KAPITA£U"));
        contentPanel.addComponent(new EmptySpace());
        Button confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Inform about money amount and oil prices
    public static void moneyInfo(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;

        // Inform about money amount
        gameProperties.window.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT));
        contentPanel.addComponent(new Label("WYGRYWA TEN, KTO OSIAGNIE NAJWIEKSZY"));
        Game.timeBuffor();
        contentPanel.addComponent(new Label("KAPITAL NA KONCU GRY"));
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
        gameProperties.window.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.RED));

        // Inform user about oil prices
        Panel titlePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.YELLOW_BRIGHT));
        titlePanel.addComponent(new Label("PRZEWIDYWANE CENY ROPY NA RYNKU"));
        titlePanel.addComponent(new Label("(JAKIE TRENDY W KOLEJNYCH LATACH :)"));
        contentPanel.addComponent(titlePanel);
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new EmptySpace());

        // Draw oil prices graph
        ImageComponent oilGraph = new ImageComponent();
        oilGraph.setTextImage(Oil.oilGraph(gameProperties, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.RED));
        contentPanel.addComponent(oilGraph);
        contentPanel.addComponent(new EmptySpace());

        // Reduce oil prices
        Oil.reducePrices(gameProperties);
        for (double price : gameProperties.oilPrices) {
            System.out.println(price);
        }

        Button confirmButton1 = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });

        contentPanel.addComponent(confirmButton1);
        confirmButton1.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Play single round
    public static void playRound(GameProperties gameProperties) {
        gameProperties.currentRound++;

        for (Player player : gameProperties.players) {
            // Display the main menu
            mainMenu(player, gameProperties);

            // Redirect to the valid menu
            switch (gameProperties.tmpAction) {
                case "A" -> {
                    // Drills productions
                    DrillsIndustry.buyIndustry(player, gameProperties);
                }
                case "B" -> {
                    // Pumps productions
                    PumpsIndustry.buyIndustry(player, gameProperties);
                }
                case "C" -> {
                    // Cars productions
                    CarsIndustry.buyIndustry(player, gameProperties);
                }
                case "D" -> {
                    // Oilfields
                    Oilfield.buyField(player, gameProperties);
                }
                case "E" -> {
                    // Drills
                    DrillsIndustry.buyProduct(player, gameProperties);
                }
                case "F" -> {
                    // Pumps
                    PumpsIndustry.buyProduct(player, gameProperties);
                }
                case "G" -> {
                    // Cars
                    CarsIndustry.buyProduct(player, gameProperties);
                }
                case "H" -> {
                    // Pass
                }
                case "I" -> {
                    // Attempt sabotage
                    Sabotage.doSabotage(player, gameProperties);
                }
                case "J" -> {
                    // Change prices
                }
                default -> {
                    System.out.println("No value provided. This could be an error.");
                }
            }
        }
        endRound(gameProperties);
    }

    // Actions to take at end of the round
    static void endRound(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;
        Random random = new Random();

        // Actions for every player
        for (Player player : gameProperties.players) {
            
            // Oilfields overview
            for (Oilfield oilfield : gameProperties.oilfields) {
                // Clean up
                contentPanel.removeAllComponents();

                // If user is not owner of the field, move on to the next
                if (oilfield.ownership != player) {
                    continue;
                }

                // If oilfield is able to pump oil
                if (oilfield.canExtractOil) {
                    // Routine actions
                    if (oilfield.oilExtracted <= oilfield.oilAmount) {
                        oilfield.oilAvailabletoSell += 8000 * oilfield.pumpsAmount;
                        oilfield.oilExtracted += 8000 * oilfield.pumpsAmount;
                    }

                    // Prepare graphical settings
                    contentPanel.setLayoutManager(new GridLayout(1));
                    gameProperties.window.setTheme(
                        SimpleTheme.makeTheme(false,
                            TextColor.ANSI.BLACK, TextColor.ANSI.YELLOW,
                            TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK,
                            TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.YELLOW
                        )
                    );

                    // Inform user
                    contentPanel.addComponent(new Label("  POLE NAFTOWE : ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)));
                    contentPanel.addComponent(new Label(oilfield.name)
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));
                    contentPanel.addComponent(new Label("▔".repeat(17)));
                    contentPanel.addComponent(new Label(" W£A$CICIEL POLA ")
                        .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK)));
                    contentPanel.addComponent(new Label(player.name));
                    contentPanel.addComponent(new Label("▔".repeat(17)));
                    
                    contentPanel.addComponent(new Panel(new GridLayout(2))
                        .addComponent(new Label("ROK: "))
                        .addComponent(new Label(String.valueOf(gameProperties.currentRound + 1985))
                            .setTheme(new SimpleTheme(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.YELLOW))
                        )
                    );
                    
                    contentPanel.addComponent(new EmptySpace());
                    contentPanel.addComponent(new Label("MI$ & RY$ & SONS -")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));
                    contentPanel.addComponent(new Label("CENA SPRZEDAZY ROPY = " +
                        String.valueOf(gameProperties.oilPrices[gameProperties.currentRound - 1]) + " $")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLUE, TextColor.ANSI.YELLOW)));

                    contentPanel.addComponent(new EmptySpace());

                    contentPanel.addComponent(new Panel(new GridLayout(2))
                        .addComponent(new Label("ILO$C POMP"))
                        .addComponent(new Label(": " + String.valueOf(oilfield.pumpsAmount)))
                        .addComponent(new Label("WYPOMPOWANO")
                            .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label(": " + String.valueOf(oilfield.pumpsAmount))
                            .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label("ILO$C WAGONOW"))
                        .addComponent(new Label(": " + String.valueOf(oilfield.carsAmount)))
                        .addComponent(new Label("MAX WYWOZ")
                            .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label(": " + String.valueOf(oilfield.carsAmount * 7000))
                            .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.YELLOW)))
                        .addComponent(new Label("TWOJ KAPITA£"))
                        .addComponent(new Label(": " + String.valueOf(player.balance)))
                    );

                    contentPanel.addComponent(new EmptySpace());

                    // If oilfield is out of oil, inform user
                    if (oilfield.oilExtracted > oilfield.oilAmount) {
                        contentPanel.addComponent(new Label("ZROD£O WYCZERPANE!"));
                        contentPanel.addComponent(new EmptySpace());
                    }

                    // If can sell oil
                    if (oilfield.oilAvailabletoSell > 0) {
                        // Ask for amount of oil
                        contentPanel.addComponent(new Label("ILE LITROW ROPY SPRZEDAJESZ?"));

                        gameProperties.tmpActionInt = -1;
                        TextBox oilAmountToSellTextBox = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
                        
                        contentPanel.addComponent(oilAmountToSellTextBox);
                        oilAmountToSellTextBox.takeFocus();

                        contentPanel.addComponent(
                            new Button("GOTOWE", () -> {
                                try {
                                    gameProperties.tmpActionInt = Integer.parseInt(oilAmountToSellTextBox.getText());
                                } catch (NumberFormatException e) {}
                            })
                        );

                        // If choice is valid, let it be
                        while (!(gameProperties.tmpActionInt > -1 &&
                            gameProperties.tmpActionInt <= oilfield.carsAmount * 7000 &&
                            gameProperties.tmpActionInt <= oilfield.oilAvailabletoSell
                        )) {
                            try {
                                Thread.sleep(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Take actions
                        int oilAmount = gameProperties.tmpActionInt;
                        player.balance += oilAmount * gameProperties.oilPrices[gameProperties.currentRound - 1];
                        oilfield.oilAvailabletoSell -= oilAmount;
                    }

                    // If cannot sell oil
                    else {
                        Button confirmButton = new Button("GOTOWE", () -> {
                            gameProperties.tmpConfirm = true;
                        });
                        contentPanel.addComponent(confirmButton);
                        confirmButton.takeFocus();
                
                        // Wait for confirmation
                        Game.waitForConfirm(gameProperties);
                    }

                    // Clean up
                    contentPanel.removeAllComponents();
                    gameProperties.tmpActionInt = -1;
                }

                // If oilfield is not able to pump oil
                else {
                    contentPanel.setLayoutManager(new GridLayout(1));
                    gameProperties.window.setTheme(
                        SimpleTheme.makeTheme(false,
                            TextColor.ANSI.GREEN, TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.GREEN,
                            TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.WHITE_BRIGHT
                        )
                    );

                    // Inform user about status
                    contentPanel.addComponent(new Label("WIERCENIE NA POLU:")
                        .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));
                    
                    contentPanel.addComponent(new Label(oilfield.name)
                    .setTheme(new SimpleTheme(TextColor.ANSI.GREEN_BRIGHT, TextColor.ANSI.WHITE_BRIGHT)));

                    contentPanel.addComponent(new EmptySpace());

                    contentPanel.addComponent(new Panel(new GridLayout(2))
                        .addComponent(new Label("W£ASNO$C: ")
                            .setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.WHITE_BRIGHT)))
                        .addComponent(new Label(player.name)
                            .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT)))
                    );

                    contentPanel.addComponent(new EmptySpace());

                    contentPanel.addComponent(new Label("TWOI LUDZIE Z POLA NAFTOWEGO")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT)));
                    contentPanel.addComponent(new Label("MELDUJA CO NASTEPUJE:")
                        .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT)));

                    contentPanel.addComponent(new EmptySpace());

                    // If no drills, inform player
                    if (oilfield.drillsAmount <= 0) {
                        contentPanel.addComponent(new Label("WIERCENIA NIEMOZLIWE - BRAK WIERTE£!")
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));
                        contentPanel.addComponent(new Label(" TRZEBA CO$ PRZEDSIEWZIAC!")
                            .setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.WHITE_BRIGHT)));
                        contentPanel.addComponent(new EmptySpace());
                    }

                    // If there are drills, drill
                    else {
                        oilfield.drillsAmount -= 500;
                        oilfield.currentDepth += 500 - (random.nextInt(30)+1);
                    }

                    // In both cases, inform about current progess
                    contentPanel.addComponent(new Label("AKTUALNA G£EBOKO$C WIERCEN: " + oilfield.currentDepth + "M"));
                    contentPanel.addComponent(new EmptySpace());
                    contentPanel.addComponent(new Label("JESZCZE CI STARCZY NA: " + oilfield.drillsAmount + "M"));
                    contentPanel.addComponent(new EmptySpace());

                    // If reached the point that makes it available to extract oil,
                    // take actions
                    if (oilfield.currentDepth >= oilfield.requiredDepth) {
                        contentPanel.addComponent(new Label("TRYSNE£O!!!"));
                        contentPanel.addComponent(new EmptySpace());
                        oilfield.canExtractOil = true;
                    }

                    // Button for confirmation
                    Button confirmButton = new Button("GOTOWE", () -> {
                        gameProperties.tmpConfirm = true;
                    });
                    contentPanel.addComponent(confirmButton);
                    confirmButton.takeFocus();
            
                    // Wait for confirmation
                    Game.waitForConfirm(gameProperties);
                    
                    // Clean up
                    contentPanel.removeAllComponents();
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

    static void overview(GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
                TextColor.ANSI.WHITE_BRIGHT)
            );

        // Inform about current balances
        contentPanel.addComponent(new Label("ROK: " + String.valueOf(1985 + gameProperties.currentRound)));
        contentPanel.addComponent(new EmptySpace());

        Panel playersPanel = new Panel(new GridLayout(2));
        for (Player player : gameProperties.players) {
            playersPanel.addComponent(new Label(player.name));
            playersPanel.addComponent(new Label("KAPITA£: " + String.valueOf(player.balance)));
        }

        contentPanel.addComponent(playersPanel);

        contentPanel.addComponent(new EmptySpace());

        Button confirmButton = new Button("GOTOWE", () -> {
            gameProperties.tmpConfirm = true;
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();

        // Wait for confirmation
        Game.waitForConfirm(gameProperties);

        // Clean up
        contentPanel.removeAllComponents();
    }

    // Debt system
    static void debt(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        
        // If user has a loan
        if (player.debt > 0) {
            // Pay it off
            player.balance -= 5000;
            player.debt -= 3000;

            // Display debtor menu

            // Prepare new graphical settings
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            gameProperties.window.setTheme(
                SimpleTheme.makeTheme(false,
                    TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLACK,
                    TextColor.ANSI.BLACK, TextColor.ANSI.BLUE_BRIGHT,
                    TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
                    TextColor.ANSI.BLACK)
                );
            
            contentPanel.addComponent(new Label(" 'SONS & FATHERS` : KARTA KREDYTOWA ")
                .setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.BLUE_BRIGHT)));
            
            contentPanel.addComponent(new EmptySpace());

            contentPanel.addComponent(new Label("D£UZNIK: " + player.name));

            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(new EmptySpace());

            Panel loanPanel = new Panel(new GridLayout(2));

            loanPanel.addComponent(new Label("DO SP£ACENIA:"));
            loanPanel.addComponent(new Label(String.valueOf(player.debt) + " $"));
            loanPanel.addComponent(new Label("KOLEJNA RATA:"));
            loanPanel.addComponent(new Label("5000 $"));
            loanPanel.addComponent(new Label("TWOJ KAPITA£:"));
            loanPanel.addComponent(new Label(String.valueOf(player.balance) + " $"));

            contentPanel.addComponent(loanPanel);

            contentPanel.addComponent(new EmptySpace());

            Button confirmButton = new Button("GOTOWE", () -> {
                gameProperties.tmpConfirm = true;
            });
            contentPanel.addComponent(confirmButton);
            confirmButton.takeFocus();
    
            // Wait for confirmation
            Game.waitForConfirm(gameProperties);
    
            // Clean up
            contentPanel.removeAllComponents();
        }

        // If user has negative money, take debt
        if (player.balance < 0) {
            // Prepare new graphical settings
            contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
            gameProperties.window.setTheme(
                SimpleTheme.makeTheme(false,
                    TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK,
                    TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT,
                    TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
                    TextColor.ANSI.BLACK)
                );

            // Inform about loan
            contentPanel.addComponent(new Label("Jest kiepsko !!! " + player.name));
            contentPanel.addComponent(new Label("Aby grac dalej musisz zapozyczyc sie"));
            contentPanel.addComponent(new Label("w banku 'Sons & Fathers' na sume"));
            contentPanel.addComponent(new Label("20000 dolarow. Splata kredytu na rok"));
            contentPanel.addComponent(new Label("wynosi 5000 dolarow."));

            contentPanel.addComponent(new EmptySpace());

            // Take debt
            player.debt += 20000;
            player.balance += 20000;

            Button confirmButton = new Button("GOTOWE", () -> {
                gameProperties.tmpConfirm = true;
            });
            contentPanel.addComponent(confirmButton);
            confirmButton.takeFocus();
    
            // Wait for confirmation
            Game.waitForConfirm(gameProperties);
    
            // Clean up
            contentPanel.removeAllComponents();
        }
    }

    // Display main menu and get action
    public static void mainMenu(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.MAGENTA,
            TextColor.ANSI.MAGENTA, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN, TextColor.ANSI.MAGENTA)
            );

        // Create theme for black buttons not to make them different when selected
        Theme blackButton = SimpleTheme.makeTheme(false, TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA,
        TextColor.ANSI.BLACK, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN, TextColor.ANSI.MAGENTA);

        // Display options
        contentPanel.addComponent(new Label("DECYZJA NALEZY DO CIEBIE!")
            .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.WHITE_BRIGHT)));

        contentPanel.addComponent(new EmptySpace());

        contentPanel.addComponent(new Label("GRACZ: " + player.name + " $= " + player.balance)
            .setTheme(new SimpleTheme(TextColor.ANSI.CYAN_BRIGHT, TextColor.ANSI.MAGENTA)));
        
        contentPanel.addComponent(new EmptySpace());
        
        // Options pt. 1
        contentPanel.addComponent(new Label(" KUPOWANIE ")
        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.BLUE)));

        Component firstButton = new Button("FABRYKI WIERTE£", () -> {
            gameProperties.tmpAction = "A";
            gameProperties.tmpConfirm = true;
        }).setTheme(blackButton);
        contentPanel.addComponent(firstButton);
        ((Interactable)firstButton).takeFocus();

        contentPanel.addComponent(new Button("ZAK£ADY POMP", () -> {
            gameProperties.tmpAction = "B";
            gameProperties.tmpConfirm = true;
        }));
        contentPanel.addComponent(new Button("FIRMY WAGONOWE", () -> {
            gameProperties.tmpAction = "C";
            gameProperties.tmpConfirm = true;
        }).setTheme(blackButton));
        contentPanel.addComponent(new Button("POLA NAFTOWE", () -> {
            gameProperties.tmpAction = "D";
            gameProperties.tmpConfirm = true;
        }));
        contentPanel.addComponent(new Button("WIERT£A", () -> {
            gameProperties.tmpAction = "E";
            gameProperties.tmpConfirm = true;
        }).setTheme(blackButton));
        contentPanel.addComponent(new Button("POMPY", () -> {
            gameProperties.tmpAction = "F";
            gameProperties.tmpConfirm = true;
        }));
        contentPanel.addComponent(new Button("WAGONY", () -> {
            gameProperties.tmpAction = "G";
            gameProperties.tmpConfirm = true;
        }).setTheme(blackButton));

        // Space
        contentPanel.addComponent(new EmptySpace());

        // Options pt. 2
        contentPanel.addComponent(new Label(" POZOSTA£E MOZLIWO$CI ")
        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.BLUE)));

        contentPanel.addComponent(new Button("NASTEPNY GRACZ", () -> {
            gameProperties.tmpAction = "H";
            gameProperties.tmpConfirm = true;
        }));
        contentPanel.addComponent(new Button("PROBA SABOTAZU", () -> {
            gameProperties.tmpAction = "I";
            gameProperties.tmpConfirm = true;
        }).setTheme(blackButton));
        contentPanel.addComponent(new Button("ZMIANA CENY", () -> {
            gameProperties.tmpAction = "J";
            gameProperties.tmpConfirm = true;
        }));

        Game.waitForConfirm(gameProperties);
        contentPanel.removeAllComponents();
    }

    public static void waitForConfirm(GameProperties gameProperties) {
        while (!gameProperties.tmpConfirm) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameProperties.tmpConfirm = false;
    }

    public static void timeBuffor() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
