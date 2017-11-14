package com.co.app.pagoservicios;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import com.co.app.pagoservicios.entities.Convenio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@ViewScoped
@ManagedBean(name="registrarConvenioBean")
public class RegistrarConvenioBean extends ControllerBean {

//	http://localhost:9093/registry/api/consume/registrarconvenio
	/* configuracion conexion ws */
	private static final String TS_SERVER = "registryservice";
    private static final int TS_PORT = 9998;
    private static final String TS_CAPACITY = "registry/api/consume/registrarconvenio";
    private static String TS_URL = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT, TS_CAPACITY);
    
    /* para el contrato del convenio */
	private Long id;
	private Integer idConvenio;
	private String nombre;
	private String host;
	private Integer puerto;
	private String urlServicio;
	private String metodo;
	private String tipo;
	private String mapeo;
	
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
	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getMapeo() {
		return mapeo;
	}
	public void setMapeo(String mapeo) {
		this.mapeo = mapeo;
	}
	
	public void btnRegistrarConvenio(ActionEvent event) {
		try {
			Convenio convenioSave = new Convenio();
			convenioSave.setIdConvenio(idConvenio);
			convenioSave.setNombre(nombre);
			convenioSave.setHost(host);
			convenioSave.setPuerto(puerto);
			convenioSave.setUrlServicio(urlServicio);
			convenioSave.setTipo(tipo);
			convenioSave.setMetodo(metodo);
			convenioSave.setMapeo(mapeo);		
			
			ObjectMapper mapper = new ObjectMapper();
			Client client = Client.create();
			
			WebResource webResource = client.resource(TS_URL);
			ClientResponse response = webResource
			    .type("application/json")
			    .put(ClientResponse.class, mapper.writeValueAsString(convenioSave));
			if (response.getStatus() == 400) {
				addError(response.getEntity(String.class));
				System.out.println(response.getEntity(String.class));
			}else if(response.getStatus() != 200) {
				addError("Servicio no responde :: "+response.getEntity(String.class));
			}else {
				addInfo("Convenio Almacenado con éxito!");
				System.out.println(response.getEntity(String.class));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
