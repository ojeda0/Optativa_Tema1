package com.daw.web.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.persistence.entities.PokemonEntity;
import com.daw.services.PokemonService;
import com.daw.services.exceptions.PokemonException;
import com.daw.services.exceptions.PokemonNotFoundException;

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
	
	//Obtener pokemon por su id
	@GetMapping("/{idPokemon}")
	public ResponseEntity<?> findById(@PathVariable long idPokemon) {
		try {
			return ResponseEntity.ok(this.pokemonService.findById(idPokemon));
		}
		catch(PokemonNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
	//Crear un pokemon
	@PostMapping
	public ResponseEntity<?> crearPokemon(@RequestBody PokemonEntity pokemon) {
		
		try {
			
			return ResponseEntity.status(HttpStatus.CREATED).body(this.pokemonService.crearPokemon(pokemon));
			
		}catch(PokemonException ex) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			
		}
		
	}
	
	//Modificar un pokemon
	@PutMapping("/{idPokemon}")
	public ResponseEntity<?> modificarPokemon(@PathVariable long idPokemon, @RequestBody PokemonEntity p){
		
		try {
			
			return ResponseEntity.ok(this.pokemonService.modificarPokemon(idPokemon, p));
			
		}catch(PokemonNotFoundException ex) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
			
		}catch(PokemonException ex) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
			
		}
		
	}
	
	//Borrar pokemon
	@DeleteMapping("/{idPokemon}")
	public ResponseEntity<?> borrarPokemon(@PathVariable long idPokemon) {
		
		try {
			
			this.pokemonService.borrarPokemon(idPokemon);
			return ResponseEntity.ok().build();
			
		}catch(PokemonNotFoundException ex) {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
			
		}
		
	}
	
	//Buscar pokémon por su número de pokédex
	@GetMapping("/pokedex")
	public ResponseEntity<List<PokemonEntity>> buscarPorNumeroPokedex(@RequestParam int numeroPokedex) {
		
		return ResponseEntity.ok(this.pokemonService.buscarPorNumeroPokedex(numeroPokedex));
		
	}
	
	//Buscar los pokemon capturados en un rango de fechas
	@GetMapping("/fecha")
	public ResponseEntity<List<PokemonEntity>> buscarPorRangoFecha(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
			
		return ResponseEntity.ok(this.pokemonService.buscarPorRangoFecha(fechaInicio, fechaFin));
			
	}
	
	//Buscar pokémon por tipo
	@GetMapping("/tipo")
	public ResponseEntity<?> buscarPorTipo(@RequestParam String tipo) {
		try {
			
			return ResponseEntity.ok(this.pokemonService.buscarPorTipo(tipo));
			
		}catch(IllegalArgumentException ex) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El tipo indicado no existe.");
			
		}

			
	}
	

}
