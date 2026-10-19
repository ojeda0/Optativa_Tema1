package com.daw.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.persistence.repositories.PokemonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PokemonService {
	
	private final PokemonRepository pokemonRepository;
	
	//Obtener todos los pokemon
	public List<PokemonEntity> findAll(){
		return this.pokemonRepository.findAll();
	}

}
