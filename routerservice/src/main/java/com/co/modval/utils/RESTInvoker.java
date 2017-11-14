package com.co.modval.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.co.modval.entities.Pago;


public class RESTInvoker {
	
	
	public static Object invokeService(String serviceURL, String method, String paramsMapping, String headers,
			String payloadMapping, String responseData, Pago pago) {
		String[] completed = completeService(serviceURL, paramsMapping, payloadMapping, pago);
		serviceURL = completed[0];
		String payload = completed[1];
		return invoke(serviceURL, method, headers, payload, responseData);
	}

	private static Object invoke(String serviceURL, String method, String headers, String payload,
			String responseData) {
		try {

			URL url = new URL(serviceURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			if (headers != null && !headers.equals("")) {

				String[] splitted = headers.split(",");
				for (String string : splitted) {
					String[] field = string.split(":");

					conn.setRequestProperty(field[0], field[1]);
				}

			}
			if (payload != null && !payload.equals("")) {
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(payload.getBytes());
				os.flush();
			}

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String returnData="";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				returnData+=output;
			}
			JSONObject jsonObject=new JSONObject(returnData);
			conn.disconnect();
			return jsonObject.get(responseData);
		} catch (

		MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {
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
				String string=splitted[i];
				String[] field = string.split(":");
				Object value = getAttributeFromPago(pago, field[1]);
				if (value instanceof String) {
					payload += "\"" + field[0] + "\":\"" + value.toString() + "\""+(i==splitted.length-1?"":",");
				} else if (value instanceof Number || value instanceof Boolean) {
					payload += "\"" + field[0] + "\":" + value.toString() +(i==splitted.length-1?"":",");
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
