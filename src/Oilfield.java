public class Oilfield {

    // Private variables
    private String name;
    private int prize;
    private char state; // f - free, b - bought

    // Constructor
    public Oilfield(String name, int prize) {
        this.name = name;
        this.prize = prize;
        this.state = 'f';
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Prize getter
    public int getPrize() {
        return prize;
    }

    // State getter
    public char getState() {
        return state;
    }

    // State setter
    public void setState(char state) {
        this.state = state;
    }

    public static Oilfield[] initialize() {
        Oilfield[] oilfields = new Oilfield[12];

        // Oilfields initialization
        oilfields[0] = new Oilfield("JASNY GWINT",    42889);
        oilfields[1] = new Oilfield("WIELKA DZIURA",  87842);
        oilfields[2] = new Oilfield("WIERTOWISKO",    92706);
        oilfields[3] = new Oilfield("SMAK WALUTY",    88622);
        oilfields[4] = new Oilfield("MIŁA ZIEMIA",    43086);
        oilfields[5] = new Oilfield("BORUJ-BORUJ",    84250);
        oilfields[6] = new Oilfield("KRASNY POTOK",   87949);
        oilfields[7] = new Oilfield("PŁYTKIE DOŁY",   97598);
        oilfields[8] = new Oilfield("ŚLADY OLEJU",    55396);
        oilfields[9] = new Oilfield("NICZYJ GRUNT",   94501);
        oilfields[10] = new Oilfield("DZIKIE PSY",    87149);
        oilfields[11] = new Oilfield("UGORY NAFTOWE", 68383);

        return oilfields;
    }
}
