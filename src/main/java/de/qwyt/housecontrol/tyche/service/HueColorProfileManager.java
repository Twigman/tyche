package de.qwyt.housecontrol.tyche.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.model.light.hue.HueLightState;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfile;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileProperties;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileType;

@Component
public class HueColorProfileManager {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final HueColorProfileProperties colorProfileProperties;
	
	private final ModelMapper modelMapper;
	
	@Autowired
	public HueColorProfileManager(
			HueColorProfileProperties colorProfileProperties, 
			@Qualifier("colorProfileModelMapper") ModelMapper modelMapper
			) {
		this.colorProfileProperties = colorProfileProperties;
		this.modelMapper = modelMapper;
	}
	
	public HueLightState applyProfile(HueLightState state, HueColorProfileType profileType) {
		HueColorProfile profile = this.colorProfileProperties.getProfiles().get(profileType);
		state.setColorProfile(profileType);
		modelMapper.map(profile, state);
		
		return state;
	}
}
