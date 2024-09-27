package zabbum.oelremake;

import com.googlecode.lanterna.gui2.Button;

public class Elements {
    @Deprecated
    // The old method of confirm button with usage of GameProperties.tmpConfirm
    public static Button confirmButton(GameProperties gameProperties) {
        gameProperties.tmpConfirm = false;

        Button button =
                new Button(
                        gameProperties.langMap.get("done"),
                        new Runnable() {
                            @Override
                            public void run() {
                                gameProperties.tmpConfirm = true;
                            }
                        });
        return button;
    }

    // The new confirm button
    public static Button newConfirmButton(Confirm confirm, String text) {
        return new Button(text, confirm::confirm);
    }
}
