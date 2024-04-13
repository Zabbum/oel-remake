package com.zabbum.oelremake;

import com.googlecode.lanterna.gui2.Window;

public class GameProperties {
    public Window window;

    public int roundCount;
    public int currentRound;
    public int playerAmount;

    public Oilfield[] oilfields;
    public CarsProd[] carsProds;
    public PumpProd[] pumpProds;
    public DrillProd[] drillProds;

    public double[] oilPrices;

    public Player[] players;


    public GameProperties(int roundCount) {
        this.roundCount = roundCount;
        this.currentRound = 0;

        // Initialize industries
        this.oilfields = Oilfield.initialize();
        this.carsProds = CarsProd.initialize();
        this.pumpProds = PumpProd.initialize();
        this.drillProds = DrillProd.initialize();

        // Generate oil prices
        oilPrices = Oil.generatePrices(roundCount);
    }
    
}
