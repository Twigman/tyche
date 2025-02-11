package de.qwyt.housecontrol.tyche.model.profile.color;

import java.util.List;

import lombok.Data;

@Data
public class HueColorProfile {
	// Independent
	private String alert;
	
	private String effect;
	
	private Integer bri;
	
	// Hs
	private Integer hue;
	
	private Integer sat;
	
	// ct
	private Integer ct;
	
	// xy
	private List<Double> xy;
}
