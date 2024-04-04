public class CarsProd {

    // Local variables
    private String name;
    private int prize;
    public boolean isBought;
    public Player ownership;
    public int amount;

    // Reference to the next object
    // (stricte for ownership data structure)
    CarsProd next;

    // Constructor
    public CarsProd(String name, int prize, int amount) {
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

    public static CarsProd[] initialize() {
        CarsProd[] carsProds = new CarsProd[4];

        // Cars productions initialization
        carsProds[0] = new CarsProd("WÓZ-PRZEWÓZ",     56700, 30);
        carsProds[1] = new CarsProd("WAGONENSITZ",     94830, 42);
        carsProds[2] = new CarsProd("WORLD CO.",       62596, 33);
        carsProds[3] = new CarsProd("DRINK TANK INC.", 81217, 39);

        return carsProds;
    }
}