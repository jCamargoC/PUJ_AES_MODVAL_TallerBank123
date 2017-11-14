package com.co.modval.boundary;

import javax.ws.rs.core.Response;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.co.modval.entities.Convenio;
import com.co.modval.util.SerialiceObjectsImpl;

@RestController
@RequestMapping("registry/api/consume")
public class RegistryServiceImpl {
	
	@RequestMapping(path = "hola/{nombre}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Response hola(@PathVariable("nombre") String idFactura) {
		return Response.status(200).entity("hola").build();
	}
	
	@RequestMapping(path = "getConvenio/{idConvenio}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Convenio getConvenioByIdConvenio(@PathVariable("idConvenio") Integer idConvenio) {
		System.out.println("cuarto >>>>>>>>>>>> convenio" + idConvenio);
		return SerialiceObjectsImpl.getConvenioByIdConvenio(idConvenio);
	}
	
	@RequestMapping(path = "registrarconvenio", method = RequestMethod.PUT)
    public Response registrarConvenio(@RequestBody Convenio convenio) {
		if(convenio!=null && convenio.getIdConvenio()!=null) {	
			convenio.setId((long) (SerialiceObjectsImpl.getLstConvenio().size() + 1));			
			SerialiceObjectsImpl.save2disk(convenio);
			return Response.status(200).entity(convenio.getIdConvenio()).build();		
		}
		return Response.status(404).entity("pailas no llego!").build();	
	}

}
