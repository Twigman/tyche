package de.qwyt.housecontrol.tyche.model.sensor.zha;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;
import de.qwyt.housecontrol.tyche.util.ChangeChecker;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Note: Integer is used instead of int to allow null values.
 * Null is assigned if the field is missing in json.
 * In that case, the ModelMapper doesn't update the field, which is important.
 * 
 * TODO: Missing fields are: specific config, lastannounced and lastseen
 */
@Data
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = DimmerSwitch.class, name = "ZHASwitch"),
	@JsonSubTypes.Type(value = PresenceSensor.class, name = "ZHAPresence"),
	@JsonSubTypes.Type(value = LightLevelSensor.class, name = "ZHALightLevel"),
	@JsonSubTypes.Type(value = TemperatureSensor.class, name = "ZHATemperature"),
	@JsonSubTypes.Type(value = PressureSensor.class, name = "ZHAPressure"),
	@JsonSubTypes.Type(value = HumiditySensor.class, name = "ZHAHumidity"),
	@JsonSubTypes.Type(value = DaylightSensor.class, name = "Daylight"),
	@JsonSubTypes.Type(value = CLIPDaylightOffsetSensor.class, name = "CLIPDaylightOffset")
})
@Document(collection = "Sensors")
public abstract class Sensor {

	//@Id
	//@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	//private String id;
	
	@Id
	@JsonProperty("uniqueid")
	private String uniqueId;
	
	@JsonProperty("ep")
	private Integer ep;
	
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
	
	@JsonProperty("config")
	@Transient
	private GeneralSensorConfig config;
	
	@JsonProperty("state")
	@Transient
	private SensorState state;
	
	
	public SensorState getState() {
		return this.state;
	}
	
	public String getNameAndIdInfo() {
		// String Templates not supported in JDK 21
		//return STR."${this.getClass().getSimpleName()} (this.getUniqueId())";
		return String.format("%s (%s)", this.name, this.getUniqueId());
	}
	
	/**
	 * Compares the relevant fields of the current {@link Sensor} object with the provided sensor 
	 * to detect whether any of the fields that are relevant for database updates have changed.
	 * <p>
	 * The method checks if there is any change in the following fields:
	 * <ul>
	 *     <li>ep</li>
	 *     <li>uniqueId</li>
	 *     <li>etag</li>
	 *     <li>modelId</li>
	 *     <li>manufacturer</li>
	 *     <li>name</li>
	 *     <li>swversion</li>
	 *     <li>type</li>
	 * </ul>
	 * If any of these fields differ between the current {@link Sensor} object and the provided one,
	 * the method will return true, indicating that a relevant change for database persistence has been detected.
	 * If none of the fields have changed, it will return false.
	 *
	 * @param s the {@link Sensor} object to compare against
	 * @return true if any relevant field has changed; false otherwise
	 */
	public boolean hasChangedValuesRelevantForDatabase(Sensor s) {
	    // compare only relevant fields
	    if (ChangeChecker.hasChanged(this.ep, s.ep)) return true;
	    if (ChangeChecker.hasChanged(this.uniqueId, s.uniqueId)) return true;
	    if (ChangeChecker.hasChanged(this.etag, s.etag)) return true;
	    if (ChangeChecker.hasChanged(this.modelId, s.modelId)) return true;
	    if (ChangeChecker.hasChanged(this.manufacturer, s.manufacturer)) return true;
	    if (ChangeChecker.hasChanged(this.name, s.name)) return true;
	    if (ChangeChecker.hasChanged(this.swversion, s.swversion)) return true;
	    if (ChangeChecker.hasChanged(this.type, s.type)) return true;
	    
	    return false;
	}
	
}
