package com.zabbum.oelremake;

import java.util.Random;

import com.googlecode.lanterna.TextColor;

public class DrillsIndustry extends AbstractIndustry {

    // Constructor
    public DrillsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.industryPrice = random.nextInt(50000) + 10000;
        this.productsAmount = ((int)(this.industryPrice/10000)) * 8 + 25;
    }

    public static DrillsIndustry[] initialize() {
        DrillsIndustry[] drillProds = new DrillsIndustry[3];

        // Dril productions initialization
        drillProds[0] = new DrillsIndustry("TURBOWIERT");
        drillProds[1] = new DrillsIndustry("NA B£YSK INC.");
        drillProds[2] = new DrillsIndustry("PET SHOP&BOYS");

        return drillProds;
    }

    // Menu for buying drills productions
    public static void buyIndustry(Player player, GameProperties gameProperties) {
        AbstractIndustry.buyIndustry(
            player, gameProperties,
            DrillsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLUE,
            TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLUE,
            "SPRZEDAZ FABRYK WIERTE£", "KTORA FIRME CHCESZ KUPIC?",
            "PROSZE PODAC SWOJA CENE NA RURY O D£UGO$CI 500 M.",
            60000
        );
    }

    // Menu for buying drills
    public static void buyProduct(Player player, GameProperties gameProperties) {
        AbstractIndustry.buyProduct(
            player, gameProperties,
            DrillsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.YELLOW,
            TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.YELLOW, TextColor.ANSI.BLUE,
            "TU MOZESZ KUPIC WIERT£A", "ILE RUR 500 METROWYCH KUPUJESZ?",
            10
        );
    }
}
