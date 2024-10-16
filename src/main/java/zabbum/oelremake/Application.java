package zabbum.oelremake;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.AsynchronousTextGUIThread.State;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Window.Hint;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

public final class Application {
    /**
     * main() method that is run by default.
     *
     * @param args cli arguments
     * @throws FileNotFoundException file not found
     * @throws IOException           input output exception
     */
    public static void main(final String[] args)
            throws IOException {
        // Final variables
        final Map<String, String> argsMap = CliArgumentsParser.parseArguments(args);
        final GameProperties gameProperties = new GameProperties(34);
        final float fontSize = 15.0f;
        final int terminalWidth = 60;
        final int terminalHeight = 34;

        // Switch to devmode
        if (argsMap.get("devmode") != null) {
            gameProperties.isInDevMode = true;
        }

        // Lang code
        final String lang;
        if (argsMap.get("lang") != null) {
            lang = argsMap.get("lang");
        } else {
            lang = "pl-PL";
        }

        // Set font
        InputStream inputStream = Application.class.getClassLoader()
                .getResourceAsStream("font/C64_Pro_Mono-STYLE.ttf");
        Font font = null;
        try {
            Font fontTmp = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = fontTmp.deriveFont(fontSize);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return;
        }
        inputStream.close();

        // Set lang
        inputStream = Application.class.getClassLoader()
                .getResourceAsStream("lang/" + lang + ".json");
        try {
            gameProperties.langMap = LangExtractor.getLangData(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        inputStream.close();

        // Config terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(
                new TerminalSize(terminalWidth, terminalHeight));
        terminalFactory.setTerminalEmulatorFontConfiguration(
                SwingTerminalFontConfiguration.newInstance(font));
        terminalFactory.setTerminalEmulatorTitle(
                gameProperties.langMap.get("windowTitle"));

        Screen screen = null;
        SeparateTextGUIThread textGUIThread = null;

        // Program
        try {
            // Initialize screen and terminal
            Terminal terminal = terminalFactory.createTerminalEmulator();

            // Set icon
            InputStream iconInputStream = Application.class.getClassLoader()
                    .getResourceAsStream("img/logo.png");
            Image icon = ImageIO.read(iconInputStream);

            ((SwingTerminalFrame) terminal).setIconImage(icon);

            screen = new TerminalScreen(terminal);

            // Change title of App

            // Start screen
            screen.startScreen();

            // Create gui
            var gui = new MultiWindowTextGUI(
                    screen,
                    new DefaultWindowManager(),
                    new EmptySpace(TextColor.ANSI.BLACK));

            var textGUIThreadFactory = new SeparateTextGUIThread.Factory();
            textGUIThread = (SeparateTextGUIThread) (textGUIThreadFactory.createTextGUIThread(gui));
            textGUIThread.start();
            gameProperties.textGUIThread = textGUIThread;
            System.out.println(textGUIThread.getState());

            gameProperties.mainThread = Thread.currentThread();

            // Thread for controlling window close
            Thread windowCloseControllThread = new Thread(
                    () -> {
                        while (!gameProperties.textGUIThread.getState()
                                .equals(State.STOPPED)) {
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
                    SimpleTheme.makeTheme(
                            false,
                            TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.RED,
                            TextColor.ANSI.RED,
                            TextColor.ANSI.BLUE_BRIGHT,
                            TextColor.ANSI.WHITE_BRIGHT,
                            TextColor.ANSI.CYAN,
                            TextColor.ANSI.RED));

            window.setHints(Arrays.asList(Hint.CENTERED, Hint.NO_POST_RENDERING));
            gameProperties.window = window;

            // Display
            gui.addWindow(window);

            // Create content panel
            gameProperties.contentPanel = new Panel(new GridLayout(2));

            // Set contentPanel to be displayed
            window.setComponent(gameProperties.contentPanel);

            // HERE GAME STARTS
            // Display oel logo
            Game.oelLogo(gameProperties);

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
        } catch (InterruptedException ignored) {
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                    textGUIThread.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
