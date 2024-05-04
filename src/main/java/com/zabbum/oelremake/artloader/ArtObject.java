package com.zabbum.oelremake.artloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;

import lombok.Data;


public @Data class ArtObject {
    // Private variables
    private int width;
    private int height;
    private TextColor defaultForegroundColor;
    private TextColor defaultBackgroundColor;
    private TextImage textImage;

    // Constructor
    public ArtObject(File dataFile) throws FileNotFoundException, IOException, ParseException, ColorNotFoundException {
        // Create JSONObject
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(dataFile));
        JSONObject dataObject = (JSONObject) object;

        this.width = Integer.parseInt(dataObject.get("width").toString());
        this.height = Integer.parseInt(dataObject.get("height").toString());
        this.defaultForegroundColor = toTextColor(dataObject.get("defaultForegroundColor").toString());
        this.defaultBackgroundColor = toTextColor(dataObject.get("defaultBackgroundColor").toString());

        createTextImage(dataObject);
    }

    // Create TextImage object
    private void createTextImage(JSONObject dataObject) {
        // Create canvas
        textImage = new BasicTextImage(
            new TerminalSize(width, height),
            TextCharacter.fromCharacter(' ',defaultForegroundColor, defaultBackgroundColor)[0]
        );

        // Create array with all the rows
        JSONArray rows = (JSONArray)dataObject.get("rows");
        System.out.println(rows.size());

        // For every row
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {

            // Column index
            int columnIndex = 0;

            // For every character sequence
            for (int seriesIndex = 0; seriesIndex < ((JSONArray)rows.get(rowIndex)).size(); seriesIndex++) {
                // Object with sequence
                JSONObject series = (JSONObject)(((JSONArray)rows.get(rowIndex)).get(seriesIndex));

                // For every character
                for (int strLen = 0; strLen < ((String)(series.get("content"))).length(); strLen++) {
                    // Set current character
                    char currChar = ((String)(series.get("content"))).charAt(strLen);

                    // Draw character
                    textImage.setCharacterAt(
                        new TerminalPosition(columnIndex, rowIndex),
                        TextCharacter.fromCharacter(currChar,
                            defaultForegroundColor, defaultBackgroundColor
                        )[0]
                    );
                    
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
