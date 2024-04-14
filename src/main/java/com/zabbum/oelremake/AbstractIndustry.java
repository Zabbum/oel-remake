package com.zabbum.oelremake;

public abstract class AbstractIndustry {
    protected String name;
    public int industryPrice;
    public boolean isBought;
    public Player ownership;

    public int productsAmount;
    public double productPrice;

    // Constructor
    public AbstractIndustry(String name) {
        this.name = name;
        this.isBought = false;
        this.ownership = null;
    }
}
