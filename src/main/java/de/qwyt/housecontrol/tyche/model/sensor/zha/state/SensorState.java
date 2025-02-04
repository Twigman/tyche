package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.qwyt.housecontrol.tyche.model.deserializer.FlexibleInstantDeserializier;
import lombok.Data;

@Data
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = DimmerSwitchState.class, name = "ZHASwitch"),
	@JsonSubTypes.Type(value = PresenceSensorState.class, name = "ZHAPresence"),
	@JsonSubTypes.Type(value = LightLevelSensorState.class, name = "ZHALightLevel"),
	@JsonSubTypes.Type(value = TemperatureSensorState.class, name = "ZHATemperature"),
	@JsonSubTypes.Type(value = PressureSensorState.class, name = "ZHAPressure"),
	@JsonSubTypes.Type(value = HumiditySensorState.class, name = "ZHAHumidity"),
	@JsonSubTypes.Type(value = DaylightSensorState.class, name = "Daylight"),
	@JsonSubTypes.Type(value = CLIPDaylightOffsetSensorState.class, name = "CLIPDaylightOffset")
})
public abstract class SensorState {

	@Id
	private String id;
	
	@JsonProperty("lastupdated")
	@JsonDeserialize(using = FlexibleInstantDeserializier.class)
	private Instant lastupdated;
	
	@Indexed
	private String sensorId;
}
