import { Component, Output, EventEmitter } from '@angular/core'; // <--- Añadido Output y EventEmitter
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PokemonService } from '../../services/pokemon.service';

@Component({
  selector: 'app-rival',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './rival.component.html',
  styleUrl: './rival.component.css'
})
export class RivalComponent {
  // Megáfono: Avisa al padre que hemos encontrado un rival
  @Output() rivalEncontrado = new EventEmitter<any>();

  searchName: string = '';
  pokemon: any = null;

  constructor(private pokemonService: PokemonService) {}

  buscarRival() {
    if (!this.searchName) return;

    this.pokemonService.getPokemon(this.searchName).subscribe({
      next: (data) => {
        this.pokemon = data;
        // ¡Gritamos al padre! Le enviamos los datos del pokemon encontrado
        this.rivalEncontrado.emit(data);
      },
      error: (err) => {
        console.error('Error al buscar el pokemon:', err);
        alert('No se encontró el Pokémon. Revisa si el nombre es correcto.');
      }
    });
  }
}