package com.co.modval.entities;


import java.io.Serializable;

public class RESTData implements Serializable{
	private String metodo;
	private FuncionEnum funcion;
	private String recurso;	
	private String paramsMapping;
	private String payloadMapping;
	private String headers;
	private String responseData;
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public FuncionEnum getFuncion() {
		return funcion;
	}
	public void setFuncion(FuncionEnum funcion) {
		this.funcion = funcion;
	}
	public String getRecurso() {
		return recurso;
	}
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	public String getParamsMapping() {
		return paramsMapping;
	}
	public void setParamsMapping(String paramsMapping) {
		this.paramsMapping = paramsMapping;
	}
	public String getPayloadMapping() {
		return payloadMapping;
	}
	public void setPayloadMapping(String payloadMapping) {
		this.payloadMapping = payloadMapping;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
	
}
