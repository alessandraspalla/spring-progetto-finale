package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.corso.dto.CorsoDto;
import it.corso.jwt.JWTTokenNeeded;
import it.corso.jwt.Secured;
import it.corso.service.CorsoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Secured(role = "Admin")
@JWTTokenNeeded
@Path("/corso")
public class CorsoController {
	@Autowired
	private CorsoService corsoService;

	@GET
	@Path("/corsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourses() {

		try {

			List<CorsoDto> listaCorsi = corsoService.getCourses();
			return Response.status(Response.Status.OK).entity(listaCorsi).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).entity("Errore caricamento utenti").build();
		}
	}
}
