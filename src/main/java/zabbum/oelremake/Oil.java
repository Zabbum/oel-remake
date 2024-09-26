package zabbum.oelremake;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;

import java.util.Random;

public class Oil {
    // Generate oil prices
    public static Double[] generatePrices(int roundCount) {
        Double[] oilPrices = new Double[roundCount];
        Random random = new Random();

        // Initialize every value
        for (int i = 0; i < oilPrices.length; i++) {
            oilPrices[i] = Double.valueOf(0);
        }

        // First value
        // 7 - 13
        oilPrices[0] = Double.valueOf(random.nextInt(7) + 7);

        // Rest of values
        for (int i = 1; i < oilPrices.length; i++) {
            // Ensure value is correct
            //   Regenerate if less than 1
            while (oilPrices[i] < 1) {
                oilPrices[i] = oilPrices[i - 1] + random.nextInt(14) - 7;
            }
            //   If greater than 20, set to 20
            if (oilPrices[i] > 20) {
                oilPrices[i] = Double.valueOf(20);
            }
        }

        return oilPrices;
    }

    // Generate oil prices graph
    public static TextImage oilGraph(
            GameProperties gameProperties, TextColor mainColor, TextColor backgroundColor) {
        // Create canvas
        TextImage oilGraphImage =
                new BasicTextImage(
                        new TerminalSize(gameProperties.roundCount, 20),
                        TextCharacter.fromCharacter(' ', backgroundColor, backgroundColor)[0]);

        // For every row (20 is the highest possible price)
        for (int row = 0; row < 20; row++) {
            // For each price in round
            for (int round = 0; round < gameProperties.roundCount; round++) {
                // If the price is greater
                // or equal to 20-current row (because we are going from the upper),
                // draw the color
                if (gameProperties.oilPrices[round] >= 20 - row) {
                    oilGraphImage.setCharacterAt(
                            round, row, TextCharacter.fromCharacter(' ', mainColor, mainColor)[0]);
                }
            }
        }

        return oilGraphImage;
    }

    // Reduce prices when the graph has been drawn
    public static void reducePrices(Double[] oilPrices) {
        // Divide each price by 10
        for (int i = 0; i < oilPrices.length; i++) {
            oilPrices[i] /= 10;
        }
    }
}
