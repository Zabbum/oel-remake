package com.zabbum.oelremake;

import java.io.IOException;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.ImageComponent;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;

public class Game {

    public static void playRound(Player[] players, Scanner scanner, Oilfield[] oilfields, CarsProd[] carsProds, PumpProd[] pumpProds, DrillProd[] drillProds, double[] oilPrices) {
        Game.currentRound += 1;

        for (Player player : players) {
            // Display the main menu
            mainMenu(player, scanner);

            // Get the action
            char action = ' ';
            char[] possibleActions = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
            // Verify that the action is correct,
            // if not then wait for another
            while (Arrays.binarySearch(possibleActions, action) < 0) {
                String input = scanner.nextLine().toUpperCase();
                if (input.length() >= 1) {
                    action = input.charAt(0);
                }
            }
            System.out.println();
            
            // Redirect to the valid menu
            switch (action) {
                case 'A' -> {
                    // Drill productions
                    DrillProd.buyIndustry(player, scanner, drillProds);
                }
                case 'B' -> {
                    // Pump productions
                    PumpProd.buyIndustry(player, scanner, pumpProds);
                }
                case 'C' -> {
                    // Cars productions
                    CarsProd.buyIndustry(player, scanner, carsProds);
                }
                case 'D' -> {
                    // Oilfields
                    Oilfield.buyField(player, scanner, oilfields);
                }
                case 'E' -> {
                    // Drills
                    DrillProd.buyProduct(player, scanner, drillProds, oilfields);
                }
                case 'F' -> {
                    // Pumps
                    PumpProd.buyProduct(player, scanner, pumpProds, oilfields);
                }
                case 'G' -> {
                    // Cars
                    CarsProd.buyProduct(player, scanner, carsProds, oilfields);
                }
                case 'H' -> {
                    // Pass
                }
                case 'I' -> {
                    // Attempt sabotage
                    Sabotage.doSabotage(player, scanner, oilfields, pumpProds, carsProds, drillProds);
                }
                case 'J' -> {
                    // Change prices
                    ChangePrices.menu(player, scanner, pumpProds, carsProds, drillProds);
                }
                default -> {
                    System.out.println(ANSI.RED + "No value provided. This could be an error.\n" + ANSI.RESET);
                }
            }
        }
        endRound(players, scanner, oilfields, oilPrices);
    }

    // Actions to take at end of the round
    static void endRound(Player[] players, Scanner scanner, Oilfield[] oilfields, double[] oilPrices) {
        Random random = new Random();

        // Actions for every player
        for (Player player : players) {
            
            // Oilfields overview
            for (Oilfield oilfield : oilfields) {
                // If user is not owner of the field, move on to the next
                if (oilfield.ownership != player) {
                    continue;
                }

                // If oilfield is able to pump oil
                if (oilfield.canExtractOil) {
                    // Routine actions
                    if (oilfield.oilExtracted <= oilfield.oilAmount) {
                        oilfield.oilAvailabletoSell += 8000 * oilfield.pumpAmount;
                        oilfield.oilExtracted += 8000 * oilfield.pumpAmount;
                    }

                    // Inform user
                    System.out.println(ANSI.BLACK_BACKGROUND + ANSI.YELLOW + "Pole naftowe:" + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + oilfield.getName() + ANSI.RESET);
                    System.out.println(ANSI.BLACK_BACKGROUND + ANSI.YELLOW + "Właściciel pola: "+ ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + player.name + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Rok: " + ANSI.WHITE + (Game.currentRound + 1986) + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "Miś & Ryś & Sons" + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLUE + "Cena sprzedaży ropy = " + oilPrices[Game.currentRound - 1] + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Ilość pomp\t" + oilfield.pumpAmount + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.GREEN_BRIGHT + "Wypompowano\t" + oilfield.oilAvailabletoSell + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Ilość wagonów\t" + oilfield.carsAmount + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.GREEN_BRIGHT + "Max wywóz\t" + (oilfield.carsAmount*7000) + ANSI.RESET);
                    System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Twój kapitał\t" + player.balance + "$" + ANSI.RESET);
                    
                    // If oilfield is out of oil, inform user
                    if (oilfield.oilExtracted > oilfield.oilAmount) {
                        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Źródło wyczerpane!" + ANSI.RESET);
                    }

                    // If can sell oil
                    if (oilfield.oilAvailabletoSell > 0) {
                        // Ask for amount of oil
                        System.out.println(ANSI.YELLOW_BACKGROUND + ANSI.BLACK + "Ile litrów ropy sprzedajesz?" + ANSI.RESET);
                        int oilAmount = Prompt.oilAmount(scanner, oilfield.carsAmount, oilfield.oilAvailabletoSell);

                        // Take actions
                        player.balance += oilAmount * oilPrices[Game.currentRound - 1];
                        oilfield.oilAvailabletoSell -= oilAmount;
                    }
                }

                // If oilfield is not able to pump oil
                else {
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Wiercenie na polu:" + ANSI.RESET);
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN_BRIGHT + oilfield.getName() + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.PURPLE_BRIGHT + "Właśność: " + ANSI.BLACK + player.name + ANSI.RESET);
                    System.out.println();
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK + "Twoi ludzie z pola naftowego meldują co następuje:" + ANSI.RESET);
                    System.out.println();

                    // If no drills, inform user
                    if (oilfield.drillAmount <= 0) { 
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Wiercenia niemożliwe - brak wierteł!" + ANSI.RESET);
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.RED + "Trzeba coś przedsięwziąć!" + ANSI.RESET);
                        System.out.println();
                    }

                    // If there are drills, drill
                    else {
                        oilfield.drillAmount -= 500;
                        oilfield.currentDepth += 500 - (random.nextInt(30)+1);
                    }

                    // In both cases
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Aktualna głębokość wierceń: " + oilfield.currentDepth + "m" + ANSI.RESET);
                    System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Jeszcze starczy ci na: " + oilfield.drillAmount + "m" + ANSI.RESET);
                    
                    // If reached the point that makes it available to extract oil,
                    // take actions
                    if (oilfield.currentDepth >= oilfield.requiredDepth) {
                        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.GREEN + "Trysnęło!!!" + ANSI.RESET);
                        oilfield.canExtractOil = true;
                    }
                    System.out.println();
                }
            }

            // Debt system
            debt(player);

        }
    }

    static void debt(Player player) {
        // If user has a loan, pay it off
        if (player.debt > 0) {
            player.balance -= 5000;
            player.debt -= 3000;

            // Menu of loans
            System.out.println(ANSI.BLUE_BACKGROUND_BRIGHT + ANSI.BLACK + " \"Sons & Fathers\" : Karta kredytowa " + ANSI.RESET);
            System.out.println();
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLUE_BRIGHT + "Dłużnik: " + player.name + ANSI.RESET);
            System.out.println();
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLUE_BRIGHT + "Do spłacenia:\t" + player.debt + "$" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLUE_BRIGHT + "Kolejna spłata:\t5000$" + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.BLUE_BRIGHT + "Twój kapitał:\t" + player.balance + "$" + ANSI.RESET);
            System.out.println();
        }

        // If user has problems with money, take actions
        if (player.balance < 0) {
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.WHITE + "Jest kiepsko !!! " + player.name + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.WHITE + "Aby grać dalej, musisz zapożyczyć się w banku \"Sons & Fathers\" na sumę 20000 dolarów." + ANSI.RESET);
            System.out.println(ANSI.BLACK_BACKGROUND + ANSI.WHITE + "Spłata kredytu na rok wynosi 5000$ (spłaca 3000$ długu)." + ANSI.RESET);
            System.out.println();

            player.balance += 20000;
            player.debt += 20000;
        }
    }

    public static void mainMenu(Player player, Scanner scanner) {
        // Display the menu
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.PURPLE + "DECYZJA NALEŻY DO CIEBIE!" + ANSI.RESET);
        System.out.println(ANSI.CYAN + "Gracz: " + player.name + " $= " + player.balance + ANSI.RESET);
        System.out.println();
        System.out.println(ANSI.BLUE_BACKGROUND + " KUPOWANIE " + ANSI.RESET);
        System.out.println("A = Fabryki wierteł");
        System.out.println("B = Zakłady pomp");
        System.out.println("C = Firmy wagonowe");
        System.out.println("D = Pola naftowe");
        System.out.println("E = Wiertła");
        System.out.println("F = Pompy");
        System.out.println("G = Wagony");
        System.out.println();
        System.out.println(ANSI.BLUE_BACKGROUND + " POZOSTAŁE MOŻLIWOŚCI " + ANSI.RESET);
        System.out.println("H = Następny gracz");
        System.out.println("I = Próba sabotażu");
        System.out.println("J = Zmiana ceny");
        System.out.println();
        System.out.println(ANSI.WHITE_BACKGROUND + ANSI.BLACK + "PRZYCIŚNIJ DOWOLNY KLAWISZ" + ANSI.RESET);
    }

    // Insert empty space
    public static EmptySpace emptyLine(int horizontalSpan) {
        EmptySpace emptySpace = new EmptySpace();
        emptySpace.setLayoutData(GridLayout.createHorizontallyFilledLayoutData(horizontalSpan));

        return emptySpace;
    }

    // Display shitty OEL logo
    // TODO: Display pretty OEL logo
    public static void oelLogo(Panel contentPanel) {
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
    public static void promptPlayerAmount(Panel contentPanel, GameProperties gameProperties) {
        contentPanel.addComponent(new Label("ILU BEDZIE KAPITALISTOW (2-6)"));
            TextBox playerAmountTextBox = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
            contentPanel.addComponent(playerAmountTextBox);

            contentPanel.addComponent(new EmptySpace());
            
            contentPanel.addComponent(
                new Button("GOTOWE", () -> {
                    try {
                        gameProperties.playerAmount = Integer.parseInt(playerAmountTextBox.getText());
                        if (gameProperties.playerAmount > 1 && gameProperties.playerAmount < 7) {
                            System.out.println(gameProperties.playerAmount);
                            contentPanel.removeAllComponents();
                            introInfo(contentPanel, gameProperties);
                        }
                        
                    } catch (NumberFormatException e) {}
                })
            );
    }

    // Intro info for player
    public static void introInfo(Panel contentPanel, GameProperties gameProperties) {
        contentPanel.setLayoutManager(new GridLayout(1));
        contentPanel.addComponent(new Label("ZNAJDUJEMY SIE OBECNIE W ROKU:"));

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
        contentPanel.addComponent(
            new Button("GOTOWE", new Runnable() {
                @Override
                public void run() {
                    gameProperties.players = new Player[gameProperties.playerAmount];

                    // Create player objects
                    for (int i = 0; i < playerNames.length; i++) {
                        System.out.println(playerNames[i].getText());
                        gameProperties.players[i] = new Player(playerNames[i].getText());
                        contentPanel.removeAllComponents();
                        // Inform about money amount
                        contentPanel.addComponent(new Label("KAZDY GRACZ POSIADA 124321 $ KAPITA£U"));
                        contentPanel.addComponent(new EmptySpace());
                        Button confirmButton = new Button("GOTOWE", () -> {
                            contentPanel.removeAllComponents();
                            furtherInfo(contentPanel, gameProperties);
                        });
                        contentPanel.addComponent(confirmButton);
                        confirmButton.takeFocus();
                    }
                }
            })
        );
    }

    // Inform about money amount and oil prices
    public static void furtherInfo(Panel contentPanel, GameProperties gameProperties) {
        // Inform about money amount
        gameProperties.window.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT));
        contentPanel.addComponent(new Label("WYGRYWA TEN, KTO OSIAGNIE NAJWIEKSZY"));
        contentPanel.addComponent(new Label("KAPITAL NA KONCU GRY"));
        contentPanel.addComponent(new EmptySpace());
        Button confirmButton = new Button("GOTOWE", () -> {
            contentPanel.removeAllComponents();
            gameProperties.window.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.RED));

            Panel titlePanel = new Panel(new LinearLayout(Direction.VERTICAL));
            titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.RED, TextColor.ANSI.YELLOW_BRIGHT));
            titlePanel.addComponent(new Label("PRZEWIDYWANE CENY ROPY NA RYNKU"));
            titlePanel.addComponent(new Label("(JAKIE TRENDY W KOLEJNYCH LATACH :)"));

            contentPanel.addComponent(titlePanel);
            contentPanel.addComponent(new EmptySpace());
            contentPanel.addComponent(new EmptySpace());

            ImageComponent oilGraph = new ImageComponent();
            oilGraph.setTextImage(Oil.oilGraph(gameProperties, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.RED));
            contentPanel.addComponent(oilGraph);

            contentPanel.addComponent(new EmptySpace());

            Button confirmButton1 = new Button("GOTOWE", () -> {
                contentPanel.removeAllComponents();
                gameProperties.window.close();

                // TODO: Redirect to playing menu
            });

            contentPanel.addComponent(confirmButton1);
            confirmButton1.takeFocus();
        });
        contentPanel.addComponent(confirmButton);
        confirmButton.takeFocus();
        
    }

}