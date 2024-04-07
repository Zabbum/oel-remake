public class Player {

    // Local variables
    public String name;
    public double balance;
    public int debt;

    // Ownerships
    CarsProd ownedCarsProd;
    DrillProd ownedDrillProd;
    Oilfield ownedOilfield;
    PumpProd ownedPumpProd;

    // Constructor
    Player(String name) {
        this.balance = 124321;
        this.name = name;
        this.debt = 0;

        // Set ownerships to null
        this.ownedCarsProd = null;
        this.ownedDrillProd = null;
        this.ownedOilfield = null;
        this.ownedPumpProd = null;
    }
}