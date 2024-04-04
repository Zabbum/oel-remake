import java.util.Random;

public class Oil {
    // Generate oil prices
    public static double[] generatePrices(int roundCount) {
        double[] oilPrices = new double[roundCount];
        Random random = new Random();

        // Initialize every value
        for (int i = 0; i < oilPrices.length; i++) {
            oilPrices[i] = 0;
        }

        // First value
        // 7 - 13
        oilPrices[0] = random.nextInt(7) + 7;

        // Rest of values
        for (int i = 1; i < oilPrices.length; i++) {
            // Ensure value is correct
            //   Regenerate if less than 1
            while (oilPrices[i] < 1) {
                oilPrices[i] = oilPrices[i-1] + random.nextInt(14) - 7;
            }
            //   If greater than 20, set to 20
            if (oilPrices[i] > 20) {
                oilPrices[i] = 20;
            }
        }

        return oilPrices;
    }

    // Print out the graph of prices
    public static void printGraph(double[] oilPrices, String ANSIGraphColor) {
        // Maximum value is 20, so 20 rows
        for (int i = 20; i > 0; i--) {
            // For each price
            for (double price : oilPrices) {
                // If the value is lesser
                // or equal to current row,
                // draw the color
                if (price >= i) {
                    System.out.print(ANSIGraphColor + " " + ANSI.RESET);
                }
                // If not, leave empty space
                else {
                    System.out.print(" ");
                }
            }
            // After row is completed, pass to the next line
            System.out.print("\n");
        }
    }

    // Reduce prices when the graph has been drawn
    public static double[] reducePrices(double[] oilPrices) {
        // Divide each price by 10
        for (int i = 0; i < oilPrices.length; i++) {
            oilPrices[i] /= 10;
        }

        return oilPrices;
    }
}
