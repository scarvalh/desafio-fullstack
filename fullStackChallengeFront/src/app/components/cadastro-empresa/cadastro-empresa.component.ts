import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Empresa } from '../../classes/Empresa';
import { Fornecedor } from '../../classes/Fornecedor';
import { FornecedorService } from '../../services/forncedor-service/fornecedor.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { CepService } from '../../services/cep-service/cep.service';
import { cepValidator } from '../../validators/cep.validator';
import { EmpresaService } from '../../services/empresa-service/empresa.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-cadastro-empresa',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule,
    CommonModule,
    HeaderComponent,
    MatDialogModule
  ],
  templateUrl: './cadastro-empresa.component.html',
  styleUrl: './cadastro-empresa.component.css'
})
export class CadastroEmpresaComponent implements OnInit {
  empresaForm: FormGroup;
  fornecedores: Fornecedor[] = [];
  isEditMode = false;
  empresaId: number | null = null;

  constructor(
    private cepService: CepService,
    private fb: FormBuilder,
    private fornecedorService: FornecedorService,
    private empresaService: EmpresaService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog
  ) {
    this.empresaForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      cnpj: ['', [Validators.required]],
      cep: ['', {
        validators: [Validators.required],
        asyncValidators: [cepValidator(this.cepService)],
        updateOn: 'blur'
      }],
      fornecedores: [[]]
    });
  }

  ngOnInit(): void {
    this.carregarFornecedores();

    // Verifica se está em modo de edição
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.empresaId = +params['id'];
        this.carregarEmpresa(this.empresaId);
      }
    });
  }

  carregarFornecedores(): void {
    this.fornecedorService._getAll().subscribe(
      (fornecedores: Fornecedor[]) => {
        this.fornecedores = fornecedores;
      },
      (error: Error) => {
        console.error('Erro ao carregar fornecedores:', error);
      }
    );
  }

  carregarEmpresa(id: number): void {
    this.empresaService._getById(id).subscribe(
      (empresa: Empresa) => {
        this.empresaForm.patchValue({
          nome: empresa.nome,
          cnpj: empresa.cnpj,
          cep: empresa.cep,
          fornecedores: empresa.fornecedores
        });
      },
      error => {
        this.snackBar.open('Erro ao carregar empresa', 'Fechar', { duration: 3000 });
        this.router.navigate(['/home']);
      }
    );
  }

  onSubmit(): void {
    if (this.empresaForm.valid) {
      const formValue = this.empresaForm.value;
      const empresa = new Empresa(
        this.empresaId,
        formValue.nome,
        formValue.cnpj,
        formValue.cep,
        formValue.fornecedores || []
      );

      if (this.isEditMode) {
        this.empresaService._update(empresa).subscribe(
          (empresa: Empresa) => {
            this.snackBar.open('Empresa atualizada com sucesso', 'Fechar', { duration: 3000 });
            this.router.navigate(['/home']);
          },
          (error: Error) => {
            this.snackBar.open('Erro ao atualizar empresa: ' + error.message, 'Fechar', { duration: 3000 });
          }
        );
      } else {
        this.empresaService._create(empresa).subscribe(
          (empresa: Empresa) => {
            this.snackBar.open('Empresa criada com sucesso', 'Fechar', { duration: 3000 });
            this.limparFormulario();
          },
          (error: Error) => {
            this.snackBar.open('Erro ao criar empresa: ' + error.message, 'Fechar', { duration: 3000 });
          }
        );
      }
    } else {
      console.log('Formulário inválido:', this.empresaForm.errors);
      console.log('Campos com erro:', Object.keys(this.empresaForm.controls).filter(key => this.empresaForm.get(key)?.errors));
      this.snackBar.open('Por favor, preencha todos os campos obrigatórios corretamente.', 'Fechar', {
        duration: 5000
      });
    }
  }

  excluirEmpresa(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: 'Tem certeza que deseja excluir esta empresa?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.empresaId) {
        this.empresaService._delete(this.empresaId).subscribe(
          () => {
            this.snackBar.open('Empresa excluída com sucesso', 'Fechar', { duration: 3000 });
            this.router.navigate(['/home']);
          },
          error => {
            this.snackBar.open('Erro ao excluir empresa', 'Fechar', { duration: 3000 });
          }
        );
      }
    });
  }

  limparFormulario(): void {
    this.empresaForm.reset();
  }

  getErrorMessage(field: string): string {
    const control = this.empresaForm.get(field);
    if (control?.hasError('required')) {
      return 'Este campo é obrigatório';
    }
    if (control?.hasError('minlength')) {
      return 'O nome deve ter pelo menos 3 caracteres';
    }
    if (control?.hasError('pattern')) {
      if (field === 'cnpj') {
        return 'CNPJ inválido';
      }
    }
    if (control?.hasError('cepInvalido')) {
      return 'CEP inválido';
    }
    return '';
  }
}
