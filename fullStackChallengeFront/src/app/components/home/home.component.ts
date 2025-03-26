import { FornecedorService } from './../../services/forncedor-service/fornecedor.service';
import { Component, OnInit, signal } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { MatAccordion, MatExpansionPanel, MatExpansionPanelHeader, MatExpansionPanelTitle } from '@angular/material/expansion';
import { MatTable, MatTableModule } from '@angular/material/table';
import { Empresa, EmpresaTable } from '../../classes/Empresa';
import { Fornecedor } from '../../classes/Fornecedor';
import { EmpresaService } from '../../services/empresa-service/empresa.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { distinctUntilChanged } from 'rxjs/operators';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    HeaderComponent,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionPanelHeader,
    MatTableModule,
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  readonly isOpenEmpresa = signal(false);
  readonly isOpenForncedor = signal(false);

  displayedColumnsEmpresa: string[] = ['nome', 'cnpj', 'cep', 'fornecedores', 'acoes'];
  displayedColumnsFornecedor: string[] = ['nome', 'email', 'cep', 'tipo', 'cpf', 'cnpj', 'dataNascimento', 'rg', 'acoes'];
  empresas: EmpresaTable[] = [];
  fornecedores: Fornecedor[] = [];
  fornecedoresFiltrados: Fornecedor[] = [];
  filterForm: FormGroup;

  // Opções para os selects
  nomes: string[] = [];
  cpfs: string[] = [];
  cnpjs: string[] = [];

  constructor(
    private fornecedorService: FornecedorService, 
    private empresaService: EmpresaService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.filterForm = this.fb.group({
      nome: [['']],
      cpf: [['']],
      cnpj: [['']]
    });
  }

  ngOnInit(): void {
    this.setupFiltros();
  }

  private setupFiltros(): void {
    this.filterForm.get('nome')?.valueChanges.pipe(
      distinctUntilChanged()
    ).subscribe(() => {
      this.aplicarFiltros();
    });

    this.filterForm.get('cpf')?.valueChanges.pipe(
      distinctUntilChanged()
    ).subscribe(() => {
      this.aplicarFiltros();
    });

    this.filterForm.get('cnpj')?.valueChanges.pipe(
      distinctUntilChanged()
    ).subscribe(() => {
      this.aplicarFiltros();
    });
  }

  private aplicarFiltros(): void {
    const nome = this.filterForm.get('nome')?.value || '';
    const cpf = this.filterForm.get('cpf')?.value || '';
    const cnpj = this.filterForm.get('cnpj')?.value || '';

    this.fornecedoresFiltrados = this.fornecedores.filter(fornecedor => {
      const nomeMatch = !nome || fornecedor.nome === nome;
      const cpfMatch = !cpf || fornecedor.cpf === cpf;
      const cnpjMatch = !cnpj || fornecedor.cnpj === cnpj;
      
      return nomeMatch && cpfMatch && cnpjMatch;
    });
  }

  private atualizarOpcoesSelects(): void {
    // Atualiza as opções dos selects com valores únicos da tabela
    this.nomes = [...new Set(this.fornecedores.map(f => f.nome))].sort();
    this.cpfs = [...new Set(this.fornecedores.map(f => f.cpf).filter((cpf): cpf is string => !!cpf))].sort();
    this.cnpjs = [...new Set(this.fornecedores.map(f => f.cnpj).filter((cnpj): cnpj is string => !!cnpj))].sort();
  }

  onOpenEmpresaPanel() {
    this.isOpenEmpresa.set(true)
    this.getEmpresas();
  }

  oncloseEmpresaPanel() {
    this.isOpenEmpresa.set(false);
    this.empresas = [];
  }

  onOpenFornecedorPanel() {
    this.isOpenForncedor.set(true)
    this.getFornecedores();
  }

  oncloseFornecedorPanel() {
    this.isOpenForncedor.set(false);
    this.fornecedores = [];
    this.fornecedoresFiltrados = [];
    this.nomes = [];
    this.cpfs = [];
    this.cnpjs = [];
  }

  getFornecedores(): void {
    this.fornecedorService._getAll().subscribe((resul) => {
      this.fornecedores = resul;
      this.fornecedoresFiltrados = resul;
      this.atualizarOpcoesSelects();
    })
  }

  getEmpresas(): void {
    this.empresaService._getAll().subscribe((resul) => {
      this.empresas = resul.map(e => new EmpresaTable(e));
    })
  }

  excluirFornecedor(fornecedor: Fornecedor): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: `Tem certeza que deseja excluir o fornecedor ${fornecedor.nome}?` }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && fornecedor.id) {
        this.fornecedorService._delete(fornecedor.id).subscribe(
          () => {
            this.snackBar.open('Fornecedor excluído com sucesso', 'Fechar', { duration: 3000 });
            this.getFornecedores();
          },
          error => {
            this.snackBar.open('Erro ao excluir fornecedor', 'Fechar', { duration: 3000 });
          }
        );
      }
    });
  }

  excluirEmpresa(empresa: Empresa): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: `Tem certeza que deseja excluir a empresa ${empresa.nome}?` }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && empresa.id) {
        this.empresaService._delete(empresa.id).subscribe(
          () => {
            this.snackBar.open('Empresa excluída com sucesso', 'Fechar', { duration: 3000 });
            this.getEmpresas();
          },
          error => {
            this.snackBar.open('Erro ao excluir empresa', 'Fechar', { duration: 3000 });
          }
        );
      }
    });
  }
}
