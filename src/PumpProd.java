public class PumpProd {

    // Local variables
    private String name;
    private int price;
    public boolean isBought;
    public Player ownership;
    public int amount;

    // Reference to the next object
    // (stricte for ownership data structure)
    PumpProd next;

    // Constructor
    public PumpProd(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.isBought = false;
        this.ownership = null;
        this.amount = amount;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Price getter
    public int getPrice() {
        return price;
    }

    public static PumpProd[] initialize() {
        PumpProd[] pumpProds = new PumpProd[2];

        // Pump productions initialization
        pumpProds[0] = new PumpProd("ZASSANICKI GMBH", 59829, 60);
        pumpProds[1] = new PumpProd("DR PUMPENER",     54754, 60);

        return pumpProds;
    }

}
