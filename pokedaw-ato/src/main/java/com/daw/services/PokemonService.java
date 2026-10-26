package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.persistence.entities.enums.Pokeball;
import com.daw.persistence.entities.enums.Tipo;
import com.daw.persistence.repositories.PokemonRepository;
import com.daw.services.exceptions.PokemonException;
import com.daw.services.exceptions.PokemonNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PokemonService {
	
	private final PokemonRepository pokemonRepository;
	
	//Obtener todos los pokemon
	public List<PokemonEntity> findAll() {
		return this.pokemonRepository.findAll();
	}
	
	//Obtener pokemon por id
	public PokemonEntity findById(long idPokemon) {
		
		if(!this.pokemonRepository.existsById(idPokemon)) {
			throw new PokemonNotFoundException(String.format("ERROR. No se ha podido encontrar el Pokémon con ID %d.", idPokemon));
		}
		
		return this.pokemonRepository.findById(idPokemon).get();
	}
	
	//Crear un pokemon
	public PokemonEntity crearPokemon(PokemonEntity pokemon) {
		
		if(pokemon.getFechaCaptura() != null) {
			
			throw new PokemonException("ERROR. La fecha de captura no puede ser modificada.");
			
		}
		
		if(pokemon.getCapturado() == null) {
			
			pokemon.setCapturado(Pokeball.POKEBALL);
			
		}
		
		if(pokemon.getTipo1() == null || pokemon.getTipo1() == Tipo.NINGUNO) {
			
			throw new PokemonException("ERROR. El tipo 1 no puede ser nulo.");
			
		}
		
		if(pokemon.getTipo1().equals(pokemon.getTipo2()) || pokemon.getTipo2() == null) {
			
			pokemon.setTipo2(Tipo.NINGUNO);
			
		}
		
		pokemon.setId(0L);
		pokemon.setFechaCaptura(LocalDate.now());
		
		return this.pokemonRepository.save(pokemon);
		
	}
	
	//Modificar un pokemon
	public PokemonEntity modificarPokemon(long idPokemon, PokemonEntity p) {
		
		PokemonEntity pokemon = this.findById(idPokemon);
		
		if(p.getId() != idPokemon) {
			
			throw new PokemonException(String.format("ERROR. El id del body (%d) y el id del path (%d) no coinciden.", p.getId(), idPokemon));
			
		}
		
		if(!this.pokemonRepository.existsById(idPokemon)) {
			
			throw new PokemonNotFoundException(String.format("ERROR. El id (%d) no existe.", idPokemon));
			
		}
		
		if(p.getFechaCaptura() != null) {
			
			throw new PokemonException("ERROR. La fecha de captura no puede ser modificada.");
			
		}
		
		if(p.getTipo1() == null || p.getTipo1() == Tipo.NINGUNO) {
			
			throw new PokemonException("ERROR. El tipo 1 no puede ser nulo.");
			
		}
		
		if(p.getTipo1().equals(p.getTipo2()) || p.getTipo2() == null) {
			
			p.setTipo2(Tipo.NINGUNO);
			
		}
		
		if(p.getCapturado() == null) {
			
			p.setCapturado(Pokeball.POKEBALL);
			
		}
		
		pokemon.setNumeroPokedex(p.getNumeroPokedex());
		pokemon.setTitulo(p.getTitulo());
		pokemon.setTipo1(p.getTipo1());
		pokemon.setTipo2(p.getTipo2());
		pokemon.setCapturado(p.getCapturado());
		
		return this.pokemonRepository.save(pokemon);
		
	}
	
	//Borrar un pokemon
	public void borrarPokemon(long idPokemon) {
		
		if(!this.pokemonRepository.existsById(idPokemon)) {
			
			throw new PokemonNotFoundException(String.format("ERROR. El id (%d) no existe.", idPokemon));
			
		}
		
		this.pokemonRepository.deleteById(idPokemon);
		
	}
	
	//Buscar pokémon según su número de la pokédex
	public List<PokemonEntity> buscarPorNumeroPokedex(int numeroPokedex) {
		
		return this.pokemonRepository.findByNumeroPokedex(numeroPokedex);
		
	}
	
	//Buscar los pokemon capturados en un rango de fechas
	public List<PokemonEntity> buscarPorRangoFecha(LocalDate fechaInicio, LocalDate fechaFin) {
		
		return this.pokemonRepository.findByFechaCapturaBetween(fechaInicio, fechaFin);
		
	}
	
	//Buscar pokemon por tipo
	public List<PokemonEntity> buscarPorTipo(String tipo) {
		Tipo tipoEnum = Tipo.valueOf(tipo.toUpperCase());
		
		
		return this.pokemonRepository.findByTipo1OrTipo2(tipoEnum, tipoEnum);
		
	}

}
