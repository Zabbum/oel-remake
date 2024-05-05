package com.zabbum.oelremake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.json.simple.parser.ParseException;

import java.awt.Font;
import java.awt.FontFormatException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.ImageComponent;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.SeparateTextGUIThread;
import com.googlecode.lanterna.gui2.TextGUIThreadFactory;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.AsynchronousTextGUIThread.State;
import com.googlecode.lanterna.gui2.Window.Hint;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.zabbum.oelremake.artloader.ArtObject;
import com.zabbum.oelremake.artloader.BadImageSizeProvidedException;
import com.zabbum.oelremake.artloader.ColorNotFoundException;

public class AppLaterna {
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, ColorNotFoundException, BadImageSizeProvidedException {
        final GameProperties gameProperties = new GameProperties(34);

        // TEST
        File drill = new File(AppLaterna.class.getClassLoader().getResource("arts/drill.json").getFile());
        ArtObject drillArt = new ArtObject(drill);
        ImageComponent drillArtComponent = new ImageComponent();
        drillArtComponent.setTextImage(drillArt.getTextImage());

        // Set font
        InputStream inputStream = AppLaterna.class.getClassLoader().getResourceAsStream("font/C64_Pro_Mono-STYLE.ttf");
        Font font = null;
        try {
            Font fontTmp = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = fontTmp.deriveFont(15.0f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return;
        }

        // Config terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(53, 34));
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

            gameProperties.mainThread = Thread.currentThread();

            // Thread for controlling window close
            Thread windowCloseControllThread = new Thread(() -> {
                while (!gameProperties.textGUIThread.getState().equals(State.STOPPED)) {
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        // Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
                gameProperties.mainThread.interrupt();
            });

            windowCloseControllThread.start();

            // Create window
            Window window = new BasicWindow();
            window.setTheme(
                SimpleTheme.makeTheme(false,
                    TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.RED,
                    TextColor.ANSI.RED, TextColor.ANSI.BLUE_BRIGHT,
                    TextColor.ANSI.WHITE_BRIGHT, TextColor.ANSI.CYAN,
                    TextColor.ANSI.RED)
            );

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
            contentPanel.addComponent(drillArtComponent);
            
            // Get a player number
            Game.promptPlayerAmount(gameProperties);

            // Intro info for player and prompt for names
            Game.promptPlayerNames(gameProperties);

            // Inform about money amount and oil prices
            Game.moneyInfo(gameProperties);

            // Play
            for (int round = 0; round < gameProperties.roundCount; round++) {
                Game.playRound(gameProperties);
            }

            Game.finishGame(gameProperties);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
