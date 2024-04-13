package com.zabbum.oelremake;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.regex.Pattern;
import java.lang.Integer;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.AbsoluteLayout;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.ImageComponent;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.Window.Hint;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

public class AppLaterna {
    public static void main(String[] args) {
        final GameProperties gameProperties = new GameProperties(34);

        // Set font
        Font font = null;
        try {
            Font fontTmp = Font.createFont(Font.TRUETYPE_FONT, new File("/home/pawel/oel-remake/lib/font/C64_Pro_Mono-STYLE.ttf"));
            font = fontTmp.deriveFont(15.0f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return;
        }

        // Config terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));

        // Create uninitialized screen object
        Screen screen = null;

        // Program
        try {
            // Initialize screen and terminal
            Terminal terminal = terminalFactory.createTerminalEmulator();
            screen = new TerminalScreen(terminal);

            // Start screen
            screen.startScreen();

            // Create gui
            final WindowBasedTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));

            // Create window
            final Window window = new BasicWindow();
            window.setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.RED));
            window.setHints(Arrays.asList(Hint.CENTERED, Hint.NO_POST_RENDERING));

            // Create content panel
            Panel contentPanel = new Panel(new GridLayout(2));

            // Create window manager
            GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(2);


            // Display shitty OEL logo
            Label oelLogo = new Label("OEL");
            oelLogo.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, GridLayout.Alignment.BEGINNING,
                true,
                false,
                2,
                1));
            contentPanel.addComponent(oelLogo);
            contentPanel.addComponent(Game.emptyLine(2));

            // Prompt for players amount
            contentPanel.addComponent(new Label("ILU BEDZIE KAPITALISTOW (2-6)"));
            TextBox playerAmountTextBox = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
            contentPanel.addComponent(playerAmountTextBox);
            contentPanel.addComponent(new EmptySpace());
            
            contentPanel.addComponent(
                new Button("GOTOWE", new Runnable() {
                    @Override
                    public void run() {
                        gameProperties.playerAmount = Integer.parseInt(playerAmountTextBox.getText());
                        System.out.println(gameProperties.playerAmount);
                        window.close();
                    }
                })
            );

            // Set contentPanel to be displayed
            window.setComponent(contentPanel);

            // Display
            gui.addWindowAndWait(window);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (screen != null) {
                try {
                    screen.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
