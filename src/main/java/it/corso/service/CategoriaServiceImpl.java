package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	private CategoriaDao categoriaDao;
	
	private ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public List<CategoriaDto> getCategories(String filter) {
	    List<Categoria> categorie = (List<Categoria>) categoriaDao.findAll();
	    List<CategoriaDto> categorieDto = new ArrayList<>();
	    categorie.forEach(c -> categorieDto.add(modelMapper.map(c, CategoriaDto.class)));

	    if (filter != null && !filter.isEmpty()) {
	        return categorieDto.stream()
	                .filter(categoria -> categoria.getNomeCategoria().toLowerCase().contains(filter.toLowerCase()))
	                .collect(Collectors.toList());
	    } else {
	        return categorieDto;
	    }
	}

	@Override
	public CategoriaDto getCategoryById(int id) {
	    CategoriaDto categoriaDto = null;

	    try {
	        Optional<Categoria> categoriaDb = categoriaDao.findById(id);
	        
	        if(categoriaDb.isPresent()) {
	            Categoria categoria = categoriaDb.get();
	            categoriaDto = modelMapper.map(categoria, CategoriaDto.class); // Mappa l'entità Categoria a CategoriaDto
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return categoriaDto;
	}

	@Override
	public void createCategoria(CategoriaCreazioneDto categoriaCreazioneDto) {
		
		Categoria categoria = new Categoria();
		
		categoria.setNomeCategoria(categoriaCreazioneDto.getNomeCategoria());
		
		categoriaDao.save(categoria);
	}


}
