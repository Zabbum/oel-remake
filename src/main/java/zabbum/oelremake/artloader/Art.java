package zabbum.oelremake.artloader;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Art {
    private int width;
    private int height;

    @JsonProperty("defaultForegroundColor")
    private String defaultForegroundColor;

    @JsonProperty("defaultBackgroundColor")
    private String defaultBackgroundColor;

    private List<List<Series>> rows;
}
