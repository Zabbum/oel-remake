package zabbum.oelremake.artloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.gui2.ImageComponent;
import lombok.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public @Data class ArtObject {
    // Private variables
    private int width;
    private int height;
    private TextColor defaultForegroundColor;
    private TextColor defaultBackgroundColor;
    private TextImage textImage;

    // Constructor
    public ArtObject(InputStream inputStream)
            throws IOException,
            ColorNotFoundException,
            BadImageSizeProvidedException {

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        String json = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));

        ObjectMapper objectMapper = new ObjectMapper();
        Art art = objectMapper.readValue(json, Art.class);

        this.width = art.getWidth();
        this.height = art.getHeight();
        this.defaultForegroundColor = toTextColor(art.getDefaultForegroundColor());
        this.defaultBackgroundColor = toTextColor(art.getDefaultBackgroundColor());

        createTextImage(art);
    }

    public ImageComponent getImageComponent() {
        ImageComponent imageComponent = new ImageComponent();
        imageComponent.setTextImage(textImage);

        return imageComponent;
    }

    // Create TextImage object
    private void createTextImage(Art art)
            throws ColorNotFoundException, BadImageSizeProvidedException {
        // Create canvas
        textImage =
                new BasicTextImage(
                        new TerminalSize(width, height),
                        TextCharacter.fromCharacter(' ', defaultForegroundColor, defaultBackgroundColor)[0]);

        // Create array with all the rows
        List<List<Series>> rows = art.getRows();

        // For every row
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            // If accessing bigger index, than provided in file,
            // throw exception
            if (rowIndex >= height) {
                throw new BadImageSizeProvidedException();
            }

            // Column index
            int columnIndex = 0;

            // For every character sequence
            for (int seriesIndex = 0;
                 seriesIndex < rows.get(rowIndex).size();
                 seriesIndex++) {
                // If accessing bigger index, than provided in file,
                // throw exception
                if (columnIndex >= width) {
                    throw new BadImageSizeProvidedException();
                }

                // Object with sequence
                Series series = rows.get(rowIndex).get(seriesIndex);

                // Declare colors
                TextColor fColor;
                TextColor bColor;

                // Set foreground color for current sequence
                if (series.getFColor() != null) {
                    fColor = toTextColor(series.getFColor());
                } else {
                    fColor = defaultForegroundColor;
                }

                // Set Background color for current sequence
                if (series.getBColor() != null) {
                    bColor = toTextColor(series.getBColor());
                } else {
                    bColor = defaultBackgroundColor;
                }

                // For each character in sequence
                for (char currChar : series.getContent().toCharArray()) {
                    // Draw character
                    textImage.setCharacterAt(
                            new TerminalPosition(columnIndex, rowIndex),
                            TextCharacter.fromCharacter(currChar, fColor, bColor)[0]);

                    // Increment column index
                    columnIndex++;
                }
            }
        }
    }

    // Convert String to TextColor
    private static TextColor toTextColor(String textColorName) throws ColorNotFoundException {
        TextColor[] colors = TextColor.ANSI.class.getEnumConstants();
        for (TextColor color : colors) {
            if (color.toString().equals(textColorName)) {
                return color;
            }
        }

        throw new ColorNotFoundException("Not found such a color: " + textColorName);
    }
}
