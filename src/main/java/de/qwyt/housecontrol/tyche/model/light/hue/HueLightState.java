package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "HueLightStates")
public class HueLightState {
	
	@Id
	private String id;
	
	private String sensorId;
	
	@JsonProperty("alert")
	private String alert;
	
	@JsonProperty("bri")
	private int bri;
	
	@JsonProperty("colormode")
	private String colormode;
	
	@JsonProperty("ct")
	private int ct;
	
	@JsonProperty("effect")
	private String effect;
	
	@JsonProperty("hue")
	private int hue;
	
	@JsonProperty("on")
	private boolean on;
	
	@JsonProperty("reachable")
	private boolean reachable;
	
	@JsonProperty("sat")
	private int sat;
	
	@JsonProperty("xy")
	private List<Double> xy;

}
