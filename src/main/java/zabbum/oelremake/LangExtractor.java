package zabbum.oelremake;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

public class LangExtractor {
    // Get language data from the file
    public static HashMap getLangData(InputStream inputStream)
            throws IOException {

        // Read data from json file
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        String json = new BufferedReader(reader).lines().collect(Collectors.joining("\n"));

        ObjectMapper objectMapper = new ObjectMapper();

        // Create and return Map object for all the data
        return objectMapper.readValue(json, HashMap.class);
    }
}
