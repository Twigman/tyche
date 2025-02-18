package de.qwyt.housecontrol.tyche.config;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfile;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;

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
		
		// Condition: Only override 'bri', if the profile is DEFAULT_CT_BRI
		Condition<Integer, Integer> mapBriCondition = context -> {
			HueLightState state = (HueLightState) context.getParent().getDestination();
			
			return state.getColorProfile() == HueColorProfileType.DEFAULT_CT_BRI;
		};
		
		
		modelMapper.addMappings(new PropertyMap<HueColorProfile, HueLightState>() {

			@Override
			protected void configure() {
				// managed by HueColorProfile
				map(source.getAlert(), destination.getAlert());
				map(source.getEffect(), destination.getEffect());
				
				// managed by HueColorProfile
                map(source.getHue(), destination.getHue());
                map(source.getSat(), destination.getSat());
                // OR
                map(source.getCt(), destination.getCt());
                // OR
                map(source.getXy(), destination.getXy());
                
                // special cause of deCONZ API
                when(mapBriCondition).map(source.getBri(), destination.getBri());
			}
		});
		
		return modelMapper;
	}
}
