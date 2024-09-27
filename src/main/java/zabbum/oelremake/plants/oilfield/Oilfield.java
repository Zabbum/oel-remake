package zabbum.oelremake.plants.oilfield;

import lombok.Getter;
import lombok.Setter;
import zabbum.oelremake.plants.AbstractPlant;
import zabbum.oelremake.plants.industries.Cars.CarsIndustry;
import zabbum.oelremake.plants.industries.Drills.DrillsIndustry;
import zabbum.oelremake.plants.industries.Pumps.PumpsIndustry;

import java.util.Random;

@Getter
@Setter
public class Oilfield extends AbstractPlant {

    /**
     * Total oil amount in oilfield.
     */
    private int totalOilAmount;

    /**
     * Is oilfield able to pump oil.
     * false by default,
     * true if player has reached required depth.
     */
    private boolean isExploitable;

    /**
     * Required depth of digging to make oilfield exploitable.
     */
    private int requiredDepth;

    /**
     * Current digging depth.
     */
    private int currentDepth;

    /**
     * Amount of oil extracted in total.
     */
    private int oilExtracted;

    /**
     * Amount of oil available to sell.
     */
    private int oilAvailabletoSell;

    /**
     * Amount of cars in oilfield.
     * Cars are needed to sell oil.
     */
    private int carsAmount;

    /**
     * Amount of drills in oilfield.
     * Drills are needed to dril (bruh).
     */
    private int drillsAmount;

    /**
     * Amount of pumps in oilfield.
     * Pumps are needed to extract oil from oilfield.
     */
    private int pumpsAmount;

    /**
     * Constructor of a Plant.
     *
     * @param name Name of a plant
     */
    public Oilfield(String name) {
        super(name);

        Random random = new Random();

        this.plantPrice = random.nextInt(70000) + 29900;
        this.ownership = null;
        this.totalOilAmount = (this.plantPrice - random.nextInt(9999) + 1) * 10;
        this.isExploitable = false;
        this.requiredDepth = random.nextInt(3666) + 1;
        this.currentDepth = 0;
        this.oilExtracted = 0;
        this.oilAvailabletoSell = 0;
        this.carsAmount = 0;
        this.drillsAmount = 0;
        this.pumpsAmount = 0;
    }

    /**
     * Method to extract oil in the oilfield.
     * This is executed every round.
     */
    public void extractOil() {
        this.oilAvailabletoSell += 8000 * this.pumpsAmount;
        this.oilExtracted += 8000 * this.pumpsAmount;
    }

    /**
     * Method to sell oil from the oilfield.
     *
     * @param oilAmount amount of oil to sell
     */
    public void sellOil(final int oilAmount) {
        this.oilAvailabletoSell -= oilAmount;
    }

    /**
     * Dig in the oilfield.
     * This is executed every round.
     */
    public void dig() {
        Random random = new Random();

        this.drillsAmount -= 500;
        this.currentDepth += 500 - (random.nextInt(30) + 1);
    }

    /**
     * Increase product amount based on industry type.
     *
     * @param industryType   type of an industry
     * @param productsAmount amount of prodduct produced by the industry
     */
    public void addProductAmount(
            final Class<?> industryType, final int productsAmount) {
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
}
