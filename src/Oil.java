import java.util.Random;

public class Oil {
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
}
