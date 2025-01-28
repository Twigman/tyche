package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "HueLights")
public class HueLight {
	@Id
	private String id;
	
	@JsonProperty("colorcapabilities")
	private int colorcapabilities;
	
	@JsonProperty("ctmax")
	private int ctmax;
	
	@JsonProperty("ctmin")
	private int ctmin;
	
	@JsonProperty("hascolor")
	private boolean hascolor;
	
	@JsonProperty("lastannounced")
	private Date lastannounced;
	
	@JsonProperty("lastseen")
	private Date lastseen;
	
	@JsonProperty("etag")
	private String etag;
	
	@JsonProperty("modelid")
	private String modelId;
	
	@JsonProperty("manufacturername")
	private String manufacturer;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("swversion")
	private String swversion;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("uniqueid")
	private String uniqueId;
	
	@JsonProperty("config")
	private HueLightConfigAttr config;
	
	@JsonProperty("capabilities")
	private HueLightCapabilities capabilities;
	
	@JsonProperty("state")
	private HueLightState state;
}
