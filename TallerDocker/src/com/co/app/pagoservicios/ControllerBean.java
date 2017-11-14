package com.co.app.pagoservicios;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;

public class ControllerBean {
	
	/* para los mensajes */		
	public void addInfo(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Pago de Servicios", mensaje);         
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }   
  
    public void addError(String mensaje) {    
    	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pago de Servicios", mensaje);         
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }  

}
