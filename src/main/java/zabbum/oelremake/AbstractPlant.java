package zabbum.oelremake;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractPlant {
    private String name;
    private int plantPrice;
    private Player ownership;

    /**
     * Constructor of a Plant.
     * @param name          Name of a plant
     */
    public AbstractPlant(String name) {
        this.name = name;
        this.ownership = null;
    }

    /**
     * Method to get if plant is bought.
     * @return  true if is bought, false if is not
     */
    public boolean isBought() {
        return this.ownership != null;
    }
}
