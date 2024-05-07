package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteDao utenteDao;
	
	@Autowired
	private RuoloDao ruoloDao;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public void userRegistration(UtenteRegistrazioneDto utenteDto) {
		
		Utente utente = new Utente();
		
		utente.setNome(utenteDto.getNome());
		utente.setCognome(utenteDto.getCognome());
		utente.setEmail(utenteDto.getEmail());
		
		String sha256hex = DigestUtils.sha256Hex(utenteDto.getPassword());
		utente.setPassword(sha256hex);
		
		utenteDao.save(utente);
		
	}

	@Override
	public boolean existsByEmail(String email) {
		
		return utenteDao.existsByEmail(email);
	}

	@Override
	public void deleteByEmail(String email) {
		
		Utente utente = utenteDao.findByEmail(email);
		
		Optional<Utente> utenteOptional = utenteDao.findById(utente.getId());
		
		if(utenteOptional.isPresent()) {
			utenteDao.delete(utenteOptional.get());
		}
    }

	@Override
	public UtenteDto getUserByEmail(String email) {
		
		Utente utente = utenteDao.findByEmail(email);
		return modelMapper.map(utente, UtenteDto.class);
	
	}
	
	@Override
	public void updateUser(UtenteDtoAggiornamento utenteDtoAgg) {
		
		try {
			Utente utenteDb = utenteDao.findByEmail(utenteDtoAgg.getEmail());
			
			if(utenteDb != null) {
				utenteDb.setNome(utenteDtoAgg.getNome());
				utenteDb.setCognome(utenteDtoAgg.getCognome());
				utenteDb.setEmail(utenteDtoAgg.getEmail());
				
				List<Ruolo> ruoliUtente = new ArrayList<>();
				Optional<Ruolo> ruoloDb = ruoloDao.findById(utenteDtoAgg.getIdRuolo());
				
				if(ruoloDb.isPresent()) {
					
					Ruolo ruolo = ruoloDb.get();
					ruolo.setId(utenteDtoAgg.getIdRuolo());
					
					ruoliUtente.add(ruolo);
					utenteDb.setRuoli(ruoliUtente);
				}
				
				utenteDao.save(utenteDb);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public List<UtenteDto> getAllUsers() {

		List<Utente> listaUtenti = (List<Utente>) utenteDao.findAll();
		List<UtenteDto> listaUtentiDto = new ArrayList<>();
		
		listaUtenti.forEach(u -> listaUtentiDto.add(modelMapper.map(u, UtenteDto.class)));
		return listaUtentiDto;
	}

	@Override
	public boolean loginUser(UtenteLoginRequestDto utenteLoginRequestDto) {
		
		Utente utente = new Utente();
		utente.setEmail(utenteLoginRequestDto.getEmail());
		utente.setPassword(utenteLoginRequestDto.getPassword());
		
		String sha256hex = DigestUtils.sha256Hex(utente.getPassword());
		
		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), sha256hex);
		
		return credenzialiUtente != null ? true : false;
	}

	@Override
	public Utente findByEmail(String email) {

		return utenteDao.findByEmail(email);
	}

}
