package zabbum.oelremake.Pumps;

import zabbum.oelremake.AbstractIndustry;

import java.util.Random;

public class PumpsIndustry extends AbstractIndustry {

    /**
     * Constructor of a pumps' industry.
     *
     * @param name name of the industry
     */
    public PumpsIndustry(final String name) {
        super(name);

        Random random = new Random();

        this.setPlantPrice(random.nextInt(80000) + 36000);
        this.setProductsAmount(((int) (this.getPlantPrice() / 10000)) * 7 + 25);
    }
}
