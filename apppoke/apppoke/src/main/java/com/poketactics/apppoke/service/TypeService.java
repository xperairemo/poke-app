package com.poketactics.apppoke.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TypeService {

    // Mapa de mapas: Map<TipoAtacante, Map<TipoDefensor, Multiplicador>>
    private final Map<String, Map<String, Double>> typeChart = new HashMap<>();

    public TypeService() {
        initializeChart();
    }

    private void initializeChart() {
        // Inicializamos los 18 tipos actuales
        String[] types = {
                "normal", "fire", "water", "grass", "electric", "ice", "fighting",
                "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
                "dragon", "steel", "dark", "fairy"
        };

        for (String type : types) {
            typeChart.put(type, new HashMap<>());
        }

        // --- RELACIONES DE DAÑO (Solo definimos las que no son 1.0) ---

        // NORMAL
        setDmg("normal", 0.5, "rock", "steel");
        setDmg("normal", 0.0, "ghost");

        // FUEGO
        setDmg("fire", 2.0, "grass", "ice", "bug", "steel");
        setDmg("fire", 0.5, "fire", "water", "rock", "dragon");

        // AGUA
        setDmg("water", 2.0, "fire", "ground", "rock");
        setDmg("water", 0.5, "water", "grass", "dragon");

        // PLANTA
        setDmg("grass", 2.0, "water", "ground", "rock");
        setDmg("grass", 0.5, "fire", "grass", "poison", "flying", "bug", "dragon", "steel");

        // ELÉCTRICO
        setDmg("electric", 2.0, "water", "flying");
        setDmg("electric", 0.5, "grass", "electric", "dragon");
        setDmg("electric", 0.0, "ground");

        // HIELO
        setDmg("ice", 2.0, "grass", "ground", "flying", "dragon");
        setDmg("ice", 0.5, "fire", "water", "ice", "steel");

        // LUCHA
        setDmg("fighting", 2.0, "normal", "ice", "rock", "dark", "steel");
        setDmg("fighting", 0.5, "poison", "flying", "psychic", "bug", "fairy");
        setDmg("fighting", 0.0, "ghost");

        // VENENO
        setDmg("poison", 2.0, "grass", "fairy");
        setDmg("poison", 0.5, "poison", "ground", "rock", "ghost");
        setDmg("poison", 0.0, "steel");

        // TIERRA
        setDmg("ground", 2.0, "fire", "electric", "poison", "rock", "steel");
        setDmg("ground", 0.5, "grass", "bug");
        setDmg("ground", 0.0, "flying");

        // VOLADOR
        setDmg("flying", 2.0, "grass", "fighting", "bug");
        setDmg("flying", 0.5, "electric", "rock", "steel");

        // PSÍQUICO
        setDmg("psychic", 2.0, "fighting", "poison");
        setDmg("psychic", 0.5, "psychic", "steel");
        setDmg("psychic", 0.0, "dark");

        // BICHO
        setDmg("bug", 2.0, "grass", "psychic", "dark");
        setDmg("bug", 0.5, "fire", "fighting", "poison", "flying", "ghost", "steel", "fairy");

        // ROCA
        setDmg("rock", 2.0, "fire", "ice", "flying", "bug");
        setDmg("rock", 0.5, "fighting", "ground", "steel");

        // FANTASMA
        setDmg("ghost", 2.0, "psychic", "ghost");
        setDmg("ghost", 0.5, "dark");
        setDmg("ghost", 0.0, "normal");

        // DRAGÓN
        setDmg("dragon", 2.0, "dragon");
        setDmg("dragon", 0.5, "steel");
        setDmg("dragon", 0.0, "fairy");

        // ACERO
        setDmg("steel", 2.0, "ice", "rock", "fairy");
        setDmg("steel", 0.5, "fire", "water", "electric", "steel");

        // SINIESTRO
        setDmg("dark", 2.0, "psychic", "ghost");
        setDmg("dark", 0.5, "fighting", "dark", "fairy");

        // HADA
        setDmg("fairy", 2.0, "fighting", "dragon", "dark");
        setDmg("fairy", 0.5, "fire", "poison", "steel");
    }

    private void setDmg(String attacker, double mult, String... defenders) {
        for (String defender : defenders) {
            typeChart.get(attacker).put(defender, mult);
        }
    }

    /**
     * Calcula el multiplicador final teniendo en cuenta los dos tipos del defensor.
     */
    public double calculateMultiplier(String moveType, String defType1, String defType2) {
        double m1 = typeChart.getOrDefault(moveType, new HashMap<>()).getOrDefault(defType1, 1.0);
        double m2 = (defType2 != null) ? typeChart.getOrDefault(moveType, new HashMap<>()).getOrDefault(defType2, 1.0) : 1.0;

        return m1 * m2;
    }
}