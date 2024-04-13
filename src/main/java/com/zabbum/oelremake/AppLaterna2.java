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
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

public class AppLaterna2 {
    public static void main(String[] args) throws IOException, FontFormatException, InterruptedException {
        // Set font
        Font fontTmp = Font.createFont(Font.TRUETYPE_FONT, new File("/home/pawel/oel-remake/lib/font/C64_Pro_Mono-STYLE.ttf"));
        Font font = fontTmp.deriveFont(20.0f);

        // Initialize terminal
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(40, 25));

        // Create terminal and screen layers
        Terminal terminal = defaultTerminalFactory.createTerminalEmulator();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create window
        BasicWindow window = new BasicWindow();
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));
        window.setTheme(new SimpleTheme(TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.RED));
        
        // Create panel
        Panel contentPanel = new Panel();
        contentPanel.setFillColorOverride(TextColor.ANSI.RED);
        contentPanel.setLayoutManager(new GridLayout(1)); // TODO: Make layout manager less shitty

        // TODO: Display a fancy Oel Logo
        contentPanel.addComponent(new Label("OEL"));

        // TEMPORARY: Display a less fancy Oel logo
        contentPanel.addComponent(new EmptySpace());

        // TODO: Prompt for player amount
        contentPanel.addComponent(new Label("ILU BEDZIE KAPITALISTOW (2-6)"));
        TextBox playerAmount = new TextBox().setValidationPattern(Pattern.compile("[0-9]"));
        contentPanel.addComponent(playerAmount);

        System.out.println(playerAmount.getText());

        
        // Add panel to window
        window.setComponent(contentPanel);

        // Create gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.RED));
        gui.addWindowAndWait(window);

        screen.stopScreen();
        screen.close();
    }
}