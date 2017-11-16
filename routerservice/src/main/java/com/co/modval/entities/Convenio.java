package com.co.modval.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Convenio implements Serializable{

    /* 
     * propiedades basadas en el contrato 
     * api.raml
     * */
	
    private Long id;
	private Integer idConvenio;
	private String nombre;
	private String host;
	private Integer puerto;
	private String urlServicio;	
	private String tipo;	
	private List<RESTData> restServicesData=new ArrayList<RESTData>();
	private List<SOAPData> soapServicesData=new ArrayList<SOAPData>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIdConvenio() {
		return idConvenio;
	}
	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPuerto() {
		return puerto;
	}
	public void setPuerto(Integer puerto) {
		this.puerto = puerto;
	}
	public String getUrlServicio() {
		return urlServicio;
	}
	public void setUrlServicio(String urlServicio) {
		this.urlServicio = urlServicio;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public List<RESTData> getRestServicesData() {
		return restServicesData;
	}
	public void setRestServicesData(List<RESTData> restServicesData) {
		this.restServicesData = restServicesData;
	}
	public List<SOAPData> getSoapServicesData() {
		return soapServicesData;
	}
	public void setSoapServicesData(List<SOAPData> soapServicesData) {
		this.soapServicesData = soapServicesData;
	}
	
	public SOAPData getSOAPDataByFuncion(FuncionEnum funcionEnum) {
		for (SOAPData soapData : soapServicesData) {
			if(funcionEnum.equals(soapData.getFuncion())) {
				return soapData;
			}
		}
		return null;
	}
	
	public RESTData getRESTDataByFuncion(FuncionEnum funcionEnum) {
		for (RESTData restData : restServicesData) {
			if(funcionEnum.getOpc().equals(restData.getFuncion().getOpc())) { System.out.println("hallado convenio!");
				return restData;
			}
		}
		return null;
	}
	
	
}
