package com.zabbum.oelremake;

import java.util.Random;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;

public class Oilfield {

    // Object variables
    public String name;
    public int price;
    public boolean isBought;
    public Player ownership;
    public int oilAmount;
    public boolean canExtractOil;
    public int requiredDepth;
    public int currentDepth;
    public int oilExtracted;
    public int oilAvailabletoSell;

    // Amounts of exploitation stuff
    public int carsAmount;
    public int drillsAmount;
    public int pumpsAmount;

    // Constructor
    public Oilfield(String name) {
        Random random = new Random();

        this.name = name;
        this.price = random.nextInt(70000) + 29900;
        this.isBought = false;
        this.ownership = null;
        this.oilAmount = (this.price - random.nextInt(9999) + 1) * 10;
        this.canExtractOil = false;
        this.requiredDepth = random.nextInt(3666) + 1;
        this.currentDepth = 0;
        this.oilExtracted = 0;
        this.oilAvailabletoSell = 0;
        this.carsAmount = 0;
        this.drillsAmount = 0;
        this.pumpsAmount = 0;
    }
    
    // Set product amount based on industry type
    public void addProductAmount(Class<?> industryType, int productsAmount) {
        if (industryType.equals(CarsIndustry.class)) {
            this.carsAmount += productsAmount;
        } else if (industryType.equals(DrillsIndustry.class)) {
            this.drillsAmount += 500 * productsAmount;
        } else if (industryType.equals(PumpsIndustry.class)) {
            this.pumpsAmount += productsAmount;
        } else {
            return;
        }
    }

    public static Oilfield[] initialize() {
        Oilfield[] oilfields = new Oilfield[12];

        // Oilfields initialization
        oilfields[0] = new Oilfield("JASNY GWINT");
        oilfields[1] = new Oilfield("WIELKA DZIURA");
        oilfields[2] = new Oilfield("WIERTOWISKO");
        oilfields[3] = new Oilfield("SMAK WALUTY");
        oilfields[4] = new Oilfield("MI£A ZIEMIA");
        oilfields[5] = new Oilfield("BORUJ-BORUJ");
        oilfields[6] = new Oilfield("KRASNY POTOK");
        oilfields[7] = new Oilfield("P£YTKIE DO£Y");
        oilfields[8] = new Oilfield("$LADY OLEJU");
        oilfields[9] = new Oilfield("NICZYJ GRUNT");
        oilfields[10] = new Oilfield("DZIKIE PSY");
        oilfields[11] = new Oilfield("UGORY NAFTOWE");

        return oilfields;
    }

    public static void buyField(Player player, GameProperties gameProperties) throws InterruptedException {
        // Prepare new graphical settings
        Panel contentPanel = gameProperties.contentPanel;
        contentPanel.setLayoutManager(new GridLayout(1));
        gameProperties.window.setTheme(
            SimpleTheme.makeTheme(false, TextColor.ANSI.BLACK, TextColor.ANSI.BLUE_BRIGHT,
            TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN, TextColor.ANSI.BLUE_BRIGHT)
            );
        
        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label("WYPRZEDAZ POL NAFTOWYCH"));
        titlePanel.addComponent(new Label("SALDO KONTA: " + String.valueOf(player.balance) + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Panel for oifields and details
        Panel oilPanel = new Panel(new GridLayout(2));

            // Panel for old ownerships
            Panel oldOwnershipPanel = new Panel(new GridLayout(1));
            oldOwnershipPanel.setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT));
            oldOwnershipPanel.addComponent(new Label("DAWNA W£ASNO$C"));
            oldOwnershipPanel.addComponent(new EmptySpace());
            oldOwnershipPanel.addComponent(new Label("1-2: SMAR & CO."));
            oldOwnershipPanel.addComponent(new Label("3-4: R.R. INC."));
            oldOwnershipPanel.addComponent(new Label("5-6: O. MACHINEN"));
            oldOwnershipPanel.addComponent(new Label("7-8: GUT & LUT"));
            oldOwnershipPanel.addComponent(new Label("9-10: OLEJARZ KC"));
            oldOwnershipPanel.addComponent(new Label("11-12: ZDISEK OB."));

            oilPanel.addComponent(oldOwnershipPanel);


            // Panel for oilfields
            Panel oilfieldsPanel = new Panel(new GridLayout(1));

            // Create table
            Table<String> oilfieldsTable = new Table<String>("NR", "NAZWA ", "CENA");

            // Add every available oilfield to table
            oilfieldsTable.getTableModel().addRow("0","-","-");
            for (int oilfieldIndex = 0; oilfieldIndex < gameProperties.oilfields.length; oilfieldIndex++) {
                if (!gameProperties.oilfields[oilfieldIndex].isBought) {
                    // If oilfield is not bought, make it possible to buy it
                    oilfieldsTable.getTableModel().addRow(
                        String.valueOf(oilfieldIndex+1),
                        gameProperties.oilfields[oilfieldIndex].name,
                        String.valueOf(gameProperties.oilfields[oilfieldIndex].price)+"$"
                        );
                }
            }

            oilfieldsTable.setSelectAction(() -> {
                gameProperties.tmpActionInt = Integer.parseInt(oilfieldsTable.getTableModel().getRow(oilfieldsTable.getSelectedRow()).get(0))-1;
                gameProperties.tmpConfirm = true;
            });

            // Display table
            oilfieldsPanel.addComponent(oilfieldsTable);
            oilPanel.addComponent(oilfieldsPanel);

        // Display oilfield details
        contentPanel.addComponent(oilPanel);
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("KTORE POLE CHCESZ WYKUPIC?"));

        // Wait for selection
        Game.waitForConfirm(gameProperties);
        int selectedOilfieldIndex = gameProperties.tmpActionInt;

        // If 0 selected, return
        if (selectedOilfieldIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Note purchase
        gameProperties.oilfields[selectedOilfieldIndex].isBought = true;
        gameProperties.oilfields[selectedOilfieldIndex].ownership = player;

        player.balance -= gameProperties.oilfields[selectedOilfieldIndex].price;

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label("JESTE$ W£ASCICIELEM POLA:"));
        contentPanel.addComponent(new Label(gameProperties.oilfields[selectedOilfieldIndex].name)
            .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT)));

        Thread.sleep(2000);

        // Clean up
        contentPanel.removeAllComponents();
    }
}
