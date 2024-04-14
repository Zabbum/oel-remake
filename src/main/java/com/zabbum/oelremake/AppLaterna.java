package com.zabbum.oelremake;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.Font;
import java.awt.FontFormatException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.TextGUIThreadFactory;
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
        terminalFactory.setInitialTerminalSize(new TerminalSize(47, 32));
        terminalFactory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));

        Screen screen = null;
        SeparateTextGUIThread textGUIThread = null;

        // Program
        try {
            // Initialize screen and terminal
            Terminal terminal = terminalFactory.createTerminalEmulator();
            screen = new TerminalScreen(terminal);

            // Start screen
            screen.startScreen();

            // Create gui
            WindowBasedTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));

            TextGUIThreadFactory textGUIThreadFactory = new SeparateTextGUIThread.Factory();
            textGUIThread = (SeparateTextGUIThread)(textGUIThreadFactory.createTextGUIThread(gui));
            textGUIThread.start();
            gameProperties.textGUIThread = textGUIThread;
            System.out.println(textGUIThread.getState());

            // Create window
            Window window = new BasicWindow();
            window.setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.RED));
            window.setHints(Arrays.asList(Hint.CENTERED, Hint.NO_POST_RENDERING));
            gameProperties.window = window;

            // Display
            gui.addWindow(window);

            // Create content panel
            gameProperties.contentPanel = new Panel(new GridLayout(2));
            Panel contentPanel = gameProperties.contentPanel;

            // Create window manager
            GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(2);

            // Set contentPanel to be displayed
            window.setComponent(contentPanel);

            // Display oel logo
            Game.oelLogo(gameProperties);
            
            // Get a player number
            Game.promptPlayerAmount(gameProperties);

            // Intro info for player and prompt for names
            Game.promptPlayerNames(gameProperties);

            // Inform about money amount and oil prices
            Game.moneyInfo(gameProperties);

            // TODO: PLAY
            for (int round = 0; round < gameProperties.roundCount; round++) {
                Game.playRound(gameProperties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(screen != null) {
                try {
                    screen.stopScreen();
                    textGUIThread.stop();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
