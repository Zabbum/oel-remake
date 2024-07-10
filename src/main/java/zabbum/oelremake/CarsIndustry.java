package zabbum.oelremake;

import com.googlecode.lanterna.TextColor;
import java.util.Random;

public class CarsIndustry extends AbstractIndustry {

  // Constructor
  public CarsIndustry(String name) {
    super(name);

    Random random = new Random();

    this.setIndustryPrice(random.nextInt(55000) + 45000);
    this.setProductsAmount(((int) (this.getIndustryPrice() / 10000)) * 3 + 15);
  }

  public static CarsIndustry[] initialize() {
    CarsIndustry[] carsProds = new CarsIndustry[4];

    // Cars productions initialization
    carsProds[0] = new CarsIndustry("WOZ-PRZEWOZ");
    carsProds[1] = new CarsIndustry("WAGONENSITZ");
    carsProds[2] = new CarsIndustry("WORLD CO.");
    carsProds[3] = new CarsIndustry("DRINK TANK INC.");

    return carsProds;
  }

  // Menu for buying cars productions
  public static void buyIndustry(Player player, GameProperties gameProperties)
      throws InterruptedException {
    AbstractIndustry.buyIndustry(
        player,
        gameProperties,
        CarsIndustry.class,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.BLUE,
        TextColor.ANSI.BLUE,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.CYAN,
        TextColor.ANSI.BLUE,
        gameProperties.langMap.get("carsIndustrySale"),
        gameProperties.langMap.get("carsIndustryPrompt"),
        gameProperties.langMap.get("carsPricePrompt"),
        50000);
  }

  // Menu for buying cars
  public static void buyProduct(Player player, GameProperties gameProperties)
      throws InterruptedException {
    AbstractIndustry.buyProduct(
        player,
        gameProperties,
        CarsIndustry.class,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.RED_BRIGHT,
        TextColor.ANSI.RED_BRIGHT,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.WHITE_BRIGHT,
        TextColor.ANSI.CYAN,
        TextColor.ANSI.RED_BRIGHT,
        TextColor.ANSI.BLACK,
        gameProperties.langMap.get("carsHereYouCanBuy"),
        gameProperties.langMap.get("carsProductsAmountPrompt"),
        15);
  }
}
