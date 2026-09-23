package com.daw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holamundo")
public class HolamundoController {

	@GetMapping
	public String saludar() {
		
		return "Holas";
		
	}
	
	/*
	 * El RequestParam se utiliza cuando tengamos cualquier método en el que tengamos
	 * que mandar parámetros al servidor.
	 * 
	 * Ejemplo:
	 * 		localhost:8080/libros/buscarPorTitulo?titulo=
	 */
	@GetMapping("/saludo")
	public String saludar2(@RequestParam(defaultValue = "Juan de la Palmilla") String nombre) {
		
		return "Holiiii " + nombre;
		
	}
	
	/*
	 * El PathVariable se utiliza exclusivamente para IDs.
	 */
	@GetMapping("/libros/{id}")
	public String libros(@PathVariable long id) {
		
		return "Aquí tienes el libro con id " + id;
		
	}
	
}
