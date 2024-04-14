package com.zabbum.oelremake;

import java.util.regex.Pattern;
import java.lang.NumberFormatException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.ImageComponent;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class Game {

    // Insert empty space
    public static EmptySpace emptyLine(int horizontalSpan) {
        EmptySpace emptySpace = new EmptySpace();
        emptySpace.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(horizontalSpan));

        return emptySpace;
    }

    // Display shitty OEL logo
    // TODO: Display pretty OEL logo
    public static void oelLogo(GameProperties gameProperties) {
        Panel contentPanel = gameProperties.contentPanel;

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
                    // Drill productions
                    DrillsIndustry.buyIndustry(player, gameProperties);
                }
                case "B" -> {
                    // Pump productions
                }
                case "C" -> {
                    // Cars productions
                }
                case "D" -> {
                    // Oilfields
                }
                case "E" -> {
                    // Drills
                }
                case "F" -> {
                    // Pumps
                }
                case "G" -> {
                    // Cars
                }
                case "H" -> {
                    // Pass
                }
                case "I" -> {
                    // Attempt sabotage
                }
                case "J" -> {
                    // Change prices
                }
                default -> {
                    System.out.println("No value provided. This could be an error.");
                }
            }
        }
    }

    // Display main menu and get action
    public static void mainMenu(Player player, GameProperties gameProperties) {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        gameProperties.window.setTheme(new SimpleTheme(TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.MAGENTA));

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

        contentPanel.addComponent(new Button("FABRYKI WIERTE£", () -> {
            gameProperties.tmpAction = "A";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA)));
        contentPanel.addComponent(new Button("ZAK£ADY POMP", () -> {
            gameProperties.tmpAction = "B";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }));
        contentPanel.addComponent(new Button("FIRMY WAGONOWE", () -> {
            gameProperties.tmpAction = "C";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA)));
        contentPanel.addComponent(new Button("POLA NAFTOWE", () -> {
            gameProperties.tmpAction = "D";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }));
        contentPanel.addComponent(new Button("WIERT£A", () -> {
            gameProperties.tmpAction = "E";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA)));
        contentPanel.addComponent(new Button("POMPY", () -> {
            gameProperties.tmpAction = "F";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }));
        contentPanel.addComponent(new Button("WAGONY", () -> {
            gameProperties.tmpAction = "G";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA)));

        // Space
        contentPanel.addComponent(new EmptySpace());

        // Options pt. 2
        contentPanel.addComponent(new Label(" POZOSTA£E MOZLIWO$CI ")
        .setTheme(new SimpleTheme(TextColor.ANSI.MAGENTA, TextColor.ANSI.BLUE)));

        contentPanel.addComponent(new Button("NASTEPNY GRACZ", () -> {
            gameProperties.tmpAction = "H";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }));
        contentPanel.addComponent(new Button("WAGONY", () -> {
            gameProperties.tmpAction = "I";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
        }).setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.MAGENTA)));
        contentPanel.addComponent(new Button("ZMIANA CENY", () -> {
            gameProperties.tmpAction = "J";
            gameProperties.tmpConfirm = true;
            contentPanel.removeAllComponents();
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
