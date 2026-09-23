import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { RivalComponent } from './components/rival/rival.component';
import { TeamComponent } from './components/team/team.component';
import { PokemonService } from './services/pokemon.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    SidebarComponent,
    RivalComponent,
    TeamComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'poketactics-ui';

  // --- VARIABLES DE ESTADO ---
  rival: any = null;
  consejos: string[] = [];
  pokemonAnalizado: string = ''; 
  cargandoAnalisis: boolean = false; 

  constructor(private pokemonService: PokemonService) {}

  actualizarRival(pokemon: any) {
    this.rival = pokemon;
    this.consejos = [];
    this.pokemonAnalizado = '';
    this.cargandoAnalisis = false;
  }

  analizarCombate(miPokemon: any) {
    if (!this.rival) {
      alert("Primero busca un Pokémon Rival para analizar.");
      return;
    }

    // 1. Iniciamos el estado de carga y guardamos el nombre
    this.cargandoAnalisis = true;
    this.pokemonAnalizado = miPokemon.name;
    this.consejos = []; // Limpiamos los anteriores para que no se mezclen

    // 2. Llamada al servicio
    this.pokemonService.getAnalysis(miPokemon.name, this.rival.name).subscribe({
      next: (data) => {
        this.consejos = data;
        this.cargandoAnalisis = false; // Finaliza la carga con éxito
      },
      error: (err) => {
        console.error("Error en el análisis táctico", err);
        this.cargandoAnalisis = false; // Finaliza la carga incluso si hay error
      }
    });
  }
}