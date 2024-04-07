package com.zabbum.oelremake;

public abstract class Industry {
    private String name;
    public boolean isBought;
    public Player ownership;

    // Constructor
    public Industry(String name) {
        this.name = name;
        this.isBought = false;
        this.ownership = null;
    }

    // Name getter
    public String getName() {
        return name;
    }
}
