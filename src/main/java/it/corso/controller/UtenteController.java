package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;

import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import it.corso.service.Blacklist;
import it.corso.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UtenteController {
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private Blacklist blacklist;
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response userRegistration(@Valid @RequestBody UtenteRegistrazioneDto utenteDto) {
		
		try {
			
			if(!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}", utenteDto.getPassword())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			
			if(utenteService.existsByEmail(utenteDto.getEmail())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

			utenteService.userRegistration(utenteDto);
			
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
		
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@DELETE
    @Path("/{email}")
    public Response deleteUserByEmail(@PathParam("email") String email) {
		
        try {
        	
            utenteService.deleteByEmail(email);
            return Response.status(Response.Status.OK).build();
            
        } catch (Exception e) {
        	
            return Response.status(Response.Status.BAD_REQUEST).build();
            
        }
    }
	
	@GET
	@Path("/getByEmail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@QueryParam("email") String email) {
		
		try {
			
			if(email != null && !email.isEmpty()) {
				UtenteDto utenteDto = utenteService.getUserByEmail(email);
				if(utenteDto != null) {
					return Response.status(Response.Status.OK).entity(utenteDto).build();
				}
			}
			
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.status(Response.Status.BAD_REQUEST).build();
		
	}
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@RequestBody UtenteDtoAggiornamento utenteDtoAgg) {
		
		try {
			
			if(!utenteService.existsByEmail(utenteDtoAgg.getEmail())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

			utenteService.updateUser(utenteDtoAgg);
			
			return Response.status(Response.Status.OK).build();
			
		} catch (Exception e) {
		
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		
		try {
			
			return Response.status(Response.Status.OK).entity(utenteService.getAllUsers()).build();
			
		} catch (Exception e) {
			
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(@RequestBody UtenteLoginRequestDto utenteLoginRequestDto) {
		
		try {
			
			if(utenteService.loginUser(utenteLoginRequestDto)) {
				return Response.ok(issueToken(utenteLoginRequestDto.getEmail())).build(); 
			}
			
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	}
	
//	@GET
//	@Path("/logout")
//	public Response logoutUtente(ContainerRequestContext containerRequestToken) {
//		
//		try {
//			
//			String authorizationHeader = containerRequestToken.getHeaderString(HttpHeaders.AUTHORIZATION);
//			String token = authorizationHeader.substring("Bearer".length()).trim();
//			
//			blacklist.invalidateToken(token);
//		
//			return Response.status(Response.Status.OK).build();
//		} catch (Exception e) {
//			
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//		}
//		
//	}
	
	private UtenteLoginResponseDto issueToken(String email) {
		// eseguiamo una crifratura attraverso l'algoritmo di crittografia HMAC
		byte[] secretKey = "wefcheut495tty3498cry4qr908302qr9u3409tyctc3wv5y4".getBytes(); 
		
		Key key = Keys.hmacShaKeyFor(secretKey);
		
		Utente utente = utenteService.findByEmail(email);
		
		Map<String, Object> map = new HashMap<>();
		map.put("nome", utente.getNome());
		map.put("cognome", utente.getCognome());
		map.put("email", email);
		
		List<String> ruoli = new ArrayList<>();
		for(Ruolo ruolo : utente.getRuoli()) {
			ruoli.add(ruolo.getTipologia().name());
		}
		
		map.put("ruoli", ruoli);
		// ttl -> time to live(?)
		Date creationDate = new Date();
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		
		// creazione del token firmato
		String jwtToken = Jwts.builder()
			.setClaims(map)
			.setIssuer("http://localhost:8080")
			.setIssuedAt(creationDate)
			.setExpiration(end)
			.signWith(key)
			.compact();
		
		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);
		token.setTokenCreationTime(creationDate);
		token.setTtl(end);
		
		return token;
	}
}

// .name nella classe enum torna il nome dell'enum come string
// con spring MVC si utilizza ResponseEntity<T> e non Response