package zabbum.oelremake;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;
import java.util.Random;
import lombok.Data;

@Data
public final class Oilfield {

  /**
   * Name of oilfield.
   */
  private String name;
  /**
   * Price of oilfield.
   */
  private int price;
  /**
   * Ownership of oilfield.
   */
  private Player ownership;
  /**
   * Total oil amount in oilfield.
   */
  private int totalOilAmount;
  /**
   * Is oilfield able to pump oil.
   * false by default,
   * true if player has reached required depth.
   */
  private boolean isExploitable;
  /**
   * Required depth of digging to make oilfield exploitable.
   */
  private int requiredDepth;
  /**
   * Current digging depth.
   */
  private int currentDepth;
  /**
   * Amount of oil extracted in total.
   */
  private int oilExtracted;
  /**
   * Amount of oil available to sell.
   */
  private int oilAvailabletoSell;
  /**
   * Amount of cars in oilfield.
   * Cars are needed to sell oil.
   */
  private int carsAmount;
  /**
   * Amount of drills in oilfield.
   * Drills are needed to dril (bruh).
   */
  private int drillsAmount;
  /**
   * Amount of pumps in oilfield.
   * Pumps are needed to extract oil from oilfield.
   */
  private int pumpsAmount;

  /**
   * Constructor of an oilfield.
   * 
   * @param name name of the oilfield
   */
  public Oilfield(final String name) {
    Random random = new Random();

    this.name = name;
    this.price = random.nextInt(70000) + 29900;
    this.ownership = null;
    this.totalOilAmount = (this.price - random.nextInt(9999) + 1) * 10;
    this.isExploitable = false;
    this.requiredDepth = random.nextInt(3666) + 1;
    this.currentDepth = 0;
    this.oilExtracted = 0;
    this.oilAvailabletoSell = 0;
    this.carsAmount = 0;
    this.drillsAmount = 0;
    this.pumpsAmount = 0;
  }

  /**
   * Method for checking is oilfield already bought.
   * 
   * @return is oilfield bought
   */
  public boolean isBought() {
    if (this.ownership == null) {
      return false;
    }
    return true;
  }

  /**
   * Method to extract oil in the oilfield.
   * This is executed every round.
   */
  public void extractOil() {
    this.oilAvailabletoSell += 8000 * this.pumpsAmount;
    this.oilExtracted += 8000 * this.pumpsAmount;
  }

  /**
   * Method to sell oil from the oilfield.
   * 
   * @param oilAmount amount of oil to sell
   */
  public void sellOil(final int oilAmount) {
    this.oilAvailabletoSell -= oilAmount;
  }

  /**
   * Dig in the oilfield.
   * This is executed every round.
   */
  public void dig() {
    Random random = new Random();

    this.drillsAmount -= 500;
    this.currentDepth += 500 - (random.nextInt(30) + 1);
  }

  /**
   * Increase product amount based on industry type.
   * 
   * @param industryType   type of an industry
   * @param productsAmount amount of prodduct produced by the industry
   */
  public void addProductAmount(
      final Class<?> industryType, final int productsAmount) {
    if (industryType.equals(CarsIndustry.class)) {
      this.carsAmount += productsAmount;
    } else if (industryType.equals(DrillsIndustry.class)) {
      this.drillsAmount += 500 * productsAmount;
    } else if (industryType.equals(PumpsIndustry.class)) {
      this.pumpsAmount += productsAmount;
    } else {
      return;
    }
  }

  /**
   * Initialize the oilfields.
   * 
   * @return array of the oilfields
   */
  public static Oilfield[] initialize() {
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

  /**
   * Buy the oilfield by a player.
   * 
   * @param player         player that is buying the oilfield
   * @param gameProperties GameProperties object
   * @throws InterruptedException interruption signal exception
   */
  public static void buyField(
      final Player player, final GameProperties gameProperties)
      throws InterruptedException {
    // Prepare new graphical settings
    Panel contentPanel = gameProperties.contentPanel;
    contentPanel.setLayoutManager(new GridLayout(1));
    gameProperties.window.setTheme(
        SimpleTheme.makeTheme(
            false,
            TextColor.ANSI.BLACK,
            TextColor.ANSI.BLUE_BRIGHT,
            TextColor.ANSI.YELLOW_BRIGHT,
            TextColor.ANSI.BLUE,
            TextColor.ANSI.WHITE_BRIGHT,
            TextColor.ANSI.CYAN,
            TextColor.ANSI.BLUE_BRIGHT));

    // Display title
    Panel titlePanel = new Panel(new GridLayout(1));
    Game.timeBuffor();
    titlePanel.setTheme(
        new SimpleTheme(
            TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT));
    titlePanel.addComponent(new EmptySpace());
    Game.timeBuffor();
    titlePanel.addComponent(
        new Label(
            gameProperties.langMap.get("oilfieldsSale")));
    titlePanel.addComponent(
        new Label(
            gameProperties.langMap.get("balance2")
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
            gameProperties.langMap.get("oldOwnership")));
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
        gameProperties.langMap.get("name"),
        gameProperties.langMap.get("price"));

    // Add every available oilfield to table
    oilfieldsTable.getTableModel().addRow("0", "-", "-");
    for (int oilfieldIndex = 0; oilfieldIndex < gameProperties.oilfields.length; oilfieldIndex++) {
      if (!gameProperties.oilfields[oilfieldIndex].isBought()) {
        // If oilfield is not bought, make it possible to buy it
        oilfieldsTable
            .getTableModel()
            .addRow(
                String.valueOf(oilfieldIndex + 1),
                gameProperties.oilfields[oilfieldIndex].getName(),
                String.valueOf(gameProperties.oilfields[oilfieldIndex].getPrice()) + "$");
      }
    }

    oilfieldsTable.setSelectAction(
        () -> {
          gameProperties.tmpConfirm = true;
        });

    // Display table
    oilfieldsPanel.addComponent(oilfieldsTable);
    oilPanel.addComponent(oilfieldsPanel);

    // Display oilfield details
    contentPanel.addComponent(oilPanel);
    oilfieldsTable.takeFocus();
    contentPanel.addComponent(new EmptySpace());
    contentPanel.addComponent(new Label(gameProperties.langMap.get("oilfieldsPrompt")));

    // Wait for selection
    Game.waitForConfirm(gameProperties);
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
    gameProperties.oilfields[selectedOilfieldIndex].setOwnership(player);

    player.decreaseBalance(gameProperties.oilfields[selectedOilfieldIndex].getPrice());

    // Inform user about purchase
    contentPanel.addComponent(new EmptySpace());
    contentPanel.addComponent(new Label(gameProperties.langMap.get("youAreOwnerOfOilfield") + ":"));
    contentPanel.addComponent(
        new Label(gameProperties.oilfields[selectedOilfieldIndex].getName())
            .setTheme(new SimpleTheme(TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.BLUE_BRIGHT)));

    Thread.sleep(2000);

    // Clean up
    contentPanel.removeAllComponents();
  }
}
