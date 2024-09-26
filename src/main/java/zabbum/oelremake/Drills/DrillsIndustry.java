package zabbum.oelremake.Drills;

import zabbum.oelremake.AbstractIndustry;

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

        this.setIndustryPrice(random.nextInt(50000) + 10000);
        this.setProductsAmount(((int) (this.getIndustryPrice() / 10000)) * 8 + 25);
    }
}
