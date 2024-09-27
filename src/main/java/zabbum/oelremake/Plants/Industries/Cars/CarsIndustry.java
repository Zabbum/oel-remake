package zabbum.oelremake.Plants.Industries.Cars;

import zabbum.oelremake.Plants.Industries.AbstractIndustry;

import java.util.Random;

public class CarsIndustry extends AbstractIndustry {

    /**
     * Constructor of a cars' industry.
     *
     * @param name name of the industry
     */
    public CarsIndustry(final String name) {
        super(name);

        Random random = new Random();

        this.setPlantPrice(random.nextInt(55000) + 45000);
        this.setProductsAmount(((int) (this.getPlantPrice() / 10000)) * 3 + 15);
    }
}
