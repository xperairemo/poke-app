package com.poketactics.apppoke.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.poketactics.apppoke.dto.MoveDetailDTO;
import com.poketactics.apppoke.dto.PokemonPokeApiDTO;
import com.poketactics.apppoke.model.Move;
import com.poketactics.apppoke.model.Pokemon;
import com.poketactics.apppoke.repository.MoveRepository;
import com.poketactics.apppoke.repository.PokemonRepository;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private MoveRepository moveRepository;

    @Mock
    private TypeService typeService;

    @InjectMocks
    private PokemonService pokemonService;

    @Test
    void testGetPokemonFromApi_Success() {
        // 1. ARRANGE (Preparar el escenario)
        String pokemonName = "bulbasaur";

        // --- Configuración del DTO de Pokemon ---
        PokemonPokeApiDTO mockDto = new PokemonPokeApiDTO();
        mockDto.setName("bulbasaur");

        // Añadimos tipos para evitar IndexOutOfBoundsException
        PokemonPokeApiDTO.TypeInfo typeInfo = new PokemonPokeApiDTO.TypeInfo();
        typeInfo.setName("grass");
        PokemonPokeApiDTO.TypeSlot typeSlot = new PokemonPokeApiDTO.TypeSlot();
        typeSlot.setType(typeInfo);
        mockDto.setTypes(List.of(typeSlot));

        // Añadimos un ataque al DTO
        PokemonPokeApiDTO.MoveInfo moveInfo = new PokemonPokeApiDTO.MoveInfo();
        moveInfo.setName("tackle");
        moveInfo.setUrl("https://pokeapi.co/api/v2/move/33/");
        PokemonPokeApiDTO.MoveSlot moveSlot = new PokemonPokeApiDTO.MoveSlot();
        moveSlot.setMove(moveInfo);
        mockDto.setMoves(List.of(moveSlot));

        // Añadimos Sprites
        PokemonPokeApiDTO.Sprites sprites = new PokemonPokeApiDTO.Sprites();
        sprites.setFront_default("http://image.png");
        mockDto.setSprites(sprites);

        // --- Configuración del DTO de Detalle de Ataque ---
        MoveDetailDTO moveDetail = new MoveDetailDTO();
        moveDetail.setName("tackle");
        moveDetail.setPower(40);
        MoveDetailDTO.TypeInfo moveTypeInfo = new MoveDetailDTO.TypeInfo();
        moveTypeInfo.setName("normal");
        moveDetail.setType(moveTypeInfo);

        // --- DEFINIR COMPORTAMIENTO DE LOS MOCKS ---

        // Mock de la PokeAPI para el Pokemon
        when(restTemplate.getForObject(contains("/pokemon/"), eq(PokemonPokeApiDTO.class)))
                .thenReturn(mockDto);

        // Mock de la Caché: Simulamos que el ataque NO está en la DB (para testear la llamada a la API)
        when(moveRepository.findByName("tackle")).thenReturn(Optional.empty());

        // Mock de la PokeAPI para el detalle del ataque
        when(restTemplate.getForObject(eq("https://pokeapi.co/api/v2/move/33/"), eq(MoveDetailDTO.class)))
                .thenReturn(moveDetail);

        // Mock del guardado de ataques
        when(moveRepository.save(any(Move.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock del guardado de Pokemon
        when(pokemonRepository.save(any(Pokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. ACT (Ejecutar)
        Pokemon result = pokemonService.getPokemonFromApi(pokemonName);

        // 3. ASSERT (Verificar)
        assertNotNull(result);
        assertEquals("bulbasaur", result.getName());
        assertEquals("grass", result.getType1());

        // Verificamos que el ataque se ha procesado correctamente
        assertFalse(result.getMoves().isEmpty());
        assertEquals("tackle", result.getMoves().get(0).getName());
        assertEquals("normal", result.getMoves().get(0).getType());

        // Verificamos que se interactuó con los repositorios
        verify(moveRepository, times(1)).findByName("tackle");
        verify(pokemonRepository, times(1)).save(any(Pokemon.class));
    }
}