package com.poketactics.apppoke.service;

import com.poketactics.apppoke.dto.MoveDetailDTO;
import com.poketactics.apppoke.dto.PokemonPokeApiDTO;
import com.poketactics.apppoke.model.Pokemon;
import com.poketactics.apppoke.model.Move;
import com.poketactics.apppoke.repository.PokemonRepository;
import com.poketactics.apppoke.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private TypeService typeService; // El cerebro que creamos antes

    /**
     * MÉTODO 1: Traer datos de la API y guardarlos.
     */
    public Pokemon getPokemonFromApi(String name) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + name.toLowerCase();
        PokemonPokeApiDTO dto = restTemplate.getForObject(url, PokemonPokeApiDTO.class);

        if (dto != null) {
            Pokemon pokemon = new Pokemon();
            // Seteamos los datos básicos del Pokémon
            pokemon.setName(dto.getName());
            pokemon.setImageUrl(dto.getSprites().getFront_default());
            pokemon.setType1(dto.getTypes().get(0).getType().getName());

            if (dto.getTypes().size() > 1) {
                pokemon.setType2(dto.getTypes().get(1).getType().getName());
            }

            // --- LÓGICA DE CACHÉ DE ATAQUES ---
            List<Move> officialMoves = new ArrayList<>();

            // Procesamos los primeros 4 ataques
            dto.getMoves().stream().limit(4).forEach(moveSlot -> {
                String moveName = moveSlot.getMove().getName();

                // 1. PRIMERO MIRAMOS EN NUESTRA "DESPENSA" (Base de Datos)
                // Intentamos buscar el ataque por su nombre
                Optional<Move> existingMove = moveRepository.findByName(moveName);

                if (existingMove.isPresent()) {
                    // ¡Bingo! Ya lo conocíamos. Lo usamos directamente y ahorramos tiempo.
                    officialMoves.add(existingMove.get());
                } else {
                    // 2. SI NO ESTÁ, HACEMOS EL ESFUERZO DE IR A INTERNET
                    String moveUrl = moveSlot.getMove().getUrl();
                    MoveDetailDTO moveDetail = restTemplate.getForObject(moveUrl, MoveDetailDTO.class);

                    if (moveDetail != null) {
                        Move newMove = new Move();
                        newMove.setName(moveDetail.getName());
                        newMove.setPower(moveDetail.getPower());
                        newMove.setType(moveDetail.getType().getName());

                        // IMPORTANTE: Lo guardamos para que la próxima vez esté disponible
                        Move savedMove = moveRepository.save(newMove);
                        officialMoves.add(savedMove);
                    }
                }
            });

            pokemon.setMoves(officialMoves);
            // Guardamos el Pokémon (Hibernate se encarga de las relaciones en la tabla intermedia)
            return pokemonRepository.save(pokemon);
        }
        return null;
    }

    /**
     * MÉTODO 2: La Inteligencia Táctica (Lo nuevo)
     */
    public List<String> getTacticalAdvice(Pokemon myPokemon, Pokemon rival) {
        List<String> advice = new ArrayList<>();

        // 1. Análisis Defensivo: ¿Cuánto daño me hace el rival?
        double damageTaken = typeService.calculateMultiplier(rival.getType1(), myPokemon.getType1(), myPokemon.getType2());

        if (damageTaken >= 2.0) {
            advice.add("⚠️ ¡CUIDADO! " + rival.getName() + " te hace x" + damageTaken + " de daño.");
        }

        // 2. Análisis Ofensivo: ¿Tengo ataques efectivos?
        if (myPokemon.getMoves() != null) {
            for (Move move : myPokemon.getMoves()) {
                double damageDealt = typeService.calculateMultiplier(move.getType(), rival.getType1(), rival.getType2());

                if (damageDealt >= 2.0) {
                    advice.add("🔥 ¡USA " + move.getName().toUpperCase() + "! Es x" + damageDealt + " de efectivo.");
                }
            }
        }

        if (advice.isEmpty()) advice.add("⚖️ Combate equilibrado. ¡Mucha suerte!");

        return advice;
    }



    public Pokemon updatePokemonMoves(Long pokemonId, List<Long> moveIds) {
        // 1. Buscamos el pokemon en nuestra base de datos
        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new RuntimeException("Pokemon no encontrado"));

        // 2. Buscamos los ataques que el usuario ha seleccionado (por sus IDs)
        // Usaremos un nuevo repositorio de ataques
        List<Move> selectedMoves = moveRepository.findAllById(moveIds);

        // 3. Actualizamos la lista de ataques del pokemon
        pokemon.setMoves(selectedMoves);

        // 4. Guardamos los cambios
        return pokemonRepository.save(pokemon);
    }
}