package com.example.demo;

import javax.ws.rs.core.Response;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicedemo/api")
public class ServiceDemoImpl {
	
	@RequestMapping(path = "hola/{nombre}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Response getInfoFactura(@PathVariable("nombre") String nombre) {
		return Response.status(200).entity("Hola :: "+nombre).build();
	}

}
