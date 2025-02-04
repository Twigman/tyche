package de.qwyt.housecontrol.tyche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class ObjectMapperFilterConfig {

	@Bean
	public FilterProvider putLightStateFilter() {
		// the filter name (dynamicFilter) needs to be the same as registerd in the JsonFilter-Annotation
		return new SimpleFilterProvider()
				.addFilter(
						"dynamicFilter",
						SimpleBeanPropertyFilter
							.serializeAllExcept(
								"colormode",
								"id",
								"lightId",
								"reachable",
								"timestamp"
								)
						);
	}
}
