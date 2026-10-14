package com.daw.persistence.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daw.persistence.entities.TareaEntity;
import com.daw.persistence.entities.enums.Estado;

public interface TareaRepository extends JpaRepository<TareaEntity, Long>{
	
	//SELECT * FROM tarea WHERE estado = "PENDIENTE";
	@Query
	List<TareaEntity>findByEstado(Estado estado);
	
	//SELECT * FROM tarea WHERE fecha_vencimiento < now();
	@Query
	List<TareaEntity>findByFechaVencimientoBefore(LocalDate fecha);
	
	//SELECT * FROM tarea WHERE fecha_vencimiento > now();
	@Query
	List<TareaEntity>findByFechaVencimientoAfter(LocalDate fecha); // decirle al vladi lo de greater than equal
	
	//SELECT * FROM tarea WHERE titulo LIKE %parametro%
	@Query
	List<TareaEntity>findByTituloContaining(String titulo); //decirle al vladi lo de ignore case
		
}