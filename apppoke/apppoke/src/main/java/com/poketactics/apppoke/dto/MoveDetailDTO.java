package com.poketactics.apppoke.dto;

import lombok.Data;

@Data
public class MoveDetailDTO {
    private String name;
    private Integer power; // La potencia (puede ser null en ataques de estado)
    private TypeInfo type; // El tipo del ataque

    @Data
    public static class TypeInfo {
        private String name;
    }
}