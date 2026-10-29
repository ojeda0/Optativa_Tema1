package com.daw.web.controllers;

import java.util.List;

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

import com.daw.persistence.entities.TareaEntity;
import com.daw.services.TareaService;
import com.daw.services.exceptions.TareaException;
import com.daw.services.exceptions.TareaNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tareas")
@RequiredArgsConstructor
public class TareaController {

	private final TareaService tareaService;
	
	@GetMapping
	public ResponseEntity<List<TareaEntity>> list(){
		return ResponseEntity.ok(this.tareaService.findAll());
	}
	
	@GetMapping("/{idTarea}")
	public ResponseEntity<?> findById(@PathVariable long idTarea){
		
		try {
			return ResponseEntity.ok(this.tareaService.findById(idTarea));
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}

	}
	
	@PostMapping
	public ResponseEntity<?> crearTarea(@RequestBody TareaEntity t){
		
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.tareaService.crearTarea(t));
		}
		catch(TareaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		
	}
	
	@DeleteMapping("/{idTarea}")
	public ResponseEntity<?> deleteById(@PathVariable long idTarea){
		try {
			this.tareaService.deleteById(idTarea);
			return ResponseEntity.ok().build();
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
	@PutMapping("/{idTarea}")
	public ResponseEntity<?> actualizarTarea(@PathVariable long idTarea, @RequestBody TareaEntity t){
		try {
			return ResponseEntity.ok(this.tareaService.actualizarTarea(idTarea, t));
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		catch(TareaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}
	
	@PutMapping("/{idTarea}/iniciar")
	public ResponseEntity<?> iniciarTarea(@PathVariable long idTarea){
		try {
			return ResponseEntity.ok(this.tareaService.iniciarTarea(idTarea));
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		catch(TareaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		
	}
	
	@PutMapping("/{idTarea}/revisar")
	public ResponseEntity<?> revisarTarea(@PathVariable long idTarea){
		try {
			return ResponseEntity.ok(this.tareaService.revisarTarea(idTarea));
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		catch(TareaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		
	}
	
	@PutMapping("/{idTarea}/completar")
	public ResponseEntity<?> completarTarea(@PathVariable long idTarea) {
		try {
			return ResponseEntity.ok(this.tareaService.completarTarea(idTarea));
		}
		catch(TareaNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		catch(TareaException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		
	}
	
	@GetMapping("/pendientes")
	public ResponseEntity<List<TareaEntity>> obtenerPendientes() {
		return ResponseEntity.ok(this.tareaService.obtenerPendientes());
	}
	
	@GetMapping("/en_progreso")
	public ResponseEntity<List<TareaEntity>> obtenerEnProgreso() {
		return ResponseEntity.ok(this.tareaService.obtenerEnProgreso());
	}
	
	@GetMapping("/completadas")
	public ResponseEntity<List<TareaEntity>> obtenerCompletadas() {
		return ResponseEntity.ok(this.tareaService.obtenerCompletadas());
	}
	
	@GetMapping("/titulo")
	public ResponseEntity<List<TareaEntity>> obtenerPorTitulo(@RequestParam(defaultValue = "") String titulo) {
		return ResponseEntity.ok(this.tareaService.obtenerPorTitulo(titulo));
	}
	
	@GetMapping("/en_revision")
	public ResponseEntity<List<TareaEntity>> obtenerEnRevision() {
		return ResponseEntity.ok(this.tareaService.obtenerEnRevision());
	}
	
}
