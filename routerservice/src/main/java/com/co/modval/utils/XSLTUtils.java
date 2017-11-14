package com.co.modval.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.co.modval.entities.Pago;

public class XSLTUtils {
	
	public static String generateFromXSLT(Pago pago, String xsltDefinition)
			throws ParserConfigurationException, SAXException, IOException, TransformerException, JAXBException {

		String xml = convertObjectToXML(pago);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xslt = db.parse(new InputSource(new StringReader(xsltDefinition)));
		Document xmlDoc = db.parse(new InputSource(new StringReader(xml)));
		Document result = transformXML(xmlDoc, xslt);
		result.setXmlStandalone(true);
		LSSerializer serializer = ((DOMImplementationLS) xmlDoc.getImplementation()).createLSSerializer();
		serializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
		return serializer.writeToString(result);
	}

	public static Document transformXML(Document xml, Document xslt)
			throws TransformerException, ParserConfigurationException, FactoryConfigurationError {

		Source xmlSource = new DOMSource(xml);
		Source xsltSource = new DOMSource(xslt);
		DOMResult result = new DOMResult();

		// the factory pattern supports different XSLT processors
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);
		trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		trans.transform(xmlSource, result);

		Document resultDoc = (Document) result.getNode();

		return resultDoc;
	}

	private static String convertObjectToXML(Pago pago) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Pago.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter s = new StringWriter();
		jaxbMarshaller.marshal(pago, s);
		jaxbMarshaller.marshal(pago, System.out);

		return s.toString();
	}
}
