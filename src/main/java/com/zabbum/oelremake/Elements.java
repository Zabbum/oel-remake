package com.zabbum.oelremake;

import com.googlecode.lanterna.gui2.Button;

public class Elements {
    // Confirm button
    public static Button confirmButton(GameProperties gameProperties) {
        gameProperties.tmpConfirm = false;
        
        Button button = new Button("GOTOWE", new Runnable() {
            @Override
            public void run() {
                gameProperties.tmpConfirm = true;
            }
        });
        return button;
    }
}
