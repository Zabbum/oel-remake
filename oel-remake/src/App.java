import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Intro
        System.out.println("OEL");
        System.out.println("The big game and the big money.\n");

        // Scanner for input
        Scanner scanner = new Scanner(System.in);

        // Prompt for players amount
        System.out.println("Ilu będzie kapitalistów (2-6)");
        int playerAmount = scanner.nextInt();
        System.out.println();

        // Prompt for names
        System.out.println("Znajdujemy się obecnie w roku:");
        System.out.println("1986\n");
        System.out.println("Gra kończy się w roku 2020");
        System.out.println("i odbędzie się przy udziale:");
        System.out.println("Proszę wpisać imiona");
        
        // Create objects of players
        Player[] players = new Player[playerAmount];
        for (int i = 0; i < players.length; i++) {
            System.out.print(i+1 + ": ");
            String name = scanner.next();
            players[i] = new Player(name);
        }
        System.out.println();

        // Close scanner object
        scanner.close();

        // Quick guide
        System.out.println("Każdy gracz posiada 124321$ kapitału.\n");
        System.out.println("Wygrywa ten, kto osiągnie największy kapitał na końcu gry.");
    }
}
