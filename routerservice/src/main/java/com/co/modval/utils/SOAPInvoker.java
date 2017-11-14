package com.co.modval.utils;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;

import com.co.modval.entities.Pago;

public class SOAPInvoker {

	public static Object invokeService(String endpointURL, String soapAction, String xsltDef, String responseElement,Pago pago) {
		try {
			String envelope=XSLTUtils.generateFromXSLT(pago, xsltDef);
			
			Document serviceResponse=callSoapWebService(endpointURL, soapAction, envelope);
			return serviceResponse.getElementsByTagName(responseElement).item(0).getFirstChild().getTextContent();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return null;
	}

	private static Document callSoapWebService(String soapEndpointUrl, String soapAction, String envelope) {
		try {
			// Create SOAP Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			
			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, envelope), soapEndpointUrl);

			// Print the SOAP Response
			System.out.println("Response SOAP Message:");
			soapResponse.writeTo(System.out);
			System.out.println();

			soapConnection.close();
			return toDocument(soapResponse);
		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
		return null;
	}

	private static Document toDocument(SOAPMessage soapMsg)
			throws TransformerConfigurationException, TransformerException, SOAPException, IOException {
		Source src = soapMsg.getSOAPPart().getContent();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMResult result = new DOMResult();
		transformer.transform(src, result);
		return (Document) result.getNode();
	}

	private static SOAPMessage createSOAPRequest(String soapAction, String body) throws Exception {
		MessageFactory messageFactory = MessageFactory.newInstance();
		InputStream is = new ByteArrayInputStream(body.getBytes());
		SOAPMessage soapMessage = messageFactory.createMessage(null, is);

		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", soapAction);

		soapMessage.saveChanges();

		/* Print the request message, just for debugging purposes */
		System.out.println("Request SOAP Message:");
		soapMessage.writeTo(System.out);
		System.out.println("\n");

		return soapMessage;
	}

}
