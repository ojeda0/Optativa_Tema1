package com.daw.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.daw.persistence.entities.TareaEntity;
import com.daw.persistence.entities.enums.Estado;

public interface TareaRepository extends JpaRepository<TareaEntity, Long>{
	
	//SELECT * FROM tarea WHERE estado = "PENDIENTE";
	@Query
	List<TareaEntity>findByEstado(Estado estado);
	
	//SELECT * FROM tarea WHERE titulo LIKE %parametro%
	@Query
	List<TareaEntity>findByTituloContainingIgnoreCase(String titulo);
}