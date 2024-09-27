package zabbum.oelremake;

import lombok.Data;

@Data
public class Player {

    /**
     * Name of the player.
     */
    private String name;
    /**
     * Balance of the player.
     */
    private double balance;
    /**
     * Debt of the player.
     */
    private int debt;

    // Constructor
    Player(final String name) {
        this.balance = 124321;
        this.name = name;
        this.debt = 0;
    }

    /**
     * Increase player's balance.
     *
     * @param moneyAmount money amount to add to player's account
     */
    public void increaseBalance(final double moneyAmount) {
        this.balance += moneyAmount;
    }

    /**
     * Decrease player's balance.
     *
     * @param moneyAmount money amount to take from player's account
     */
    public void decreaseBalance(final double moneyAmount) {
        this.balance -= moneyAmount;
    }

    /**
     * Take debt.
     */
    public void takeDebt() {
        this.debt += 20000;
        this.balance += 20000;
    }

    /**
     * Pay off debt.
     */
    public void payOffDebt() {
        this.balance -= 5000;
        this.debt -= 3000;
    }
}
