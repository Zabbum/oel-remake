public class PumpProd {

    // Private variables
    private String name;
    private int prize;
    public boolean isBought;
    public Player ownership;
    public int amount;

    // Constructor
    public PumpProd(String name, int prize, int amount) {
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

    public static PumpProd[] initialize() {
        PumpProd[] pumpProds = new PumpProd[2];

        // Pump productions initialization
        pumpProds[0] = new PumpProd("ZASSANICKI GMBH", 59829, 60);
        pumpProds[1] = new PumpProd("DR PUMPENER",     54754, 60);

        return pumpProds;
    }

}
