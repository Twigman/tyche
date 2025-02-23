package de.qwyt.housecontrol.tyche;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.qwyt.housecontrol.tyche.model.profile.automation.AutomationProfileProperties;
import de.qwyt.housecontrol.tyche.model.profile.color.HueColorProfileProperties;
import de.qwyt.housecontrol.tyche.service.DeconzApiClient;
import de.qwyt.housecontrol.tyche.service.FritzboxService;
import de.qwyt.housecontrol.tyche.service.LightServiceImpl;
import de.qwyt.housecontrol.tyche.service.RoomServiceImpl;
import de.qwyt.housecontrol.tyche.service.SensorServiceImpl;
import de.qwyt.housecontrol.tyche.service.WeatherServiceImpl;
import de.qwyt.housecontrol.tyche.service.websocket.WebSocketService;
import jakarta.annotation.PreDestroy;

@EnableConfigurationProperties({AutomationProfileProperties.class, HueColorProfileProperties.class})
@SpringBootApplication
public class TycheApplication implements CommandLineRunner {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Value("${deconz.websocket.url}")
	private String deconzWebSocketUrl;
	
	@Autowired
	//@Qualifier("jettyWebSocketService")
	@Qualifier("springBasicWebSocketService")
	private WebSocketService webSocketService;
	
	@Autowired
	private WeatherServiceImpl weatherService;
	
	@Autowired
	private DeconzApiClient deconzClient;
	
	@Autowired
	private SensorServiceImpl sensorService;
	
	@Autowired
	private LightServiceImpl lightService;
	
	@Autowired
	private RoomServiceImpl roomService;
	
	@Autowired
	private FritzboxService fritzboxService;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(TycheApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.debug("Start run");
		LOG.info("Initialising sensors");
		sensorService.loadSensorsFromDb();
		sensorService.registerSensors(deconzClient.getSensors());
		LOG.info("Initialising lights");
		lightService.loadLightsFromDb();
		lightService.registerLights(deconzClient.getLights());
		LOG.info("Initialising rooms");
		roomService.saveRoomsinDb();
		
		LOG.debug("Entries: {}", fritzboxService.getHostNumberOfEntries());
		
		LOG.debug("MAC: {}", fritzboxService.getGenericHostEntry(3).getNewMACAddress());
		
		
		
		webSocketService.connect(deconzWebSocketUrl);
		
		//weatherService.requestData();
	}

	@PreDestroy
	public void exit() {
		LOG.info("Shutting down services...");
		webSocketService.close();
	}
}
