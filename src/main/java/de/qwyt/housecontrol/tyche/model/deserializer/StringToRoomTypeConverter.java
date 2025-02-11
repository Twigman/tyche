package de.qwyt.housecontrol.tyche.model.deserializer;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.qwyt.housecontrol.tyche.model.group.RoomType;

@Component
@ConfigurationPropertiesBinding
public class StringToRoomTypeConverter implements Converter<String, RoomType>{

	@Override
	public RoomType convert(String source) {
		return RoomType.valueOf(source.toUpperCase());
	}

}
