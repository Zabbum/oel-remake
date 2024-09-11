package zabbum.oelremake;

import lombok.Data;

@Data

// TODO: Create class AbstractPlacement that is basically AbstractIndustry, but without products
// and make AbstractIndustry and Oilfield extend that class
public abstract class AbstractIndustry {
  private String name;
  private int industryPrice;
  private Player ownership;

  private int productsAmount;
  private double productPrice;

  // Constructor
  public AbstractIndustry(String name) {
    this.name = name;
    this.ownership = null;
  }

  // Is bought
  public boolean isBought() {
    return this.ownership != null;
  }

  // Buy products
  public void buyProducts(int productsAmount) {
    this.productsAmount -= productsAmount;
  }
}
