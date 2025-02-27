package de.qwyt.housecontrol.tyche.model.sensor.zha.state;

public enum SensorStatesCollectionName {
	TEMPERATURE_SENSOR_STATES("TemperatureSensorStates"),
    HUMIDITY_SENSOR_STATES("HumiditySensorStates"),
    CLIP_DAYLIGHT_OFFSET_SENSOR_STATES("CLIPDaylightOffsetSensorStates"),
    PRESSURE_SENSOR_STATES("PressureSensorStates"),
    PRESENCE_SENSOR_STATES("PresenceSensorStates"),
    LIGHT_LEVEL_SENSOR_STATES("LightLevelSensorStates"),
    DIMMER_SWITCH_SENSOR_STATES("DimmerSwitchStates"),
    DAYLIGHT_SENSOR_STATES("DaylightSensorStates");

    private final String collectionName;

    SensorStatesCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
