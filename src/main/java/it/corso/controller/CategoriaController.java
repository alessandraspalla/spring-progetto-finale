package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;
import it.corso.service.CategoriaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
// deve essere ADMIN
@Path("/category")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;
	
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategories(@QueryParam("filter") String filter) {
	    try {
	        List<CategoriaDto> listaCategorie = categoriaService.getCategories(filter);
	        return Response.status(Response.Status.OK).entity(listaCategorie).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoryById(@PathParam("id") int id) {
		try {
            CategoriaDto categoriaDto = categoriaService.getCategoryById(id);
            
            if (categoriaDto != null) {
                return Response.status(Response.Status.OK).entity(categoriaDto).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
	}
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creaCategoria(@RequestBody CategoriaCreazioneDto categoriaCreazioneDto) {
        try {
            categoriaService.createCategoria(categoriaCreazioneDto);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
