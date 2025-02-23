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
		try {
            SaajSoapMessage message = (SaajSoapMessage) messageContext.getRequest();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(message.getPayloadSource(), new StreamResult(writer));
            System.out.println("SOAP Request:\n" + writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        SoapHeader soapHeader = soapMessage.getSoapHeader();
		
        // Auth header
        String headerXml = "<h:InitChallenge xmlns:h=\"http://soap-authentication.org/digest/2001/10/\" xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:mustUnderstand=\"1\">" +
                "<UserID>" + username + "</UserID>" +
                "</h:InitChallenge >";

        try {
        	Transformer transformer = TransformerFactory.newInstance().newTransformer();
        	transformer.transform(new StringSource(headerXml), soapHeader.getResult());
        } catch (Exception e) {
        	throw new WebServiceClientException("Error when adding authentication header", e) {};
        }
        */
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		try {
            SaajSoapMessage message = (SaajSoapMessage) messageContext.getResponse();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(message.getPayloadSource(), new StreamResult(writer));
            System.out.println("SOAP Response:\n" + writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
