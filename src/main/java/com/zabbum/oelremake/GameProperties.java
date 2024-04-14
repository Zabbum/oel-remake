package com.zabbum.oelremake;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.Window;

public class GameProperties {
    public Window window;
    public Panel contentPanel;
    public SeparateTextGUIThread textGUIThread;

    public int roundCount;
    public int currentRound;
    public int playerAmount;

    public Oilfield[] oilfields;
    public CarsIndustry[] carsIndustries;
    public PumpsIndustry[] pumpIndustries;
    public DrillsIndustry[] drillIndustries;

    public double[] oilPrices;

    public Player[] players;

    public boolean tmpConfirm;
    public String tmpAction;
    public int tmpActionInt;


    public GameProperties(int roundCount) {
        this.roundCount = roundCount;
        this.currentRound = 0;

        // Initialize industries
        this.oilfields = Oilfield.initialize();
        this.carsIndustries = CarsIndustry.initialize();
        this.pumpIndustries = PumpsIndustry.initialize();
        this.drillIndustries = DrillsIndustry.initialize();

        // Generate oil prices
        oilPrices = Oil.generatePrices(roundCount);
    }
    
}
