package com.zabbum.oelremake;

public class GameProperties {
    public int roundCount;
    public int currentRound;
    public int playerAmount;

    public GameProperties(int roundCount) {
        this.roundCount = roundCount;
        this.currentRound = 0;
    }
    
}
