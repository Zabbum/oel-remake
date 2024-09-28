package zabbum.oelremake;

import com.googlecode.lanterna.gui2.Button;

public class Elements {
    // The new confirm button
    public static Button newConfirmButton(Confirm confirm, String text) {
        return new Button(text, confirm::confirm);
    }
}
