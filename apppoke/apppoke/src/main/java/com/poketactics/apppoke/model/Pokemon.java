package com.poketactics.apppoke.model;

// Importaciones: Traemos las herramientas que necesitamos de Spring y Lombok.
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Clase Entidad 'Pokemon'.
 * * - @Entity: Esta es la anotación CLAVE. Le dice a Spring/Hibernate:
 * "Esta clase no es código normal, es el molde para una tabla en la base de datos".
 * Por defecto, la tabla se llamará 'pokemon' (en minúsculas).
 * * - @Data: Es de Lombok. Nos ahorra escribir un montón de código repetitivo
 * (getters, setters, equals, hashCode, toString). ¡Es muy útil!
 */
@Entity
@Data
public class Pokemon {

    /**
     * El ID es el identificador único de cada fila en la tabla.
     * - @Id: Marca este campo como la Llave Primaria (Primary Key).
     * - @GeneratedValue(...): Le dice a la base de datos que genere este número
     * automáticamente. 'IDENTITY' significa que usará un contador (auto-increment): 1, 2, 3...
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos básicos del Pokémon. Se convertirán en columnas de la tabla.

    // El nombre (ej. "Pikachu")
    private String name;

    // El tipo principal (ej. "Eléctrico")
    private String type1;

    // El tipo secundario (ej. "Volador"). Puede estar vacío (null) para Pokémon de un solo tipo.
    private String type2;

    // La URL de la imagen para mostrarla luego en el frontend.
    private String imageUrl;



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // EAGER para que traiga los ataques siempre, Añadimos cascade aquí
    @JoinTable(
            name = "pokemon_moves",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "move_id")
    )
    private List<Move> moves;
}