import java.util.Random;

public class Oil {
    // Generate oil prizes
    public static double[] generatePrizes(int roundCount) {
        double[] oilPrizes = new double[roundCount];
        Random random = new Random();

        // Initialize every value
        for (int i = 0; i < oilPrizes.length; i++) {
            oilPrizes[i] = -1;
        }

        // First value
        // 7 - 13
        oilPrizes[0] = random.nextInt(7) + 7;

        // Rest of values
        for (int i = 1; i < oilPrizes.length; i++) {
            // Ensure value is correct
            //   Regenerate if less than 0
            while (oilPrizes[i] < 0) {
                oilPrizes[i] = oilPrizes[i-1] + random.nextInt(14) - 7;
            }
            //   If greater than 20, set to 20
            if (oilPrizes[i] > 20) {
                oilPrizes[i] = 20;
            }
        }

        return oilPrizes;
    }

    // Print out the graph of prizes
    public static void printGraph(double[] oilPrizes, String ANSIGraphColor) {
        // Maximum value is 20, so 20 rows
        for (int i = 0; i < 20; i++) {
            // For each prize
            for (double prize : oilPrizes) {
                // If the value is lesser
                // or equal to current row,
                // draw the color
                if (prize <= i) {
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
}
