package com.co.modval.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;

import com.co.modval.entities.Pago;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RESTInvoker {

	public static void main(String[] args) {
		String method = "GET";
		String headers = "Accept:application/json,Content-Type:application/json; charset=utf8";
		String paramsMapping = "idFactura:numeroFactura";
		Pago pago = new Pago();
		pago.setNumeroConvenio(35423L);
		pago.setNumeroFactura(1234L);
		pago.setNumeroIdCliente("fsdfsdfds");
		pago.setTipoIdCliente("CC");
		pago.setTotalPago(3434344.4344);
		String payloadMapping = null;
		String responseData = "valorFactura";
		String serviceURL = "http://192.168.0.4:9090/servicios/pagos/v1/payments/{idFactura}";
		String accept = "application/json";
		System.out.println(
				invokeService(serviceURL, method, paramsMapping, headers, accept, payloadMapping, responseData, pago));
	}

	public static Object invokeService(String serviceURL, String method, String paramsMapping, String headers,
			String accept, String payloadMapping, String responseData, Pago pago) {
		String[] completed = completeService(serviceURL, paramsMapping, payloadMapping, pago);
		serviceURL = completed[0];
		String payload = completed[1];
		return invoke(serviceURL, method, headers, accept, payload, responseData);
	}

	private static Object invoke(String serviceURL, String method, String headers, String accept, String payload,
			String responseData) {
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(serviceURL);
			if (headers != null && !headers.equals("")) {
				String[] splitted = headers.split(",");
				for (String string : splitted) {
					String[] field = string.split(":");
					webResource.header(field[0], field[1]);
				}
			}
			ClientResponse response = null;
			if (method.equals("GET")) {
				response = webResource.accept(accept).get(ClientResponse.class);
			} else if (method.equals("POST")) {
				response = webResource.type(accept).post(ClientResponse.class, payload);
			} else if (method.equals("PUT")) {
				response = webResource.type(accept).put(ClientResponse.class, payload);
			} else if (method.equals("DELETE")) {
				response = webResource.accept(accept).delete(ClientResponse.class);
			}
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			JSONObject jsonObject = new JSONObject(response.getEntity(String.class));

			return jsonObject.get(responseData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static String[] completeService(String serviceURL, String paramsMapping, String payloadMapping, Pago pago) {
		String payload = "";
		if (paramsMapping != null && !paramsMapping.equals("")) {
			String[] splitted = paramsMapping.split(",");
			for (String string : splitted) {
				String[] field = string.split(":");
				Object value = getAttributeFromPago(pago, field[1]);
				serviceURL = serviceURL.replace("{" + field[0] + "}", value.toString());
			}
		}
		if (payloadMapping != null && !payloadMapping.equals("")) {
			String[] splitted = payloadMapping.split(",");
			payload = "{";
			for (int i = 0; i < splitted.length; i++) {
				String string = splitted[i];
				String[] field = string.split(":");
				Object value = getAttributeFromPago(pago, field[1]);
				if (value instanceof String) {
					payload += "\"" + field[0] + "\":\"" + value.toString() + "\""
							+ (i == splitted.length - 1 ? "" : ",");
				} else if (value instanceof Number || value instanceof Boolean) {
					payload += "\"" + field[0] + "\":" + value.toString() + (i == splitted.length - 1 ? "" : ",");
				}
			}
			payload += "}";
		}
		return new String[] { serviceURL, payload };
	}

	public static Object getAttributeFromPago(Pago pago, String attribute) {

		try {
			String methodName = "get" + capitalizeAttr(attribute); 
			Method method = pago.getClass().getMethod(methodName); 
			Object value = method.invoke(pago);
			return value;
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static String capitalizeAttr(String attribute) {
		return Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
	}
}
