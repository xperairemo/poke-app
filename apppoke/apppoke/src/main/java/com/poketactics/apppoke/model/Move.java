package com.poketactics.apppoke.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Integer power;

    @ManyToMany(mappedBy = "moves") // Indica que la relación la "manda" la clase Pokemon
    @JsonIgnore // Evita bucles infinitos al convertir a JSON
    private List<Pokemon> pokemons;
}