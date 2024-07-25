package zabbum.oelremake;

import java.util.HashMap;
import java.util.Map;

public class CliArgumentsParser {
  public static Map<String, String> parseArguments(String[] args) {
    Map<String, String> arguments = new HashMap<>();

    for (String arg : args) {
      if (arg.startsWith("--")) {
        String[] parts = arg.substring(2).split("=");

        // If argument doesn't have value, use itself as value
        if (parts.length == 1) {
          arguments.put(parts[0], parts[0]);
        }

        // If argument has value, use the value
        else if (parts.length == 2) {
          arguments.put(parts[0], parts[1]);
        }
      }
    }

    return arguments;
  }
}
