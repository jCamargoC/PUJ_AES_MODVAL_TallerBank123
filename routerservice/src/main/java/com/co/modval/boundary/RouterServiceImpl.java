package com.co.modval.boundary;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.co.modval.entities.Convenio;
import com.co.modval.entities.FuncionEnum;
import com.co.modval.entities.Pago;
import com.co.modval.entities.RESTData;
import com.co.modval.entities.SOAPData;
import com.co.modval.utils.RESTInvoker;
import com.co.modval.utils.SOAPInvoker;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import entities.ServiceProvider;

@RestController
@RequestMapping("router/api/consume")
public class RouterServiceImpl {

	/* configuracion conexion ws RegistryService */
	private static final String TS_SERVER = "registryservice";
	private static final int TS_PORT = 9998;
	private static final String TS_CAPACITY = "/registry/api/consume/getConvenio/";
	private static String TS_URL = String.format("http://%s:%d/%s", TS_SERVER, TS_PORT, TS_CAPACITY);

	private static final String HEADERS_JSON = "Accept:application/json,Content-Type:application/json; charset=utf8";

	@RequestMapping(path = "obtenerFactura/{idFactura}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Pago getInfoFactura(@PathVariable("idFactura") String idFactura) {
		/*
		 * tomamos los 4 primeros digitos de la factura para buscarlo en el listado de
		 * convenios
		 */
		Long llaveConvenio = Long.parseLong(idFactura.substring(0, 4));
		/* valida la existencia del convenio */
		Convenio convenio = validarExistenciaConvenio(llaveConvenio);
		if (convenio != null && convenio.getId() != null) {
			/*
			 * aca debe consumir el convenio y obetener el dato de la factura y de volverlo
			 * como el objeto Pago
			 */
			Pago response = consumirWSConvenioConsultar(convenio, llaveConvenio, idFactura, llaveConvenio+"-"+convenio.getNombre());
			/* retorna el pago con la informacion de la factura */
			return response;
		}
		return new Pago();
	}
	
	@RequestMapping(path = "pagarFactura/{idFactura}/{valorPago}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public String doPagarFactura(@PathVariable("idFactura") String idFactura, @PathVariable("valorPago") Double valorPago) {
		/*
		 * tomamos los 4 primeros digitos de la factura para buscarlo en el listado de
		 * convenios
		 */
		Long llaveConvenio = Long.parseLong(idFactura.substring(0, 4));
		/* valida la existencia del convenio */
		Convenio convenio = validarExistenciaConvenio(llaveConvenio);
		if (convenio != null && convenio.getId() != null) { 
			/*
			 * aca debe consumir el convenio y obetener el dato de la factura y de volverlo
			 * como el objeto Pago
			 */
			String respuesta = consumirWSConvenio(convenio, llaveConvenio, idFactura, valorPago, FuncionEnum.PAGAR);
			System.out.println(" pagar>>>>>>>>>>>>>>>>< "+respuesta);
			return respuesta;
		}
		return "-1";
	}
	
	@RequestMapping(path = "compensarFactura/{idFactura}/{valorPago}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public String doCompensarFactura(@PathVariable("idFactura") String idFactura, @PathVariable("valorPago") Double valorPago) {
		/*
		 * tomamos los 4 primeros digitos de la factura para buscarlo en el listado de
		 * convenios
		 */
		Long llaveConvenio = Long.parseLong(idFactura.substring(0, 4));
		/* valida la existencia del convenio */
		Convenio convenio = validarExistenciaConvenio(llaveConvenio);
		if (convenio != null && convenio.getId() != null) {
			/*
			 * aca debe consumir el convenio y obetener el dato de la factura y de volverlo
			 * como el objeto Pago
			 */
			String respuesta = consumirWSConvenio(convenio, llaveConvenio, idFactura, valorPago, FuncionEnum.COMPENSAR);
			System.out.println(" compensar>>>>>>>>>>>>>>>>< "+respuesta);
			return respuesta;
		}
		return "-1";
	}

	public Convenio validarExistenciaConvenio(Long idConvenio) {
		Client client = Client.create();
		Convenio convenioExiste = client.resource(TS_URL + idConvenio)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).get(Convenio.class);
		return convenioExiste;
	}

	public String consumirWSConvenio(Convenio convenio, Long idConvenio, String idFactura, Double valorPago, FuncionEnum opcion) {
		/* objeto de consulta */
		Pago pago = new Pago();
		pago.setNumeroConvenio(idConvenio);
		pago.setNumeroFactura(Long.parseLong(idFactura));
		pago.setTotalPago(valorPago);
		String endpointUrl = String.format("http://%s:%d/%s", convenio.getHost(), convenio.getPuerto(),
				convenio.getUrlServicio());
		/* determian el tipo de servicio a consumir - REST/SOAP */
		if (convenio.getTipo().equals("REST")) {
			RESTData serviceData = convenio.getRESTDataByFuncion(opcion);
			System.out.println(endpointUrl + "/" + serviceData.getRecurso() + "----" + idFactura +"----$--"+valorPago);
			System.out.println("---------A > "+serviceData.getMetodo());
			return (String) RESTInvoker.invokeService(endpointUrl + "/" + serviceData.getRecurso(),
					serviceData.getMetodo(), serviceData.getParamsMapping(), serviceData.getHeaders(),
					serviceData.getAccept(),
					serviceData.getPayloadMapping(), serviceData.getResponseData(), pago);
		} else {
			SOAPData serviceData = convenio.getSOAPDataByFuncion(opcion);
			return (String) SOAPInvoker.invokeService(endpointUrl, serviceData.getSoapAction(),
					serviceData.getXsltDefinition(), serviceData.getResponseElement(), pago);
		}
	}

	public Pago consumirWSConvenioConsultar(Convenio convenio, Long idConvenio, String idFactura, String opcion) {
		/* objeto de consulta */
		Pago pago = new Pago();
		pago.setNumeroConvenio(idConvenio);
		pago.setNumeroFactura(Long.parseLong(idFactura));
		pago.setNumeroIdCliente(opcion);
		String endpointUrl = String.format("http://%s:%d/%s", convenio.getHost(), convenio.getPuerto(),
				convenio.getUrlServicio()); 
		/* determian el tipo de servicio a consumir - REST/SOAP */
		if (convenio.getTipo().equals("REST")) {
			RESTData serviceData = convenio.getRESTDataByFuncion(FuncionEnum.CONSULTAR);
			System.out.println(endpointUrl + "/" + serviceData.getRecurso() + "----" + idFactura);
			System.out.println("---------A > "+serviceData.getMetodo());
			
			pago.setTotalPago((Double) RESTInvoker.invokeService(endpointUrl + "/" + serviceData.getRecurso(),
					serviceData.getMetodo(), serviceData.getParamsMapping(), serviceData.getHeaders(),
					serviceData.getAccept(),
					serviceData.getPayloadMapping(), serviceData.getResponseData(), pago));
		} else {
			SOAPData serviceData = convenio.getSOAPDataByFuncion(FuncionEnum.CONSULTAR);
			pago.setTotalPago((Double) SOAPInvoker.invokeService(endpointUrl, serviceData.getSoapAction(),
					serviceData.getXsltDefinition(), serviceData.getResponseElement(), pago));
		}
		/* si el consumir el ws es valido, retorna el valor */
		if (pago.getTotalPago() != null) {
			return pago;
		}
		return new Pago();
	}

}
