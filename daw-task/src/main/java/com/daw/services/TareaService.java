package com.daw.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.daw.persistence.entities.TareaEntity;
import com.daw.persistence.entities.enums.Estado;
import com.daw.persistence.repositories.TareaRepository;
import com.daw.services.exceptions.TareaException;
import com.daw.services.exceptions.TareaNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {
	
	final private TareaRepository tareaRepository;
	
	//Listar todas las tareas
	public List<TareaEntity> findAll(){
		return this.tareaRepository.findAll();
	}
	
	//Buscar por id de tarea
	public TareaEntity findById(long idTarea) {
		
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException(String.format("La tarea con id %d no existe.", idTarea));
		}
		
		return this.tareaRepository.findById(idTarea).get();
		
	}
	
	//Crear una tarea
	public TareaEntity crearTarea(TareaEntity t) {
		
		if(t.getFechaCreacion() != null) {
			throw new TareaException(String.format("ERROR. No se puede modificar la fecha de creación de una tarea."));
		}
		
		if(t.getEstado() != null) {
			throw new TareaException(String.format("ERROR. No se puede modificar el estado de una tarea."));
		}
		
		if(t.getFechaVencimiento() == null || !t.getFechaVencimiento().isAfter(LocalDate.now())) {
			throw new TareaException(String.format("ERROR. La fecha límite es obligatoria y debe ser posterior al día de hoy."));
		}
		
		t.setId(0L);
		t.setFechaCreacion(LocalDate.now());
		t.setEstado(Estado.PENDIENTE);
		
		return this.tareaRepository.save(t);
		
	}
	
	//Borrar tarea
	public void deleteById(long idTarea) {
		
		if(!this.tareaRepository.existsById(idTarea)) {
			throw new TareaNotFoundException(String.format("La tarea con id %d no existe.", idTarea));
		}
		
		this.tareaRepository.deleteById(idTarea);
		
	}
	
	//Actualizar tarea
	public TareaEntity actualizarTarea(long idTarea, TareaEntity t) {
		
		TareaEntity tarea = this.findById(idTarea);
		
		if(t.getId() != idTarea) {
			
			throw new TareaException(String.format("El id del body (%d) y el id del path (%d) no coinciden", t.getId(), idTarea));
			
		}
		
		if(t.getEstado() != null) {
			throw new TareaException("ERROR. No se puede modificar el estado de una tarea.");
		}
		
		if(t.getFechaCreacion() != null) {
			throw new TareaException("ERROR. No se puede modificar la fecha de creación de una tarea.");
		}
		
		if(t.getFechaVencimiento() != null && t.getFechaVencimiento().isBefore(LocalDate.now())) {
			throw new TareaException("ERROR. La fecha de vencimiento no puede ser anterior a la fecha actual.");
		}
		
		tarea.setTitulo(t.getTitulo());
		tarea.setDescripcion(t.getDescripcion());
		tarea.setFechaVencimiento(t.getFechaVencimiento());
		
		return this.tareaRepository.save(tarea);
	}
	
	//Iniciar tarea
	public TareaEntity iniciarTarea(long idTarea) {
		
		//Traigo tarea de la BD y compruebo que exista el ID
		TareaEntity tarea = this.findById(idTarea);
		
		if(!tarea.getEstado().equals(Estado.PENDIENTE)) {
			throw new TareaException("ERROR. No se puede iniciar una tarea que no esté PENDIENTE.");
		}
		
		tarea.setEstado(Estado.EN_PROGRESO);
		
		return this.tareaRepository.save(tarea);
		
	}
	
	//Completar tarea
	public TareaEntity completarTarea(long idTarea) {
		TareaEntity tarea = this.findById(idTarea);
		
		if(!tarea.getEstado().equals(Estado.EN_PROGRESO)) {
			throw new TareaException("ERROR. Solo pueden completarse tareas en progreso.");
		}
		
		tarea.setEstado(Estado.COMPLETADA);
		
		return this.tareaRepository.save(tarea);
		
	}
	
	//Obtener tareas pendientes
	public List<TareaEntity> obtenerPendientes() {
		return this.tareaRepository.findByEstado(Estado.PENDIENTE);
	}
	
	//Obtener tareas en progreso
	public List<TareaEntity> obtenerEnProgreso() {
		return this.tareaRepository.findByEstado(Estado.EN_PROGRESO);
	}
	
	//Obtener tareas completadas
	public List<TareaEntity> obtenerCompletadas() {
		return this.tareaRepository.findByEstado(Estado.COMPLETADA);
	}
	
	//Obtener tareas vencidas
	public List<TareaEntity> obtenerVencidas() {
		return this.tareaRepository.findByFechaVencimientoBefore(LocalDate.now());
	}
	
	//Obtener tareas no vencidas
	public List<TareaEntity> obtenerNoVencidas() {
		return this.tareaRepository.findByFechaVencimientoAfter(LocalDate.now());
	}
	
	//Obtener tareas por su título
	public List<TareaEntity> obtenerPorTitulo(String titulo) {
		return this.tareaRepository.findByTituloContainingIgnoreCase(titulo);
	}
	
}
