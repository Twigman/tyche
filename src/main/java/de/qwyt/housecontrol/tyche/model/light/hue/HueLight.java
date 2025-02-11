package de.qwyt.housecontrol.tyche.model.light.hue;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.qwyt.housecontrol.tyche.util.ChangeChecker;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	private Boolean hascolor;
	
	@JsonProperty("etag")
	private String etag;
	
	@JsonProperty("modelid")
	private String modelId;
	
	@JsonProperty("productid")
	private String productId;
	
	@JsonProperty("manufacturername")
	private String manufacturer;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("swconfigid")
	private String swconfigId;
	
	@JsonProperty("swversion")
	private String swversion;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("config")
	private HueLightConfigAttr config;
	
	@JsonProperty("capabilities")
	private HueLightCapabilities capabilities;
	
	@JsonProperty("state")
	@Transient
	private HueLightState state;
	
	
	public String getNameAndIdInfo() {
		return String.format("%s (%s)", this.name, this.uniqueId);
	}
	
	public boolean hasChangedValuesRelevantForDatabase(HueLight l) {
	    // compare only relevant fields
	    if (ChangeChecker.hasChanged(this.uniqueId, l.uniqueId)) return true;
	    if (ChangeChecker.hasChanged(this.colorcapabilities, l.colorcapabilities)) return true;
	    if (ChangeChecker.hasChanged(this.ctmax, l.ctmax)) return true;
	    if (ChangeChecker.hasChanged(this.ctmin, l.ctmin)) return true;
	    if (ChangeChecker.hasChanged(this.hascolor, l.hascolor)) return true;
	    if (ChangeChecker.hasChanged(this.etag, l.etag)) return true;
	    if (ChangeChecker.hasChanged(this.modelId, l.modelId)) return true;
	    if (ChangeChecker.hasChanged(this.manufacturer, l.manufacturer)) return true;
	    if (ChangeChecker.hasChanged(this.name, l.name)) return true;
	    if (ChangeChecker.hasChanged(this.swversion, l.swversion)) return true;
	    if (ChangeChecker.hasChanged(this.type, l.type)) return true;
	    // config
	    if (ChangeChecker.hasChanged(this.config, l.config)) return true;
	    //capabilities
	    if (ChangeChecker.hasChanged(this.capabilities, l.capabilities)) return true;
	    
	    return false;
	}
	
}
