import java.util.InputMismatchException;
import java.util.Scanner;

public class Prompt {
    // Prompt for player amount and ensure that provided number is correct
    static public int playerAmount(Scanner scanner) {

        int playerAmount = 0;

        while (playerAmount < 2 || playerAmount > 6) { // Check if number is valid
            try {
                // Prompt for players amount
                System.out.println("Ilu będzie kapitalistów (2-6)");
                playerAmount = scanner.nextInt();

            } catch (InputMismatchException e) {
                // If wrong number format, clear buffer
                scanner.nextLine();
            }
        }
        return playerAmount;
    }

    // Prompt for player names and create objects
    static public Player[] playerNames(int playerAmount, Scanner scanner) {

        Player[] players = new Player[playerAmount];

        // Clear scanner buffer
        scanner.nextLine();

        for (int i = 0; i < playerAmount; i++) {
            // Prompt user for name
            System.out.print("  ? ");
            String name = scanner.nextLine();

            // Create player object
            players[i] = new Player(name);
        }
        return players;
    }
}
