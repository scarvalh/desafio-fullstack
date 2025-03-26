import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface CepResponse {
  cep: string;
  logradouro: string;
  complemento: string;
  bairro: string;
  localidade: string;
  uf: string;
  ibge: string;
  gia: string;
  ddd: string;
  siafi: string;
}

@Injectable({
  providedIn: 'root'
})
export class CepService {
  private apiUrl = 'https://viacep.com.br/ws';

  constructor(private http: HttpClient) { }

  consultarCep(cep: string): Observable<CepResponse> {
    console.log('Consultando CEP:', cep);
    return this.http.get<CepResponse>(`${this.apiUrl}/${cep}/json/`);
  }
}