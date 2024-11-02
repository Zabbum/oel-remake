package zabbum.oelremake;

import com.github.zabbum.oelremakecomponents.Player;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.Window;
import zabbum.oelremake.plants.industries.CarsOperations;
import zabbum.oelremake.plants.industries.DrillsOperations;
import zabbum.oelremake.plants.industries.PumpsOperations;
import zabbum.oelremake.plants.oilfield.OilfieldOperations;

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

    public OilfieldOperations oilfieldOperations;
    public CarsOperations carsIndustryOperations;
    public PumpsOperations pumpsIndustryOperations;
    public DrillsOperations drillsIndustryOperations;

    public Double[] oilPrices;

    public Player[] players;

    public GameProperties(int roundCount) {
        this.roundCount = roundCount;
        this.currentRound = 0;
        this.isInDevMode = false;

        // Initialize industries
        this.oilfieldOperations = new OilfieldOperations();
        this.carsIndustryOperations = new CarsOperations();
        this.pumpsIndustryOperations = new PumpsOperations();
        this.drillsIndustryOperations = new DrillsOperations();

        // Generate oil prices
        this.oilPrices = Oil.generatePrices(roundCount);
    }
}
