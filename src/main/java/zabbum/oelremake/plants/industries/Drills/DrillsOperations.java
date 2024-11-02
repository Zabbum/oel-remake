package zabbum.oelremake.plants.industries.Drills;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.plants.industries.Drills.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Window;
import lombok.Getter;
import zabbum.oelremake.operations.BuyableIndustryOperation;
import zabbum.oelremake.operations.BuyableProductsOperation;
import zabbum.oelremake.operations.SabotableOperation;

import java.util.Map;

@Getter
public class DrillsOperations implements BuyableIndustryOperation, BuyableProductsOperation, SabotableOperation {

    private final DrillsIndustry[] industries;

    public DrillsOperations() {
        industries = initialize();
    }

    /**
     * Initialize drills industries.
     *
     * @return array of cars industries
     */
    public static DrillsIndustry[] initialize() {
        DrillsIndustry[] drillProds = new DrillsIndustry[3];

        // Drills productions initialization
        drillProds[0] = new DrillsIndustry("TURBOWIERT");
        drillProds[1] = new DrillsIndustry("NA B£YSK INC.");
        drillProds[2] = new DrillsIndustry("PET SHOP&BOYS");

        return drillProds;
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
                langMap.get("drillsIndustrySale"),
                langMap.get("drillsIndustryPrompt"),
                langMap.get("drillsPricePrompt"),
                60000,
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
                TextColor.ANSI.YELLOW,
                TextColor.ANSI.BLUE,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.CYAN,
                TextColor.ANSI.YELLOW,
                TextColor.ANSI.BLUE,
                langMap.get("drillsHereYouCanBuy"),
                langMap.get("drillsProductsAmountPrompt"),
                10,
                window,
                langMap
        );
    }
}
