package zabbum.oelremake;

import com.googlecode.lanterna.TextColor;

import java.util.Random;

public class DrillsIndustry extends AbstractIndustry {

    // Constructor
    public DrillsIndustry(String name) {
        super(name);

        Random random = new Random();

        this.setIndustryPrice(random.nextInt(50000) + 10000);
        this.setProductsAmount(((int) (this.getIndustryPrice() / 10000)) * 8 + 25);
    }

    public static DrillsIndustry[] initialize() {
        DrillsIndustry[] drillProds = new DrillsIndustry[3];

        // Dril productions initialization
        drillProds[0] = new DrillsIndustry("TURBOWIERT");
        drillProds[1] = new DrillsIndustry("NA B£YSK INC.");
        drillProds[2] = new DrillsIndustry("PET SHOP&BOYS");

        return drillProds;
    }

    // Menu for buying drills productions
    public static void buyIndustry(Player player, GameProperties gameProperties)
            throws InterruptedException {
        AbstractIndustry.buyIndustry(
                player,
                gameProperties,
                DrillsIndustry.class,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.BLUE,
                gameProperties.langMap.get("drillsIndustrySale"),
                gameProperties.langMap.get("drillsIndustryPrompt"),
                gameProperties.langMap.get("drillsPricePrompt"),
                60000);
    }

    // Menu for buying drills
    public static void buyProduct(Player player, GameProperties gameProperties)
            throws InterruptedException {
        AbstractIndustry.buyProduct(
                player,
                gameProperties,
                DrillsIndustry.class,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.YELLOW,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.YELLOW,
                TextColor.ANSI.BLUE,
                gameProperties.langMap.get("drillsHereYouCanBuy"),
                gameProperties.langMap.get("drillsProductsAmountPrompt"),
                10);
    }
}
