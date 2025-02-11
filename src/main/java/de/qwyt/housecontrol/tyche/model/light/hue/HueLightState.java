package de.qwyt.housecontrol.tyche.model.light.hue;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

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
	
	@Indexed
	private String lightId;
	
	@LastModifiedDate
	private Instant timestamp;
	
	/**
	 * Only use "on2" for yaml configuration.
	 * The name "on" combined with the wrapper class Boolean causes problems.
	 * 
	 * @param value
	 */
	/*public void setOn2(Boolean value) {
		this.on = value;
		this.on2 = value;
	}
	
	public Boolean getOn2() {
		return this.on2;
	}*/
	
	public void setOn(Boolean value) {
		this.enabled = value;
	}
	
	public Boolean isOn() {
		return this.enabled;
	}
}
