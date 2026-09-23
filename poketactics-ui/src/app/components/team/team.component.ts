import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PokemonService } from '../../services/pokemon.service'; // <--- CORREGIDO: ../../ y .service

@Component({
  selector: 'app-team',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './team.component.html',
  styleUrl: './team.component.css'
})
export class TeamComponent {
  @Input() pokemonActivo: string = ''; 
  @Output() analizarPokemon = new EventEmitter<any>();

  team: any[] = [null, null, null, null, null, null];
  searchNames: string[] = ['', '', '', '', '', ''];

  // Ahora Angular sí reconocerá el token de inyección
  constructor(private pokemonService: PokemonService) {}

  addPokemonToTeam(index: number) {
    const name = this.searchNames[index].trim();
    if (!name) return;

    this.pokemonService.getPokemon(name).subscribe({
      next: (data) => {
        this.team[index] = data;
        this.searchNames[index] = '';
      },
      error: (err) => {
        console.error('Error:', err);
        alert('No se pudo encontrar el Pokémon.');
      }
    });
  }

  solicitarAnalisis(pokemon: any) {
    if (pokemon) {
      this.analizarPokemon.emit(pokemon);
    }
  }

  removePokemon(index: number) {
    this.team[index] = null;
    this.searchNames[index] = '';
  }
}