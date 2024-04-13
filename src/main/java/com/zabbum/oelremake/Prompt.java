package com.zabbum.oelremake;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Prompt {
    // Prompt for player amount and ensure that provided number is correct
    public static int playerAmount(Scanner scanner) {

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
    public static Player[] playerNames(int playerAmount, Scanner scanner) {

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

    // Prompt for an integer
    public static int promptInt(String[] possibleActions, Scanner scanner) {
        Arrays.sort(possibleActions);
        String action = " ";

        // Get the action and
        // verify that the action is correct,
        // if not then wait for another
        while (Arrays.binarySearch(possibleActions, action) < 0) {
            System.out.print("  ? ");
            action = scanner.nextLine().toUpperCase();
        }
        int result = Integer.parseInt(action) - 1;

        return result;
    }

    // Prompt for a product price
    public static double promptPrice(Scanner scanner) {
        System.out.println(ANSI.RED_BACKGROUND_BRIGHT + ANSI.WHITE + "\nNowa cena?" + ANSI.RESET);
        double proposedPrice = -1;
        while (proposedPrice < 0) {
            System.out.print("  ? ");
            try {
                proposedPrice = scanner.nextDouble();
            } catch (InputMismatchException e) {
                scanner.next();
            }
        }
        return proposedPrice;
    }

    // Prompt for oil amount
    public static int oilAmount(Scanner scanner, int carsAmount, int oilAvailabletoSell) {
        int proposedAmount = -1;
        while (proposedAmount < 0 || proposedAmount > carsAmount * 7000 || proposedAmount > oilAvailabletoSell) {
            System.out.print("  ? ");
            try {
                proposedAmount = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next();
            }
        }
        return proposedAmount;
    }
}