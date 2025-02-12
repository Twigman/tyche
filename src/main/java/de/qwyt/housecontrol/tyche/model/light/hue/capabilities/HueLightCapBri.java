package de.qwyt.housecontrol.tyche.model.light.hue.capabilities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightCapBri {
    @JsonProperty("min_dim_level")
    private Integer minDimLevel;
}
