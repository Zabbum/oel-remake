package com.zabbum.oelremake;

import java.util.Random;

import com.googlecode.lanterna.TextColor;

public class PumpsIndustry extends AbstractIndustry {

    // Constructor
    public PumpsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.setIndustryPrice(random.nextInt(80000) + 36000);
        this.setProductsAmount(((int)(this.getIndustryPrice()/10000)) * 7 + 25);
    }

    public static PumpsIndustry[] initialize() {
        PumpsIndustry[] pumpProds = new PumpsIndustry[2];

        // Pump productions initialization
        pumpProds[0] = new PumpsIndustry("ZASSANICKI GMBH");
        pumpProds[1] = new PumpsIndustry("DR PUMPENER");

        return pumpProds;
    }

    // Menu for buying pumps productions
    public static void buyIndustry(Player player, GameProperties gameProperties) throws InterruptedException {
        AbstractIndustry.buyIndustry(
            player, gameProperties,
            PumpsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLUE,
            TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLUE,
            "KUPOWANIE FABRYK POMP", "KTORA FIRME CHCESZ KUPIC?",
            "PROSZE PODAC SWOJA CENE POMPY.",
            50000
        );
    }

    // Menu for buying pumps
    public static void buyProduct(Player player, GameProperties gameProperties) throws InterruptedException {
        AbstractIndustry.buyProduct(
            player, gameProperties,
            PumpsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLACK_BRIGHT,
            TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK,
            "SPRZEDAZ POMP", "ILE POMP KUPUJESZ?",
            15
        );
    }
}
