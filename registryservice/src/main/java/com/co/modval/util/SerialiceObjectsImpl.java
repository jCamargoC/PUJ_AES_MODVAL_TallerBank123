package com.co.modval.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.co.modval.entities.Convenio;

public class SerialiceObjectsImpl {
	
	private static List lstConvenio;
	private final static String RUTA = "/convenios.serv";
	
	public static void save2disk(Convenio obj) {
		if(lstConvenio==null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try{
		    fout = new FileOutputStream(RUTA, true);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(obj);
		    lstConvenio.add(obj);
		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    if(oos  != null){
		        try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public static void load2disk() {
		if(lstConvenio==null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		ObjectInputStream objectinputstream = null;
		try {
		    FileInputStream streamIn = new FileInputStream(RUTA);
		    objectinputstream = new ObjectInputStream(streamIn);
		    Object readCase = null;
		    do {
		        readCase = (Convenio) objectinputstream.readObject();
		        if(readCase != null){
		            lstConvenio.add(readCase);
		        } 
		    } while (readCase != null);   
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    if(objectinputstream != null){
		        try {
					objectinputstream .close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } 
		}
	}

	public static List getLstConvenio() {
		if(lstConvenio==null) {
			lstConvenio = new ArrayList<Convenio>();
		}
		return lstConvenio;
	}	
	
	public static Convenio getConvenioByIdConvenio(Integer idConvenio) { System.out.println("quinto ");
		if(lstConvenio==null) {
			lstConvenio = new ArrayList<Convenio>();System.out.println("-------------");
		}System.out.println("quinto 2");
		for(Convenio obj : (ArrayList<Convenio>) lstConvenio) {System.out.println("quinto 3>>"+obj.getIdConvenio()+"----"+idConvenio);
			if(obj.getIdConvenio().toString().equals(idConvenio.toString())) {
				System.out.println("encontro convenio");
				return obj;
			}
		}
		return new Convenio();
	}
	
}
