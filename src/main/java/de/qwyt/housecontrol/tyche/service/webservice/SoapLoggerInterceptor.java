package de.qwyt.housecontrol.tyche.service.webservice;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;

public class SoapLoggerInterceptor implements ClientInterceptor {

    @Override
    public boolean handleRequest(MessageContext messageContext) {
        logSoapMessage(messageContext.getRequest());
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        logSoapMessage(messageContext.getResponse());
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) {
        logSoapMessage(messageContext.getResponse());
        return true;
    }

    private void logSoapMessage(WebServiceMessage webServiceMessage) {
        try {
            System.out.println("SOAP Message:");
            webServiceMessage.writeTo(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
		// TODO Auto-generated method stub
		
	}

}