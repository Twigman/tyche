package de.qwyt.housecontrol.tyche;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.qwyt.housecontrol.tyche.controller.rest.DeconzRestController;
import de.qwyt.housecontrol.tyche.service.SensorService;
import de.qwyt.housecontrol.tyche.service.WeatherService;
import de.qwyt.housecontrol.tyche.service.WebSocketService;
import jakarta.annotation.PreDestroy;

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
	private WeatherService weatherService;
	
	@Autowired
	private DeconzRestController deconzController;
	
	@Autowired
	private SensorService sensorService;
	
	public static void main(String[] args) {
		SpringApplication.run(TycheApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.debug("Start run");
		LOG.info("Initialising sensors");
		sensorService.registerSensors(deconzController.getSensors());
		
		
		webSocketService.connect(deconzWebSocketUrl);
		
		//weatherService.requestData();
	}

	@PreDestroy
	public void exit() {
		LOG.info("Shutting down services...");
		webSocketService.close();
	}
}
