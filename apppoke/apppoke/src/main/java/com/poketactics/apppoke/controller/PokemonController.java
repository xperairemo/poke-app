package com.poketactics.apppoke.controller;

import com.poketactics.apppoke.model.Pokemon;
import com.poketactics.apppoke.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @RestController: Le dice a Spring que esta clase es un punto de entrada de la API.
 * Automáticamente convierte las respuestas (como un objeto Pokemon) en formato JSON.
 * * @RequestMapping("/api/pokemon"): Todos los caminos de este controlador empezarán por aquí.
 */
@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:4200") // Esto es VITAL para que Angular pueda hablar con Spring después
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    /**
     * @GetMapping("/{name}"): Define que este método responde a peticiones GET.
     * Ejemplo: localhost:8080/api/pokemon/pikachu
     * * @PathVariable String name: Coge lo que escribas en la URL y lo mete en la variable 'name'.
     */
    @GetMapping("/{name}")
    public Pokemon getPokemon(@PathVariable String name) {
        // Llamamos al cocinero (Service) para que haga su magia
        return pokemonService.getPokemonFromApi(name);
    }


    /**
     * MÉTODO PUT: Actualizar los ataques de un Pokémon.
     * El usuario enviará por Angular una lista de IDs de ataques.
     */
    @PutMapping("/{id}/moves")
    public Pokemon updateMoves(@PathVariable Long id, @RequestBody List<Long> moveIds) {
        // Le pedimos al servicio que realice la actualización en la base de datos
        return pokemonService.updatePokemonMoves(id, moveIds);
    }


    @GetMapping("/analyze/{myPokeName}/{rivalName}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<String> analyze(@PathVariable String myPokeName, @PathVariable String rivalName) {
        // Buscamos ambos en nuestra DB/API
        Pokemon mine = pokemonService.getPokemonFromApi(myPokeName);
        Pokemon rival = pokemonService.getPokemonFromApi(rivalName);

        // Devolvemos la lista de consejos que programamos antes
        return pokemonService.getTacticalAdvice(mine, rival);
    }
}