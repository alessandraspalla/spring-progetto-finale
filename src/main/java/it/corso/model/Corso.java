package it.corso.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "corso")
public class Corso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_C")
	private int id;
	
	@Column(name = "Nome_Corso")
	private String nomeCorso;
	
	@Column(name = "Descrizione_breve")
	private String descrizioneBreve;
	
	@Column(name = "Descrizione_completa")
	private String descrizioneCompleta;
	
	@Column(name = "Durata")
	private int durata;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn
	(
		name = "FK_CA", referencedColumnName = "ID_CA"
	)
	private Categoria categoria;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable
	(
		name = "utenti_corsi", joinColumns = @JoinColumn(name = "FK_CU", referencedColumnName = "ID_C"),
		inverseJoinColumns = @JoinColumn(name = "FK_UC", referencedColumnName = "ID_U")
	)
	private List<Utente> utenti = new ArrayList<>();
}
