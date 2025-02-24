package de.qwyt.housecontrol.tyche.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import de.qwyt.housecontrol.tyche.service.webservice.FritzboxAuthInterceptor;
import de.qwyt.housecontrol.tyche.service.webservice.SoapLoggerInterceptor;




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
		template.setInterceptors(new ClientInterceptor[] {
				new FritzboxAuthInterceptor(username, pw),
				new SoapLoggerInterceptor()
		});
		
		return template;
	}
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Setzt den Pfad zu deinen generierten JAXB-Klassen
        marshaller.setContextPath("de.qwyt.housecontrol.tyche.model.soap.fritzbox.tr064");
        return marshaller;
	}
	
	/*
	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSender() {
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(this.getFritzboxCredentialsProvider()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory()
		
		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
		messageSender.setHttpClient(httpClient);
		return messageSender;
	}
	
	private CredentialsProvider getFritzboxCredentialsProvider() {
		BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, pw.toCharArray());
		
		final AuthScope authScope = new AuthScope(new HttpHost("https", "fritz.box", 49443));
		credentialsProvider.setCredentials(authScope, credentials);
		
		return credentialsProvider;
	}
	*/
}
