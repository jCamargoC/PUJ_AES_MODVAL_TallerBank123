package com.co.app.pagoservicios;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.co.app.pagoservicios.entities.Pago;
import com.sun.jersey.api.client.Client;

@ViewScoped
@ManagedBean(name = "indexBean")
public class IndexBean extends ControllerBean implements Serializable {

	/* configuracion conexion ws */
	private static final String TS_SERVER = "routerservice";
	private static final int TS_PORT = 9999;
	private static final String TS_CAPACITY = "router/api/consume/";
	private static String TS_URL_CONSULTAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT, TS_CAPACITY + "obtenerFactura/");
	private static String TS_URL_PAGAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT, TS_CAPACITY + "pagarFactura/");
	private static String TS_URL_COMPENSAR = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT, TS_CAPACITY + "compensarFactura/");

	
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
	 * consulta la informacion de la factura, consume el sservicio del router findInfoFactura
	 * @param event
	 */
	public void btnConsultarPago(ActionEvent event) {
		rndPanelPago = false; System.out.println("va a consyltars");
		try {
			Client client = Client.create();
			Pago response = client.resource(TS_URL_CONSULTAR+Integer.parseInt(codigoPago)).
					header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
					.get(Pago.class);
			if(response!=null && response.getNumeroConvenio()!=null) {
				valorFactura = response.getTotalPago();
				nombreConvenio = response.getNumeroConvenio().toString();
				rndPanelPago = true;
			}else {
				addError("No existe convenio de pago para la factura asociada");
			}
		}catch(Exception e) {
			e.printStackTrace();
			addError("Se han presentado problemas en la comunicación, por favor intente mas tarde");
			
		}		
	}
	
	/**
	 * realiza el pago de la factura, consume el servicio del router pagarFactura
	 * @param event
	 */
	public void btnRealizarPago(ActionEvent event) {System.out.println("..................");
		try {			
			Client client = Client.create();
			Pago response = client.resource(TS_URL_PAGAR+Integer.parseInt(codigoPago)).
					header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).get(Pago.class);
			if(response!=null && response.getNumeroIdCliente()!=null) {
				addInfo("Pago realizado con éxito! "+response.getNumeroIdCliente());
			}else {
				/* compensar */
				if(compensar()) {
					addError("No se ha posido realizar el pago, su saldo no ha sido afectado, por favor intente mas tarde.");
				}
			}
			addInfo("hola");
		}catch(Exception e) {
			/* compensar */
			compensar();			
		}
	}
	
	/**
	 *  realiza la compensacion de la factura, consume el servicio del roueter compensarFactura
	 * @return
	 */
	public Boolean compensar() {
		Client client = Client.create();
		Pago response = client.resource(TS_URL_PAGAR+Integer.parseInt(codigoPago)).
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).get(Pago.class);
		if(response!=null && response.getNumeroIdCliente()!=null) {
			return true;
		}else {
			addError("Se han presentado problemas en la comunicación, por favor intente mas tarde");
		}
		return false;
	}
}
