package de.qwyt.housecontrol.tyche.model.sensor.zha;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.qwyt.housecontrol.tyche.model.sensor.zha.state.SensorState;

import org.springframework.data.annotation.Id;

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
	//@JsonSubTypes.Type(value = DimmerSensor.class, name = "ZHASwitch"),
	@JsonSubTypes.Type(value = PresenceSensor.class, name = "ZHAPresence"),
	@JsonSubTypes.Type(value = LightLevelSensor.class, name = "ZHALightLevel"),
	@JsonSubTypes.Type(value = TemperatureSensor.class, name = "ZHATemperature"),
	@JsonSubTypes.Type(value = PressureSensor.class, name = "ZHAPressure"),
	@JsonSubTypes.Type(value = HumiditySensor.class, name = "ZHAHumidity"),
	@JsonSubTypes.Type(value = DaylightSensor.class, name = "Daylight"),
	@JsonSubTypes.Type(value = CLIPDaylightOffsetSensor.class, name = "CLIPDaylightOffset")
})
public abstract class Sensor {

	@Id
	private String id;
	
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
	
	// Will not be filled, because it is used to decide which Subclass to load
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("uniqueid")
	private String uniqueId;
	
	@JsonProperty("config")
	private GeneralSensorConfig config;
	
	@JsonProperty("state")
	private SensorState state;
	
	
	public SensorState getState() {
		return this.state;
	}
	
}
