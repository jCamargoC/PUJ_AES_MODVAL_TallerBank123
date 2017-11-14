package com.co.app.pagoservicios.entities;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Pago {
	private Long numeroFactura;
	
	private Long numeroConvenio;
	
	private String numeroIdCliente;
	
	private String tipoIdCliente;
	
	private Double totalPago;

	public Long getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(Long numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public Long getNumeroConvenio() {
		return numeroConvenio;
	}

	public void setNumeroConvenio(Long numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}

	public String getNumeroIdCliente() {
		return numeroIdCliente;
	}

	public void setNumeroIdCliente(String numeroIdCliente) {
		this.numeroIdCliente = numeroIdCliente;
	}

	public String getTipoIdCliente() {
		return tipoIdCliente;
	}

	public void setTipoIdCliente(String tipoIdCliente) {
		this.tipoIdCliente = tipoIdCliente;
	}

	public Double getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}
	
}
