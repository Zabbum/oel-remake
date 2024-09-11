package zabbum.oelremake.Cars;

import com.googlecode.lanterna.TextColor;
import zabbum.oelremake.AbstractIndustry;
import zabbum.oelremake.GameProperties;
import zabbum.oelremake.Player;

import java.util.Random;

public class CarsIndustry extends AbstractIndustry {

  /**
   * Constructor of a cars' industry.
   * @param name name of the industry
   */
  public CarsIndustry(final String name) {
    super(name);

    Random random = new Random();

    this.setIndustryPrice(random.nextInt(55000) + 45000);
    this.setProductsAmount(((int) (this.getIndustryPrice() / 10000)) * 3 + 15);
  }
}
