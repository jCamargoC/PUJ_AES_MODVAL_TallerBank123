package entities;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider implements Serializable, Cloneable{

	public Map<String, Object> fields = new HashMap<String, Object>();

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
	
}
