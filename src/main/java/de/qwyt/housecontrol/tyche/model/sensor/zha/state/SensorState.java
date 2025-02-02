package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.qwyt.housecontrol.tyche.model.sensor.zha.CLIPDaylightOffsetSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.DaylightSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.HumiditySensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.LightLevelSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PresenceSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.PressureSensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.model.sensor.zha.TemperatureSensor;
import lombok.Data;

@Data
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)
@JsonSubTypes({
	//@JsonSubTypes.Type(value = DimmerSensor.class, name = "ZHASwitch"),
	@JsonSubTypes.Type(value = PresenceSensorState.class, name = "ZHAPresence"),
	@JsonSubTypes.Type(value = LightLevelSensorState.class, name = "ZHALightLevel"),
	@JsonSubTypes.Type(value = TemperatureSensorState.class, name = "ZHATemperature"),
	@JsonSubTypes.Type(value = PressureSensorState.class, name = "ZHAPressure"),
	@JsonSubTypes.Type(value = HumiditySensorState.class, name = "ZHAHumidity"),
	@JsonSubTypes.Type(value = DaylightSensorState.class, name = "Daylight"),
	@JsonSubTypes.Type(value = CLIPDaylightOffsetSensorState.class, name = "CLIPDaylightOffset")
})
@Document(collection = "SensorStates")
public abstract class SensorState {

	@Id
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id;
	
	@JsonProperty("lastupdated")
	private Date lastupdated;
	
	private String sensorId;
}
