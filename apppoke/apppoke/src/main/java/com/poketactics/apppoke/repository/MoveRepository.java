package com.poketactics.apppoke.repository;

import com.poketactics.apppoke.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    //busca en la tabla 'move' por la columna 'name'
    Optional<Move> findByName(String name);
}