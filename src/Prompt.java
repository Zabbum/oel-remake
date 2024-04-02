import java.util.InputMismatchException;
import java.util.Scanner;

public class Prompt {
    // Prompt for player amount and ensure that provided number is correct
    static public int playerAmount() {
        Scanner scanner = new Scanner(System.in);

        int playerAmount = 0;

        while (playerAmount < 2 || playerAmount > 6) { // Check if number is valid
            try {
                // Prompt for players amount
                System.out.println("Ilu będzie kapitalistów (2-6)");
                playerAmount = scanner.nextInt();
                System.out.println(playerAmount);

            } catch (InputMismatchException e) {
                // If wrong number format, clear buffer
                scanner.nextLine();
            }
        }
        
        scanner.close();
        return playerAmount;
    }
}
