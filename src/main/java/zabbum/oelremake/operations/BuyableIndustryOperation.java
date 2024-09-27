package zabbum.oelremake.operations;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import zabbum.oelremake.*;
import zabbum.oelremake.plants.industries.AbstractIndustry;

import java.util.Map;
import java.util.regex.Pattern;

public interface BuyableIndustryOperation {

    /**
     * Menu for buying industries.
     *
     * @param player  Player buying industry
     * @param window  Lanterna Window
     * @param langMap Map with all the text
     */
    public void buyIndustryMenu(
            Player player,
            Window window,
            Map<String, String> langMap
    ) throws InterruptedException;

    /**
     * Default menu for buying industries.
     *
     * @param player             Player that is buying industry
     * @param industries         List of industries to buy
     * @param baseForeground     Foreground color
     * @param baseBackground     Background color
     * @param editableForeground Editable foreground color
     * @param editableBackground Editable background color
     * @param selectedForeground Selected foreground color
     * @param selectedBackground Selected background color
     * @param guiBackground      GUI background
     * @param industrySale
     * @param industryPrompt
     * @param pricePrompt
     * @param maxPrice           maximum price for the product
     * @param window             Lanterna Window
     * @param langMap            Map with all the text
     */
    static void defaultBuyIndustryMenu(
            Player player,
            AbstractIndustry[] industries,

            TextColor baseForeground,
            TextColor baseBackground,
            TextColor editableForeground,
            TextColor editableBackground,
            TextColor selectedForeground,
            TextColor selectedBackground,
            TextColor guiBackground,

            String industrySale,
            String industryPrompt,
            String pricePrompt,

            int maxPrice,

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
        titlePanel.addComponent(new Label(industrySale));
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
        Table<String> industriesTable =
                new Table<>(
                        "NR",
                        langMap.get("industryName"),
                        langMap.get("productsAmount"),
                        langMap.get("price"));

        // Add every available industry to table
        industriesTable.getTableModel().addRow("0", "-", "-", "-");
        for (int industryIndex = 0; industryIndex < industries.length; industryIndex++) {
            if (!industries[industryIndex].isBought()) {
                // If industry is not bought, make it possible to buy it
                industriesTable
                        .getTableModel()
                        .addRow(
                                String.valueOf(industryIndex + 1),
                                industries[industryIndex].getName(),
                                String.valueOf(industries[industryIndex].getProductsAmount()),
                                industries[industryIndex].getPlantPrice() + "$");
            }
        }

        Confirm tmpConfirm = new Confirm();
        industriesTable.setSelectAction(tmpConfirm::confirm);

        // Display table
        contentPanel.addComponent(industriesTable);
        industriesTable.takeFocus();
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(industryPrompt));

        // Wait for selection
        tmpConfirm.waitForConfirm();
        tmpConfirm = null;
        industriesTable.setEnabled(false);
        int selectedIndustryIndex =
                Integer.parseInt(
                        industriesTable.getTableModel().getRow(industriesTable.getSelectedRow()).get(0))
                        - 1;

        // If 0 selected, return
        if (selectedIndustryIndex == -1) {
            // Clean up
            contentPanel.removeAllComponents();
            return;
        }

        // Note purchase
        industries[selectedIndustryIndex].setOwnership(player);
        player.decreaseBalance(industries[selectedIndustryIndex].getPlantPrice());

        // Inform user about purchase
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(
                new Label(langMap.get("youAreOwnerOfIndustry") + ": "));
        contentPanel.addComponent(new Label(industries[selectedIndustryIndex].getName()));
        contentPanel.addComponent(new EmptySpace());
        contentPanel.addComponent(new Label(pricePrompt));

        // Prompt for price until provided value is valid
        TextBox productPriceBox = new TextBox(new TerminalSize(6, 1));
        productPriceBox.setValidationPattern(Pattern.compile("[0-9]*"));
        contentPanel.addComponent(productPriceBox);
        productPriceBox.takeFocus();

        tmpConfirm = new Confirm();
        contentPanel.addComponent(Elements.newConfirmButton(tmpConfirm, langMap.get("done")));

        // If confirm button is pressed and choice is valid, let it be
        do {
            tmpConfirm.waitForConfirm();
            tmpConfirm.setConfirmStatus(false);
        } while (!(SimpleLogic.isValid(productPriceBox.getText(), 0, new int[]{maxPrice})));

        // Set the price
        industries[selectedIndustryIndex].setProductPrice(Integer.parseInt(productPriceBox.getText()));

        // Clean up
        contentPanel.removeAllComponents();
    }
}
