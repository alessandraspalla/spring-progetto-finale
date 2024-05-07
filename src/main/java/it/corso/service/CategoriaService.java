package it.corso.service;

import java.util.List;

import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;

public interface CategoriaService {

	List<CategoriaDto> getCategories(String filter);
	CategoriaDto getCategoryById(int id);
	void createCategoria(CategoriaCreazioneDto categoriaCreazioneDto);
	// mancano update e delete
	// nell'update per fare il find riutilizza il metodo getCategoryById
}
