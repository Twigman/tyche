package de.qwyt.housecontrol.tyche.service.webservice;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.StringSource;

public class FritzboxAuthInterceptor implements ClientInterceptor {

	public final String username;
	
	public final String pw;
	
	public FritzboxAuthInterceptor(String username, String pw) {
		this.username = username;
		this.pw = pw;
	}
	
	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        SoapHeader soapHeader = soapMessage.getSoapHeader();
		

		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
		// TODO Auto-generated method stub
		
	}

}
