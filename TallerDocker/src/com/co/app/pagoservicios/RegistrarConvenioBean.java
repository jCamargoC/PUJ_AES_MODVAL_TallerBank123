package com.co.app.pagoservicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import org.json.JSONObject;

import com.co.app.pagoservicios.entities.Convenio;
import com.co.app.pagoservicios.entities.FuncionEnum;
import com.co.app.pagoservicios.entities.RESTData;
import com.co.app.pagoservicios.entities.SOAPData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@SessionScoped
@ManagedBean(name = "registrarConvenioBean")
public class RegistrarConvenioBean extends ControllerBean {

	// http://localhost:9093/registry/api/consume/registrarconvenio
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

	private String tipo;
	private String mapeo;
	/* de los rest / soap data */
	private String headers;
	private String accept;
	private String recurso;
	private String metodo;
	private String paramsMapping;
	private String payloadMapping;
	private String responseData;
	private RESTData objRestConsultar;
	private RESTData objRestPagar;
	private RESTData objRestCompensar;

	private String capacidad;

	private List<RESTData> restServicesData = new ArrayList<RESTData>();;
	private List<SOAPData> soapServicesData = new ArrayList<SOAPData>();
	private List<String> posServicios = new ArrayList<String>();

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

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
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

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
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

	public String getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(String capacidad) {
		this.capacidad = capacidad;
	}

	public void btnRegistrarConvenio(ActionEvent event) {
		try {

			ObjectMapper mapper = new ObjectMapper();

			Convenio convenioSave = new Convenio();
			convenioSave.setIdConvenio(idConvenio);
			convenioSave.setNombre(nombre);
			convenioSave.setHost(host);
			convenioSave.setPuerto(puerto);
			convenioSave.setUrlServicio(urlServicio);
			convenioSave.setTipo(tipo);

			convenioSave.setRestServicesData(restServicesData);
			convenioSave.setSoapServicesData(soapServicesData);

			Client client = Client.create();
			WebResource webResource = client.resource(TS_URL);
			ClientResponse response = webResource.type("application/json").put(ClientResponse.class,
					mapper.writeValueAsString(convenioSave));
			Integer status = (Integer) response.getStatus();
			JSONObject jsonObject = new JSONObject(response.getEntity(String.class));
			switch (status) {
				case 200:
					if((Integer) jsonObject.get("entity") == -1) {
						addInfo("Convenio [" + idConvenio + "-" + nombre + "] ya existe!");
					}else {
						addInfo("Convenio [" + idConvenio + "-" + nombre + "] Almacenado con éxito!");
					}
					break;
				default:
					addError("Servicio no responde :: " + jsonObject.toString());
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void btnAgregarCapacidad(ActionEvent event) {
		if (tipo.equals("REST")) {
			if (capacidad.equals("CONSULTAR")) {
				objRestConsultar = new RESTData();
				objRestConsultar.setHeaders(headers);
				objRestConsultar.setAccept(accept);
				objRestConsultar.setMetodo(metodo);
				objRestConsultar.setParamsMapping(paramsMapping);
				objRestConsultar.setPayloadMapping(payloadMapping);
				objRestConsultar.setRecurso(recurso);
				objRestConsultar.setResponseData(responseData);
				objRestConsultar.setFuncion(FuncionEnum.CONSULTAR);
				if (posServicios.contains(FuncionEnum.CONSULTAR.getOpc())) {
					this.restServicesData.set(posServicios.indexOf(FuncionEnum.CONSULTAR.getOpc()), objRestConsultar);
				} else {
					this.restServicesData.add(objRestConsultar);
					posServicios.add(FuncionEnum.CONSULTAR.getOpc());
				}
			} else if (capacidad.equals("PAGAR")) {
				objRestPagar = new RESTData();
				objRestPagar.setHeaders(headers);
				objRestPagar.setAccept(accept);
				objRestPagar.setMetodo(metodo);
				objRestPagar.setParamsMapping(paramsMapping);
				objRestPagar.setPayloadMapping(payloadMapping);
				objRestPagar.setRecurso(recurso);
				objRestPagar.setResponseData(responseData);
				objRestPagar.setFuncion(FuncionEnum.PAGAR);
				if (posServicios.contains(FuncionEnum.PAGAR.getOpc())) {
					this.restServicesData.set(posServicios.indexOf(FuncionEnum.PAGAR.getOpc()), objRestPagar);
				} else {
					this.restServicesData.add(objRestPagar);
					posServicios.add(FuncionEnum.PAGAR.getOpc());
				}
			} else {
				objRestCompensar = new RESTData();
				objRestCompensar.setHeaders(headers);
				objRestCompensar.setAccept(accept);
				objRestCompensar.setMetodo(metodo);
				objRestCompensar.setParamsMapping(paramsMapping);
				objRestCompensar.setPayloadMapping(payloadMapping);
				objRestCompensar.setRecurso(recurso);
				objRestCompensar.setResponseData(responseData);
				objRestCompensar.setFuncion(FuncionEnum.COMPENSAR);
				if (posServicios.contains(FuncionEnum.COMPENSAR.getOpc())) {
					this.restServicesData.set(posServicios.indexOf(FuncionEnum.COMPENSAR.getOpc()), objRestCompensar);
				} else {
					this.restServicesData.add(objRestCompensar);
					posServicios.add(FuncionEnum.COMPENSAR.getOpc());
				}
			}
		}
	}

	public String btnLimpiar() {
		id = null;
		idConvenio = null;
		nombre = null;
		host = null;
		puerto = null;
		urlServicio = null;
		tipo = null;
		mapeo = null;
		headers = null;
		accept = null;
		recurso = null;
		metodo = null;
		paramsMapping = null;
		payloadMapping = null;
		responseData = null;
		objRestConsultar = null;
		objRestPagar = null;
		objRestCompensar = null;
		capacidad = null;
		restServicesData = new ArrayList<RESTData>();
		soapServicesData = new ArrayList<SOAPData>();
		posServicios = new ArrayList<String>();
		return "registrarConvenio";
	}

}
