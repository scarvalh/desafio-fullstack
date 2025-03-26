export class Fornecedor {
  id?: number;
  email: string;
  cep: string;
  tipo: string;
  nome: string;
  cnpj?: string;
  cpf?: string;
  rg?: string;
  dataNascimento?: Date;

  constructor(
    id: number | null,
    email: string,
    cep: string,
    tipo: string,
    nome: string,
    dataNascimento: Date,
    cnpj?: string,
    cpf?: string,
    rg?: string
  ) {
    this.id = id || undefined;
    this.email = email;
    this.cep = cep;
    this.tipo = tipo;
    this.nome = nome;
    this.dataNascimento = dataNascimento;
    this.cnpj = cnpj;
    this.cpf = cpf;
    this.rg = rg;
  }
}