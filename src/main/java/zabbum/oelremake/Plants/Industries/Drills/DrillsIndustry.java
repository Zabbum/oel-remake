package zabbum.oelremake.Plants.Industries.Drills;

import zabbum.oelremake.Plants.Industries.AbstractIndustry;

import java.util.Random;

public class DrillsIndustry extends AbstractIndustry {

    /**
     * Constructor of a drills' industry.
     *
     * @param name name of the industry
     */
    public DrillsIndustry(final String name) {
        super(name);

        Random random = new Random();

        this.setPlantPrice(random.nextInt(50000) + 10000);
        this.setProductsAmount(((int) (this.getPlantPrice() / 10000)) * 8 + 25);
    }
}
