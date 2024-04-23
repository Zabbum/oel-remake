package com.zabbum.oelremake;

import java.util.Random;

import com.googlecode.lanterna.TextColor;

public class CarsIndustry extends AbstractIndustry {

    // Constructor
    public CarsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.industryPrice = random.nextInt(55000) + 45000;
        this.productsAmount = ((int)(this.industryPrice/10000)) * 3  + 15;
    }

    public static CarsIndustry[] initialize() {
        CarsIndustry[] carsProds = new CarsIndustry[4];

        // Cars productions initialization
        carsProds[0] = new CarsIndustry("WOZ-PRZEWOZ");
        carsProds[1] = new CarsIndustry("WAGONENSITZ");
        carsProds[2] = new CarsIndustry("WORLD CO.");
        carsProds[3] = new CarsIndustry("DRINK TANK INC.");

        return carsProds;
    }

    // Menu for buying cars productions
    public static void buyIndustry(Player player, GameProperties gameProperties) {
        AbstractIndustry.buyIndustry(
            player, gameProperties,
            CarsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.BLUE,
            TextColor.ANSI.BLUE, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.BLUE,
            "SPRZEDAZ FABRYK WAGONOW", "KTORA FIRME CHCESZ KUPIC?",
            "PROSZE PODAC SWOJA CENE WAGONU.",
            50000
        );
        System.out.println("test");
    }

    // Menu for buying cars
    public static void buyProduct(Player player, GameProperties gameProperties) {
        AbstractIndustry.buyProduct(
            player, gameProperties,
            CarsIndustry.class,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.RED_BRIGHT,
            TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
            TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.BLACK,
            "TU MOZESZ KUPIC WAGONY", "ILE WAGONOW KUPUJESZ?",
            15
        );
    }
}