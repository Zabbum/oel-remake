package zabbum.oelremake.artloader;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Series {
    private String content;
    @JsonProperty("fColor")
    private String fColor;
    @JsonProperty("bColor")
    private String bColor;
}