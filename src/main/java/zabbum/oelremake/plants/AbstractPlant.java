package zabbum.oelremake.plants;

import lombok.Getter;
import lombok.Setter;
import zabbum.oelremake.Player;

@Getter
@Setter
public abstract class AbstractPlant {
    protected String name;
    protected int plantPrice;
    protected Player ownership;

    /**
     * Constructor of a Plant.
     *
     * @param name Name of a plant
     */
    public AbstractPlant(String name) {
        this.name = name;
        this.ownership = null;
    }

    /**
     * Method to get if plant is bought.
     *
     * @return true if is bought, false if is not
     */
    public boolean isBought() {
        return this.ownership != null;
    }
}
