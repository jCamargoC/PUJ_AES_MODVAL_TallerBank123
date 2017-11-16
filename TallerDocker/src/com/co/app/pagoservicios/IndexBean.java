package com.co.app.pagoservicios;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.co.app.pagoservicios.entities.Pago;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@SessionScoped
@ManagedBean(name = "indexBean")
public class IndexBean extends ControllerBean implements Serializable {

	/* configuracion conexion ws */
	private static final String TS_SERVER = "routerservice";
	private static final int TS_PORT = 9999;
	private static final String TS_CAPACITY = "router/api/consume/";
	private static String TS_URL_CONSULTAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT,
			TS_CAPACITY + "obtenerFactura/");
	private static String TS_URL_PAGAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT,
			TS_CAPACITY + "pagarFactura/");
	private static String TS_URL_COMPENSAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT,
			TS_CAPACITY + "compensarFactura/");

	private String codigoPago;
	private Boolean rndPanelPago = false;
	private Double valorFactura;
	private String nombreConvenio;

	public String getCodigoPago() {
		return codigoPago;
	}

	public void setCodigoPago(String codigoPago) {
		this.codigoPago = codigoPago;
	}

	public Boolean getRndPanelPago() {
		return rndPanelPago;
	}

	public void setRndPanelPago(Boolean rndPanelPago) {
		this.rndPanelPago = rndPanelPago;
	}

	public Double getValorFactura() {
		return valorFactura;
	}

	public void setValorFactura(Double valorFactura) {
		this.valorFactura = valorFactura;
	}

	public String getNombreConvenio() {
		return nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * consulta la informacion de la factura, consume el sservicio del router
	 * findInfoFactura
	 * 
	 * @param event
	 */
	public void btnConsultarPago(ActionEvent event) {
		rndPanelPago = false;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Client client = Client.create();
			WebResource webResource = client.resource(TS_URL_CONSULTAR + Integer.parseInt(codigoPago));
			ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
			Pago pagoRes = mapper.readValue(response.getEntity(String.class), Pago.class);
			System.out.println("--responde consultar-------------------->");
			if (pagoRes != null && pagoRes.getNumeroConvenio() != null) {
				valorFactura = pagoRes.getTotalPago();
				nombreConvenio = pagoRes.getNumeroIdCliente();
				rndPanelPago = true;
			} else {
				addError("No existe convenio de pago para la factura asociada");
			}
		} catch (Exception e) {
			e.printStackTrace();
			addError("Se han presentado problemas en la comunicación, por favor intente mas tarde");

		}
	}

	/**
	 * realiza el pago de la factura, consume el servicio del router pagarFactura
	 * 
	 * @param event
	 */
	public void btnRealizarPago(ActionEvent event) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Client client = Client.create();System.out.println("consultar factura ");
			WebResource webResource = client.resource(TS_URL_PAGAR + Integer.parseInt(codigoPago)+"/"+valorFactura);
			ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
			String msgRes = response.getEntity(String.class);
			System.out.println("--respuesta pagar----------------------------->"+msgRes);
			if (msgRes != null && !msgRes.equals("-1")) {
				addInfo("Pago realizado con éxito! " + msgRes);
			} else {
				/* compensar */
				if (compensar(event)) {
					addError(
							"No se ha posido realizar el pago, su saldo no ha sido afectado, por favor intente mas tarde.");
				}
			}
		} catch (Exception e) {
			/* compensar */
			compensar(event);
		}
	}

	/**
	 *  realiza la compensacion de la factura, consume el servicio del roueter compensarFactura
	 * @return
	 */
	public Boolean compensar(ActionEvent event) {
		try {	
			ObjectMapper mapper = new ObjectMapper();
			Client client = Client.create();System.out.println("compensar pago");
			WebResource webResource = client.resource(TS_URL_COMPENSAR + Integer.parseInt(codigoPago)+"/"+valorFactura);
			ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
			String msgRes = response.getEntity(String.class);
			System.out.println("--respuesta compensar----------------------------->"+msgRes);
			if (msgRes != null && !msgRes.equals("-1")) {
				return true;
			} else {
				addError("Se han presentado problemas en la comunicación, por favor intente mas tarde");
				return false;
			}			
		}catch(Exception e) {e.printStackTrace();
			/* compensar fallido */
			addError("No se ha podido compensar el pago, favor comuniquese con la entidad!");		
		}
		return false;
	}
	
	public String btnLimpiar() {
		codigoPago = null;
		rndPanelPago = false;
		valorFactura = null;
		nombreConvenio = null;
		return "index";
	}
}
