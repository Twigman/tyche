package de.qwyt.housecontrol.tyche.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfile;

@Configuration
public class MapperConfig {
	
	@Bean
	public ModelMapper notNullModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		
		return modelMapper;
	}
	
	@Bean 
	public ModelMapper colorProfileModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		// map exact match
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		modelMapper.addMappings(new PropertyMap<HueColorProfile, HueLightState>() {

			@Override
			protected void configure() {
				map(source.getAlert(), destination.getAlert());
				map(source.getEffect(), destination.getEffect());
                map(source.getBri(), destination.getBri());
                map(source.getHue(), destination.getHue());
                map(source.getSat(), destination.getSat());
                map(source.getCt(), destination.getCt());
                map(source.getXy(), destination.getXy());
			}
		});
		
		return modelMapper;
	}
}
