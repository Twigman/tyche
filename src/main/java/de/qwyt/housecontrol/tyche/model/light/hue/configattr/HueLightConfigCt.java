package de.qwyt.housecontrol.tyche.model.light.hue.configattr;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HueLightConfigCt {
	@JsonProperty("startup")
    private Object startup;
}
