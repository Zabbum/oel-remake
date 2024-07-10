package zabbum.oelremake;

import com.googlecode.lanterna.gui2.Button;

public class Elements {
  // Confirm button
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
}
