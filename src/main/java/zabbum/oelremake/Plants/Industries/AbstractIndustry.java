package zabbum.oelremake.Plants.Industries;

import lombok.Getter;
import lombok.Setter;
import zabbum.oelremake.Plants.AbstractPlant;

@Getter
@Setter
// and make AbstractIndustry and Oilfield extend that class
public abstract class AbstractIndustry extends AbstractPlant {
    private int productsAmount;
    private double productPrice;

    /**
     * Constructor of a Plant.
     *
     * @param name       Name of a plant
     */
    public AbstractIndustry(String name) {
        super(name);
    }

    /**
     * Method to reduce wanted products amount from the industry.
     * @param productsAmount    Amount of products to reduce amount of
     */
    public void buyProducts(int productsAmount) {
        this.productsAmount -= productsAmount;
    }
}
