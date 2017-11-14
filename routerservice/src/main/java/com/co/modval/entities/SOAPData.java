package com.co.modval.entities;

import java.io.Serializable;

public class SOAPData implements Serializable{
	
	private String soapAction;
	
	private String xsltDefinition;
	
	private FuncionEnum funcion;
	
	private String responseElement;

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getXsltDefinition() {
		return xsltDefinition;
	}

	public void setXsltDefinition(String xsltDefinition) {
		this.xsltDefinition = xsltDefinition;
	}

	public FuncionEnum getFuncion() {
		return funcion;
	}

	public void setFuncion(FuncionEnum funcion) {
		this.funcion = funcion;
	}

	public String getResponseElement() {
		return responseElement;
	}

	public void setResponseElement(String responseElement) {
		this.responseElement = responseElement;
	}
	
	
}
