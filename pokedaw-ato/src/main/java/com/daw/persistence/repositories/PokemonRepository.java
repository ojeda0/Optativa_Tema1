package com.daw.persistence.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.persistence.entities.enums.Tipo;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Long>{

	//Buscar por número de pokédex
	List<PokemonEntity> findByNumeroPokedex(int numeroPokedex);
	
	//Buscar los pokemon capturados en un rango de fechas
	List<PokemonEntity> findByFechaCapturaBetween(LocalDate fechaInicio, LocalDate fechaFin);
	
	//Buscar pokemon por tipo
	List<PokemonEntity> findByTipo1OrTipo2(Tipo tipo1, Tipo tipo2);
	
}