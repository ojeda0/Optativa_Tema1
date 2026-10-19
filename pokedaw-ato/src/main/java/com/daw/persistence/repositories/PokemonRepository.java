package com.daw.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.persistence.entities.PokemonEntity;

public interface PokemonRepository extends JpaRepository<PokemonEntity, Long>{

}