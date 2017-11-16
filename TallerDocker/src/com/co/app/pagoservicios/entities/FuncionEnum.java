package com.co.app.pagoservicios.entities;

public enum FuncionEnum {
	CONSULTAR("consultar"),
	PAGAR("pagar"),
	COMPENSAR("compensar");
	
	private String opc;
	
	private FuncionEnum(String opc) {
		this.opc = opc;
	}

	public String getOpc() {
		return opc;
	}

	public void setOpc(String opc) {
		this.opc = opc;
	}
}
