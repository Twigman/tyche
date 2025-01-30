package de.qwyt.housecontrol.tyche.model.light.hue;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection = "HueLights")
public class HueLight {
	
	@Id
	@JsonProperty("uniqueid")
	private String uniqueId;
	
	@JsonProperty("colorcapabilities")
	private Integer colorcapabilities;
	
	@JsonProperty("ctmax")
	private Integer ctmax;
	
	@JsonProperty("ctmin")
	private Integer ctmin;
	
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
	
	@DBRef
	@JsonProperty("config")
	private HueLightConfigAttr config;
	
	@DBRef
	@JsonProperty("capabilities")
	private HueLightCapabilities capabilities;
	
	@DBRef
	@JsonProperty("state")
	private HueLightState state;
	
	
	public String getNameAndIdInfo() {
		return String.format("%s (%s)", this.name, this.uniqueId);
	}
	
}
