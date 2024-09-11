package zabbum.oelremake.Operations;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import zabbum.oelremake.*;

import java.util.Map;
import java.util.regex.Pattern;

public interface BuyableProductsOperation {

    /**
     * Menu for buying products.
     *
     * @param player    Player buying product
     * @param oilfields Oilfields to place products on
     * @param window    Lanterna Window
     * @param langMap   Map with all the text
     */
    public void buyProductsMenu(
            Player player,
            Oilfield[] oilfields,
            Window window,
            Map<String, String> langMap
    ) throws InterruptedException;

    /**
     * Default menu for buying products.
     *
     * @param player
     * @param industries
     * @param oilfields
     * @param baseForeground
     * @param baseBackground
     * @param editableForeground
     * @param editableBackground
     * @param selectedForeground
     * @param selectedBackground
     * @param guiBackground
     * @param tableForeground
     * @param hereYouCanBuy
     * @param productsAmountPrompt
     * @param maxAmount
     * @param window
     * @param langMap
     */
    static void defaultBuyProductsMenu(
            Player player,
            AbstractIndustry[] industries,
            Oilfield[] oilfields,

            TextColor baseForeground,
            TextColor baseBackground,
            TextColor editableForeground,
            TextColor editableBackground,
            TextColor selectedForeground,
            TextColor selectedBackground,
            TextColor guiBackground,
            TextColor tableForeground,

            String hereYouCanBuy,
            String productsAmountPrompt,

            int maxAmount,

            Window window,
            Map<String, String> langMap
    ) throws InterruptedException {

        // Prepare new graphical settings
        Panel contentPanel = (Panel) window.getComponent();
        contentPanel.setLayoutManager(new GridLayout(1));
        window.setTheme(SimpleTheme.makeTheme(
                false,
                baseForeground,
                baseBackground,
                editableForeground,
                editableBackground,
                selectedForeground,
                selectedBackground,
                guiBackground));

        // Display title
        Panel titlePanel = new Panel(new GridLayout(1));
        Game.timeBuffor();
        titlePanel.setTheme(new SimpleTheme(baseBackground, baseForeground));
        titlePanel.addComponent(new EmptySpace());
        Game.timeBuffor();
        titlePanel.addComponent(new Label(hereYouCanBuy));
        titlePanel.addComponent(
                new Label(
                        langMap.get("balance2")
                                + ": "
                                + player.getBalance()
                                + "$"));
        titlePanel.addComponent(new EmptySpace());
        contentPanel.addComponent(titlePanel);

        contentPanel.addComponent(new EmptySpace());

        // Create table
        Table<String> productsTable =
                new Table<>(
                        "NR",
                        langMap.get("industryName"),
                        langMap.get("productsAmount"),
                        langMap.get("price"));
        productsTable.setTheme(
                SimpleTheme.makeTheme(
                        false,
                        tableForeground,
                        baseBackground,
                        editableForeground,
                        editableBackground,
                        selectedForeground,
                        selectedBackground,
                        guiBackground));

        // Add every available industry to table
        productsTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (industries[industryIndex].isBought()
                    && industries[industryIndex].getProductsAmount() != 0) {
                // If industry is bought and has products,
                // make it possible to buy it
                productsTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(industryIndex + 1),
                                industries[industryIndex].getName(),
                                String.valueOf(industries[industryIndex].getProductsAmount()),
                                industries[industryIndex].getProductPrice() + "$");
            }
        }

        Confirm tmpConfirm = new Confirm();
        productsTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(productsTable);
        productsTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(langMap.get("whereToBuy")));

        // Wait for selection
        tmpConfirm.waitForConfirm();
        tmpConfirm = null;
        int selectedIndustryIndex =
                Integer.parseInt(
                        productsTable.getTableModel().getRow(productsTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Prompt for product amount
        contentPanel.addComponent(new Label(productsAmountPrompt));

        // Prompt for product amount until provided value is valid
        TextBox productAmountBox;
        int selectedProductAmount = -1;
        do {
            // Prompt for product amount
            contentPanel.addComponent(new EmptySpace());
            productAmountBox = new TextBox(new TerminalSize(6, 1));
            productAmountBox.setValidationPattern(Pattern.compile("[0-9]*"));
            contentPanel.addComponent(productAmountBox);
            tmpConfirm = new Confirm();
            contentPanel.addComponent(Elements.newConfirmButton(tmpConfirm, langMap.get("done")));

            // Wait for selection
            productAmountBox.takeFocus();
            tmpConfirm.waitForConfirm();

            try {
                selectedProductAmount = Integer.parseInt(productAmountBox.getText());
            } catch (NumberFormatException e) {
                // If a bad value has been provided
                selectedProductAmount = -1;
            }
        } while (selectedProductAmount < 0 || selectedProductAmount > maxAmount);

        // Prompt for oilfield
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(langMap.get("onWhatOilfield")));

        // Display all the oilfields

        // Create table
        Table<String> oilfieldsTable =
                new Table<>(
                        "NR", langMap.get("name"), langMap.get("property"));

        // Add every available oilfield to table
        oilfieldsTable.getTableModel().addRow("0", "-", "-");
        for (int oilfieldIndex = 0; oilfieldIndex < oilfields.length; oilfieldIndex++) {
            // If oilfield is bought, display the name
            String ownerName = "---";
            if (oilfields[oilfieldIndex].isBought()) {
                ownerName = oilfields[oilfieldIndex].getOwnership().getName();
            }
            oilfieldsTable
                    .getTableModel()
                    .addRow(
                            String.valueOf(oilfieldIndex + 1),
                            oilfields[oilfieldIndex].getName(),
                            ownerName);
        }

        tmpConfirm = new Confirm();
        oilfieldsTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(oilfieldsTable);
        oilfieldsTable.takeFocus();

        // Wait for selection
        tmpConfirm.waitForConfirm();
        int selectedOilfieldIndex =
                Integer.parseInt(
                        oilfieldsTable.getTableModel().getRow(oilfieldsTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedOilfieldIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Take actions
        // Reduce amount of available products in industry
        industries[selectedIndustryIndex].buyProducts(selectedProductAmount);
        // Reduce player's balance
        player.decreaseBalance(
                selectedProductAmount * industries[selectedIndustryIndex].getProductPrice());
        if (industries[selectedIndustryIndex].getOwnership() == player) {
            // If player is buying product from themself, give them the money * 0.2
            player.increaseBalance(
                    0.2 * selectedProductAmount * industries[selectedIndustryIndex].getProductPrice());
        } else {
            // Give owner of industry the money
            industries[selectedIndustryIndex]
                    .getOwnership()
                    .increaseBalance(
                            selectedProductAmount * industries[selectedIndustryIndex].getProductPrice());
        }

        // Place products in the oilfield
        // TODO: Make it more modifiable (not just predefined in the Oilfield class)
        oilfields[selectedOilfieldIndex].addProductAmount(
                industries[0].getClass(), selectedProductAmount);

        // Clean up
        contentPanel.removeAllComponents();
    }
}
