import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Empresa } from '../../classes/Empresa';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private apiUrl  = "http://localhost:8080/empresa";

  constructor(private httpClient: HttpClient) { }

  public _getAll():Observable<Empresa[]>{
    return this.httpClient.get<Empresa[]>(this.apiUrl);
  }

  public _create(empresa: Empresa):Observable<Empresa>{
    return this.httpClient.post<Empresa>(this.apiUrl, empresa);
  }

  public _update(empresa: Empresa):Observable<Empresa>{
    return this.httpClient.put<Empresa>(this.apiUrl, empresa);
  }

  public _delete(id: number):Observable<void>{
    return this.httpClient.delete<void>(`${this.apiUrl}/delete-by-id/${id}`);
  }

  public _getById(id: number):Observable<Empresa>{
    return this.httpClient.get<Empresa>(`${this.apiUrl}/find-empresa-by-id/${id}`);
  }


}
