package zabbum.oelremake.plants.industries.Pumps;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Window;
import lombok.Getter;
import zabbum.oelremake.Player;
import zabbum.oelremake.operations.BuyableIndustryOperation;
import zabbum.oelremake.operations.BuyableProductsOperation;
import zabbum.oelremake.operations.SabotableOperation;
import zabbum.oelremake.plants.oilfield.Oilfield;

import java.util.Map;

@Getter
public class PumpsOperations implements BuyableIndustryOperation, BuyableProductsOperation, SabotableOperation {

    private final PumpsIndustry[] industries;

    public PumpsOperations() {
        industries = initialize();
    }

    /**
     * Initialize drills industries.
     *
     * @return array of cars industries
     */
    public static PumpsIndustry[] initialize() {
        PumpsIndustry[] pumpProds = new PumpsIndustry[2];

        // Pump productions initialization
        pumpProds[0] = new PumpsIndustry("ZASSANICKI GMBH");
        pumpProds[1] = new PumpsIndustry("DR PUMPENER");

        return pumpProds;
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
                langMap.get("pumpsIndustrySale"),
                langMap.get("pumpsIndustryPrompt"),
                langMap.get("pumpsPricePrompt"),
                50000,
                window,
                langMap
        );
    }

    @Override
    public void buyProductsMenu(Player player, Oilfield[] oilfields, Window window, Map<String, String> langMap) throws InterruptedException {
        BuyableProductsOperation.defaultBuyProductsMenu(
                player,
                industries,
                oilfields,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.BLACK,
                langMap.get("pumpsHereYouCanBuy"),
                langMap.get("pumpsProductsAmountPrompt"),
                15,
                window,
                langMap
        );
    }
}
