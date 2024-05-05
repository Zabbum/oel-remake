package zabbum.oelremake;

import lombok.Data;

public @Data class Player {

    // Local variables
    private String name;
    private double balance;
    private int debt;

    // Constructor
    Player(String name) {
        this.balance = 124321;
        this.name = name;
        this.debt = 0;
    }

    // Increase balace
    public void increaseBalance(double moneyAmount) {
        this.balance += moneyAmount;
    }

    // Decrease balace
    public void decreaseBalance(double moneyAmount) {
        this.balance -= moneyAmount;
    }

    // Take debt
    public void takeDebt() {
        this.debt += 20000;
        this.balance += 20000;
    }

    // Pay off debt
    public void payOffDebt() {
        this.balance -= 5000;
        this.debt -= 3000;
    }
}