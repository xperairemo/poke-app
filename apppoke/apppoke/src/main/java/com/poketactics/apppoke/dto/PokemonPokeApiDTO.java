package com.poketactics.apppoke.dto;

import lombok.Data;
import java.util.List;

@Data
public class PokemonPokeApiDTO {
    private String name;
    private List<TypeSlot> types;
    private Sprites sprites;
    private List<MoveSlot> moves; // <--- NUEVO: Lista de ataques

    @Data public static class TypeSlot { private TypeInfo type; }
    @Data public static class TypeInfo { private String name; }
    @Data public static class Sprites { private String front_default; }

    // --- NUEVO: Estructuras para los ataques ---
    @Data public static class MoveSlot { private MoveInfo move; }
    @Data public static class MoveInfo {
        private String name;
        private String url; // La URL para obtener el tipo del ataque después
    }
}