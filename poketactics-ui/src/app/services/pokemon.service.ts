import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PokemonService {
  // La URL de tu API de Spring Boot
  private apiUrl = 'http://localhost:8080/api/pokemon';

  constructor(private http: HttpClient) { }

  // Método para buscar un Pokémon por nombre
  getPokemon(name: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${name}`);
  }

  
  getAnalysis(myPoke: string, rivalPoke: string): Observable<string[]> {
  return this.http.get<string[]>(`${this.apiUrl}/analyze/${myPoke}/${rivalPoke}`);
}
}