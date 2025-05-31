package de.qwyt.housecontrol.tyche.model.light.hue;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "HueLightStates")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFilter("dynamicFilter")
public class HueLightState {
	
	@Id
	private String id;
	
	@JsonProperty("alert")
	private String alert;
	
	@JsonProperty("bri")
	private Integer bri;
	
	@JsonProperty("colormode")
	private String colormode;
	
	@JsonProperty("ct")
	private Integer ct;
	
	@JsonProperty("effect")
	private String effect;
	
	@JsonProperty("hue")
	private Integer hue;
	
	// "on" doesnt work for yaml configuration
	@JsonProperty("on")
	private Boolean enabled;
	
	@JsonProperty("reachable")
	private Boolean reachable;
	
	@JsonProperty("sat")
	private Integer sat;
	
	@JsonProperty("xy")
	private List<Double> xy;
	
	@JsonProperty("ignoreSensors")
	private Boolean ignoreSensors;
	
	@Indexed
	private String lightId;
	
	@LastModifiedDate
	private Instant timestamp;
	
	// extra field for tyche
	@JsonProperty("hueColorProfileType")
	private HueColorProfileType colorProfile;

	/*
	public void setOn(Boolean value) {
		this.enabled = value;
	}
	
	public Boolean isOn() {
		return this.enabled;
	}*/
	
	public Boolean isEnabled() {
		return this.enabled;
	}
	
	public Boolean isIgnoreSensors() {
		return this.ignoreSensors;
	}
}
