package de.qwyt.housecontrol.tyche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import de.qwyt.housecontrol.tyche.service.webservice.interceptor.FritzboxAuthInterceptor;
import de.qwyt.housecontrol.tyche.service.webservice.interceptor.SoapLoggerInterceptor;


@Configuration
public class SoapConfig {
	
	@Value("${fritzbox.username}")
	private String username;
	
	@Value("${fritzbox.pw}")
	private String pw;
	
	@Value("${fritzbox.tr064.endpoints.hosts}")
	private String endpointHosts;
	
	@Bean
	public WebServiceTemplate tr064Template(Jaxb2Marshaller marshaller) {
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMarshaller(marshaller);
		template.setUnmarshaller(marshaller);
		/*
		template.setInterceptors(new ClientInterceptor[] {
				new FritzboxAuthInterceptor(username, pw),
				new SoapLoggerInterceptor()
		});*/
		
		return template;
	}
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Setzt den Pfad zu deinen generierten JAXB-Klassen
        marshaller.setContextPath("de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064");
        return marshaller;
	}
}
