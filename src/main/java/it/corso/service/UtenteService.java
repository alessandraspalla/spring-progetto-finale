package it.corso.service;


import java.util.List;

import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteDtoAggiornamento;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Utente;

public interface UtenteService {

	void userRegistration(UtenteRegistrazioneDto utenteDto);
	boolean existsByEmail(String email);
	void deleteByEmail(String email);
	UtenteDto getUserByEmail(String email);
	void updateUser(UtenteDtoAggiornamento utenteDtoAgg);
	List<UtenteDto> getAllUsers();
	boolean loginUser(UtenteLoginRequestDto utenteLoginRequestDto);
	Utente findByEmail(String email);
}
