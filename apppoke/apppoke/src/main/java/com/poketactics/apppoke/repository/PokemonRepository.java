package com.poketactics.apppoke.repository;

import com.poketactics.apppoke.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Esta es una INTERFAZ.
 * Al heredar de JpaRepository, Spring entiende que esta clase
 * manejará la tabla 'Pokemon'.
 * * Los parámetros <Pokemon, Long> significan:
 * 1. 'Pokemon': Es la clase que va a gestionar.
 * 2. 'Long': Es el tipo de dato de su ID (Primary Key).
 */
@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    // Aquí, por arte de magia, ya tenemos métodos como:
    // .save(pokemon) -> Para guardar
    // .findAll()     -> Para traer todos los pokemon de la DB
    // .findById(id)  -> Para buscar uno solo
    // .delete(pokemon)-> Para borrar
}