package zabbum.oelremake.plants.oilfield;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import lombok.Getter;
import zabbum.oelremake.Confirm;
import zabbum.oelremake.Game;
import zabbum.oelremake.Player;

import java.util.Map;

@Getter
public class OilfieldOperations {

    private final Oilfield[] oilfields;

    public OilfieldOperations() {
        oilfields = initialize();
    }

    /**
     * Initialize the oilfields.
     *
     * @return array of the oilfields
     */
    private Oilfield[] initialize() {
        Oilfield[] oilfields = new Oilfield[12];

        // Oilfields initialization
        oilfields[0] = new Oilfield("JASNY GWINT");
        oilfields[1] = new Oilfield("WIELKA DZIURA");
        oilfields[2] = new Oilfield("WIERTOWISKO");
        oilfields[3] = new Oilfield("SMAK WALUTY");
        oilfields[4] = new Oilfield("MI£A ZIEMIA");
        oilfields[5] = new Oilfield("BORUJ-BORUJ");
        oilfields[6] = new Oilfield("KRASNY POTOK");
        oilfields[7] = new Oilfield("P£YTKIE DO£Y");
        oilfields[8] = new Oilfield("$LADY OLEJU");
        oilfields[9] = new Oilfield("NICZYJ GRUNT");
        oilfields[10] = new Oilfield("DZIKIE PSY");
        oilfields[11] = new Oilfield("UGORY NAFTOWE");

        return oilfields;
    }

    public void buyOilfieldMenu(
            final Player player,
            final Window window,
            final Map<String, String> langMap
    ) throws InterruptedException {

        // Prepare new graphical settings
        Panel contentPanel = (Panel) window.getComponent();
        contentPanel.setLayoutManager(new GridLayout(1));
        window.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.BLUE_BRIGHT,
                        TextColor.ANSI.YELLOW_BRIGHT,
                        TextColor.ANSI.BLUE,
                        TextColor.ANSI.WHITE_BRIGHT,
                        TextColor.ANSI.CYAN,
                        TextColor.ANSI.BLUE_BRIGHT));

        // Display title and player details
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(
                new SimpleTheme(
                        TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(
                new Label(
                        langMap.get("oilfieldsSale")));
        titlePanel.addComponent(
                new Label(
                        langMap.get("balance2")
                                + ": "
                                + String.valueOf(player.getBalance())
                                + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Panel for oifields and details
        Panel oilPanel = new Panel(new GridLayout(2));

        // Panel for old ownerships
        Panel oldOwnershipPanel = new Panel(new GridLayout(1));
        oldOwnershipPanel.setTheme(
                new SimpleTheme(
                        TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT));
        oldOwnershipPanel.addComponent(
                new Label(
                        langMap.get("oldOwnership")));
        oldOwnershipPanel.addComponent(new EmptySpace());
        oldOwnershipPanel.addComponent(new Label("1-2: SMAR & CO."));
        oldOwnershipPanel.addComponent(new Label("3-4: R.R. INC."));
        oldOwnershipPanel.addComponent(new Label("5-6: O. MACHINEN"));
        oldOwnershipPanel.addComponent(new Label("7-8: GUT & LUT"));
        oldOwnershipPanel.addComponent(new Label("9-10: OLEJARZ KC"));
        oldOwnershipPanel.addComponent(new Label("11-12: ZDISEK OB."));

        oilPanel.addComponent(oldOwnershipPanel);

        // Panel for oilfields
        Panel oilfieldsPanel = new Panel(new GridLayout(1));

        // Create table
        Table<String> oilfieldsTable = new Table<String>(
                "NR",
                langMap.get("name"),
                langMap.get("price"));

        // Add every available oilfield to table
        oilfieldsTable.getTableModel().addRow("0", "-", "-");
        for (int oilfieldIndex = 0; oilfieldIndex < oilfields.length; oilfieldIndex++) {
            if (!oilfields[oilfieldIndex].isBought()) {
                // If oilfield is not bought, make it possible to buy it
                oilfieldsTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(oilfieldIndex + 1),
                                oilfields[oilfieldIndex].getName(),
                                String.valueOf(oilfields[oilfieldIndex].getPlantPrice()) + "$");
            }
        }

        Confirm tmpConfirm = new Confirm();
        oilfieldsTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        oilfieldsPanel.addComponent(oilfieldsTable);
        oilPanel.addComponent(oilfieldsPanel);

        // Display oilfield details
        contentPanel.addComponent(oilPanel);
        oilfieldsTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(langMap.get("oilfieldsPrompt")));

        // Wait for selection
        tmpConfirm.waitForConfirm();
        tmpConfirm = null;
        oilfieldsTable.setEnabled(false);
        int selectedOilfieldIndex = Integer.parseInt(
                oilfieldsTable.getTableModel().getRow(oilfieldsTable.getSelectedRow()).get(0))
                - 1;

        // If 0 selected, return
        if (selectedOilfieldIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Note purchase
        oilfields[selectedOilfieldIndex].setOwnership(player);

        player.decreaseBalance(oilfields[selectedOilfieldIndex].getPlantPrice());

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(langMap.get("youAreOwnerOfOilfield") + ":"));
        contentPanel.addComponent(
                new Label(oilfields[selectedOilfieldIndex].getName())
                        .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT)));

        Thread.sleep(2000);

        // Clean up
        contentPanel.removeAllComponents();
    }
}
