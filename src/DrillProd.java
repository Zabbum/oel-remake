public class DrillProd {

    // Private variables
    private String name;
    private int prize;
    public boolean isBought;
    public Player ownership;
    public int amount;

    // Constructor
    public DrillProd(String name, int prize, int amount) {
        this.name = name;
        this.prize = prize;
        this.isBought = false;
        this.ownership = null;
        this.amount = amount;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Prize getter
    public int getPrize() {
        return prize;
    }

    public static DrillProd[] initialize() {
        DrillProd[] drillProds = new DrillProd[3];

        // Dril productions initialization
        drillProds[0] = new DrillProd("TURBOWIERT",    41124, 57);
        drillProds[1] = new DrillProd("NA BŁYSK INC.", 43149, 57);
        drillProds[2] = new DrillProd("PET SHOP&BOYS", 32919, 49);

        return drillProds;
    }
}
