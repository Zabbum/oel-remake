package zabbum.oelremake.Cars;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Window;
import lombok.Getter;
import zabbum.oelremake.Oilfield;
import zabbum.oelremake.Operations.BuyableIndustryOperation;
import zabbum.oelremake.Operations.BuyableProductsOperation;
import zabbum.oelremake.Player;
import zabbum.oelremake.Operations.SabotableOperation;

import java.util.Map;


@Getter
public class CarsOperations implements BuyableIndustryOperation, BuyableProductsOperation, SabotableOperation {

    private final CarsIndustry[] industries;

    public CarsOperations() {
        industries = initialize();
    }

    /**
     * Initialize cars industries.
     *
     * @return array of cars industries
     */
    private CarsIndustry[] initialize() {
        CarsIndustry[] carsIndustries = new CarsIndustry[4];

        // Cars productions initialization
        carsIndustries[0] = new CarsIndustry("WOZ-PRZEWOZ");
        carsIndustries[1] = new CarsIndustry("WAGONENSITZ");
        carsIndustries[2] = new CarsIndustry("WORLD CO.");
        carsIndustries[3] = new CarsIndustry("DRINK TANK INC.");

        return carsIndustries;
    }

    @Override
    public void buyIndustryMenu(Player player, Window window, Map<String, String> langMap) throws InterruptedException {
        BuyableIndustryOperation.defaultBuyIndustryMenu(
                player,
                industries,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.BLUE,
                langMap.get("carsIndustrySale"),
                langMap.get("carsIndustryPrompt"),
                langMap.get("carsPricePrompt"),
                50000,
                window,
                langMap
        );
    }

    @Override
    public void buyProductsMenu(Player player, Oilfield[] oilfields, Window window, Map<String, String> langMap)
            throws InterruptedException {

        BuyableProductsOperation.defaultBuyProductsMenu(
                player,
                industries,
                oilfields,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.RED_BRIGHT,
                TextColor.ANSI.RED_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.RED_BRIGHT,
                TextColor.ANSI.BLACK,
                langMap.get("carsHereYouCanBuy"),
                langMap.get("carsProductsAmountPrompt"),
                15,
                window,
                langMap
        );
    }
}
