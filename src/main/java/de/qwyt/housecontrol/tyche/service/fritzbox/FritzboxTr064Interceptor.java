package de.qwyt.housecontrol.tyche.service.fritzbox;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.headers.Header;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import java.util.Collections;


public class FritzboxTr064Interceptor extends AbstractPhaseInterceptor<Message> {



	public FritzboxTr064Interceptor() {
		super(Phase.PREPARE_SEND);
	}

	/*
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		//SOAPMessage soapMessage = message.getContent(SOAPMessage.class);
		XMLStreamWriter writer = message.getContent(XMLStreamWriter.class);
		
		try {
			SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
			
			// namespaces
			envelope.addNamespaceDeclaration("s", "http://schemas.xmlsoap.org/soap/envelope/");
			envelope.addNamespaceDeclaration("u", "urn:dslforum-org:service:Hosts:1");
			
			// encoding
			envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
			
			// header
			message.put("SOAPAction", "urn:dslforum-org:service:Hosts:1#GetHostNumberOfEntries");
			message.put("Content-Type", "text/xml");
			message.put("charset", "utf-8");
			
			soapMessage.saveChanges();
		} catch (SOAPException e) {
			throw new RuntimeException("Error modifying SOAP envelope", e);
		}
	}
*/

	@Override
	public void handleMessage(Message message) throws Fault {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().newDocument();

            Element initChallengeElem = doc.createElementNS("http://soap-authentication.org/digest/2001/10/", "h:InitChallenge");
            initChallengeElem.setAttribute("s:mustUnderstand", "1");
            Element userIdElem = doc.createElement("UserID");
            userIdElem.setTextContent("tyche");
            initChallengeElem.appendChild(userIdElem);

            Header header = new Header(new QName("http://soap-authentication.org/digest/2001/10/", "InitChallenge"), initChallengeElem);
            List<Header> headers = Collections.singletonList(header);
            message.put(Header.HEADER_LIST, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
}
