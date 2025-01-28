package de.qwyt.housecontrol.tyche.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.qwyt.housecontrol.tyche.model.sensor.zha.Sensor;
import de.qwyt.housecontrol.tyche.util.json.SensorDeserializer;

@Configuration
public class MapperConfig {
	
	@Bean
	public ModelMapper notNullModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		
		return modelMapper;
	}
	
	/*@Bean
	public ObjectMapper sensorObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		
		// register custom deserializer for automatic insertion of "type" attribute in "state"
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Sensor.class, new SensorDeserializer());
		mapper.registerModule(module);
		
		return mapper;
	}*/

}
