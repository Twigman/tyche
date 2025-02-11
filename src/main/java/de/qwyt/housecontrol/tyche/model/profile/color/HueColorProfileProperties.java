package de.qwyt.housecontrol.tyche.model.profile.color;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "color")
public class HueColorProfileProperties {
	
	private Map<HueColorProfileType, HueColorProfile> profiles;
	
}
