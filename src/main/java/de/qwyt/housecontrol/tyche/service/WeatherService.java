package de.qwyt.housecontrol.tyche.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Value("${meteomatics.url}")
	private String url;
	
	@Value("${home.lat}")
	private String homeLat;
	
	@Value("${home.lon}")
	private String homeLon;
	
	@Autowired
	private RestTemplate restTemplateWithBasicAuth;
	
	public String requestData() {
		String requestUrl = url + "/" + getUtcDateTimeNow() + getParameterUrlFormat() + getHomeLatLon() + "/json";
		
		LOG.debug("weather request url: " + requestUrl);
		
		//ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
		ResponseEntity<String> response = restTemplateWithBasicAuth.getForEntity(requestUrl, String.class);
		
		LOG.debug("weather response: " + response);
		return "z";
	}
	
	private String getUtcDateTimeNow() {
		return OffsetDateTime.now(ZoneOffset.UTC).toString().substring(0, 19) + "Z";
	}
	
	private String getHomeLatLon() {
		return this.homeLat + "," + this.homeLon;
	}
	
	private String getParameterUrlFormat() {
		// temperature at 2 m in Celsius
		// wind speed at 10 m in m/s
		// precipitation during the last hour in mm
		// wind direction at 10 m in degree
		// max temp last 24 h 
		// min temp last 24 h
		// precipitation last 24 h in mm
		// uv index
		// sunrise
		// sunset
		return "/t_2m:C,"
				+ "wind_speed_10m:ms,"
				+ "precip_1h:mm,"
				+ "wind_dir_10m:d,"
				+ "t_max_2m_24h:C,"
				+ "t_min_2m_24h:C,"
				+ "precip_24h:mm,"
				+ "uv:idx,"
				+ "sunrise:sql,"
				+ "sunset:sql/";
	}

}
