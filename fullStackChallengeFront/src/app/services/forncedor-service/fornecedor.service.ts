import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Fornecedor } from '../../classes/Fornecedor';

@Injectable({
  providedIn: 'root'
})
export class FornecedorService {

  private apiUrl  = "http://localhost:8080/fornecedor";

  constructor(private httpClient: HttpClient) { }

  public _getAll():Observable<Fornecedor[]>{
    return this.httpClient.get<Fornecedor[]>(this.apiUrl);
  }

  public _create(fornecedor: Fornecedor):Observable<Fornecedor>{
    return this.httpClient.post<Fornecedor>(this.apiUrl, fornecedor);
  }

  public _update(fornecedor: Fornecedor):Observable<Fornecedor>{
    return this.httpClient.put<Fornecedor>(this.apiUrl, fornecedor);
  }

  public _delete(id: number):Observable<void>{
    return this.httpClient.delete<void>(`${this.apiUrl}/delete-by-id/${id}`);
  }

  public _getById(id: number):Observable<Fornecedor>{
    return this.httpClient.get<Fornecedor>(`${this.apiUrl}/find-fornecedor-by-id/${id}`);
  }

}
