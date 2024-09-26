package zabbum.oelremake;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.Window;
import zabbum.oelremake.Cars.CarsOperations;
import zabbum.oelremake.Drills.DrillsOperations;
import zabbum.oelremake.Pumps.PumpsOperations;

import java.util.Map;

public class GameProperties {
    public Map<String, String> langMap;

    public Window window;
    public Panel contentPanel;
    public SeparateTextGUIThread textGUIThread;
    public Thread mainThread;

    public boolean isInDevMode;

    public int roundCount;
    public int currentRound;
    public int playerAmount;

    public Oilfield[] oilfields;
    public CarsOperations carsIndustryOperations;
    public PumpsOperations pumpsIndustryOperations;
    public DrillsOperations drillsIndustryOperaions;

    public double[] oilPrices;

    public Player[] players;

    public boolean tmpConfirm;
    public String tmpAction;

    public GameProperties(int roundCount) {
        this.roundCount = roundCount;
        this.currentRound = 0;
        this.isInDevMode = false;

        // Initialize industries
        this.oilfields = Oilfield.initialize();
        this.carsIndustryOperations = new CarsOperations();
        this.pumpsIndustryOperations = new PumpsOperations();
        this.drillsIndustryOperaions = new DrillsOperations();

        // Generate oil prices
        oilPrices = Oil.generatePrices(roundCount);
    }
}
