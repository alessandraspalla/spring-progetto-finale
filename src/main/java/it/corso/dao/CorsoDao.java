package it.corso.dao;

import org.springframework.data.repository.CrudRepository;

import it.corso.model.Corso;

public interface CorsoDao extends CrudRepository<Corso, Integer> {

}
