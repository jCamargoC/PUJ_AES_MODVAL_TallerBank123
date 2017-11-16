package com.co.modval.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.co.modval.entities.Convenio;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerialiceObjectsImpl {

	private static List lstConvenio;
	private final static String RUTA = "/convenios.serv";

	public static void save2disk(Convenio obj) {
		if (lstConvenio == null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(RUTA, true);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(obj);
			lstConvenio.add(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void load2disk() {
		if (lstConvenio == null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream(RUTA);
			objectinputstream = new ObjectInputStream(streamIn);
			Object readCase = null;
			do {
				readCase = (Convenio) objectinputstream.readObject();
				if (readCase != null) {
					lstConvenio.add(readCase);
				}
			} while (readCase != null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectinputstream != null) {
				try {
					objectinputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List getLstConvenio() {
		if (lstConvenio == null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		return lstConvenio;
	}

	public static Convenio getConvenioByIdConvenio(Integer idConvenio) {
		if (lstConvenio == null) {
			lstConvenio = new ArrayList<Convenio>();
			try {
				ObjectMapper mapper = new ObjectMapper();
				StringBuilder primero = new StringBuilder();
				primero.append(
						"{\"id\":1,\"idConvenio\":1234,\"nombre\":\"colpatria\",\"host\":\"10.0.2.15\",\"puerto\":9997,\"urlServicio\":\"/servicios/pagos/v1\",\"tipo\":\"REST\",\"restServicesData\":[{\"metodo\":\"GET\",\"funcion\":\"CONSULTAR\",\"recurso\":\"payments/{idFactura}\",\"paramsMapping\":\"idFactura:numeroFactura\",\"payloadMapping\":\"valorFactura:totalPago\",\"headers\":\"\",\"responseData\":\"valorFactura\",\"accept\":\"application/json\"},{\"metodo\":\"POST\",\"funcion\":\"PAGAR\",\"recurso\":\"payments/{idFactura}\",\"paramsMapping\":\"idFactura:numeroFactura\",\"payloadMapping\":\"valorFactura:totalPago\",\"headers\":\"Accept:application/json,Content-Type:application/json; charset=utf8\",\"responseData\":\"mensaje\",\"accept\":\"application/json\"},{\"metodo\":\"DELETE\",\"funcion\":\"COMPENSAR\",\"recurso\":\"payments/{idFactura}\",\"paramsMapping\":\"idFactura:numeroFactura\",\"payloadMapping\":\"valorFactura:totalPago\",\"headers\":\"Accept:application/json,Content-Type:application/json; charset=utf8\",\"responseData\":\"mensaje\",\"accept\":\"application/json\"}],\"soapServicesData\":[]}");
				Convenio convenioPrimero = mapper.readValue(primero.toString(), Convenio.class);
				lstConvenio.add(convenioPrimero);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Convenio obj : (ArrayList<Convenio>) lstConvenio) {
			if (obj.getIdConvenio().toString().equals(idConvenio.toString())) {
				System.out.println("encontro convenio");
				return obj;
			}
		}
		return new Convenio();
	}

}
