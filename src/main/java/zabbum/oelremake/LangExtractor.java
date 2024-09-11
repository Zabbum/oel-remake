package zabbum.oelremake;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LangExtractor {
    // Get language data from the file
    public static Map<String, String> getLangData(InputStream inputStream)
            throws IOException, ParseException {
        // Transform InputStream to file
        File dataFile = File.createTempFile("lang", ".json");
        FileOutputStream out = new FileOutputStream(dataFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();

        // Create JSONObject
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(dataFile, StandardCharsets.UTF_8);
        JSONObject dataObject = (JSONObject) (parser.parse(fileReader));

        // Create Map object for all the data
        Map<String, String> langMap = new HashMap<>();

        // Iterate through the JSONObject and put all the data to Map object
        for (Object key : dataObject.keySet()) {
            String keyString = (String) key;
            String valueString = (String) (dataObject.get(keyString));
            langMap.put(keyString, valueString);
        }

        return langMap;
    }
}
