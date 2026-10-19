package com.daw.web.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.services.PokemonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {
	
	private final PokemonService pokemonService;
	
	//Obtener todos los pokemon
	@GetMapping
	public ResponseEntity<List<PokemonEntity>> findAll() {
		return ResponseEntity.ok(this.pokemonService.findAll());
	}
	

}
