import { Fornecedor } from "./Fornecedor";

export class Empresa {
  id?: number;
  nome: string;
  cnpj: string;
  cep: string;
  fornecedores: Fornecedor[];

  constructor(id: number | null, nome: string, cnpj: string, cep: string, fornecedores: Fornecedor[]){
    this.id = id || undefined;
    this.nome = nome;
    this.cnpj = cnpj;
    this.cep = cep;
    this.fornecedores = fornecedores;
  }
}

export class EmpresaTable {
  id: number;
  nome: string;
  cnpj: string;
  cep: string;
  fornecedores: string;

  constructor(empresa: Empresa){
    this.id = empresa.id || 0;
    this.nome = empresa.nome;
    this.cnpj = empresa.cnpj;
    this.cep = empresa.cep;
    this.fornecedores = empresa.fornecedores.map(f => f.nome).join(", ");
  }
}