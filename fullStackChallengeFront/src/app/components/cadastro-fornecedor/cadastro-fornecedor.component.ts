import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Fornecedor } from '../../classes/Fornecedor';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { CepService } from '../../services/cep-service/cep.service';
import { cepValidator } from '../../validators/cep.validator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FornecedorService } from '../../services/forncedor-service/fornecedor.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-cadastro-fornecedor',
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    CommonModule,
    HeaderComponent,
    MatDialogModule
  ],
  templateUrl: './cadastro-fornecedor.component.html',
  styleUrl: './cadastro-fornecedor.component.css'
})
export class CadastroFornecedorComponent implements OnInit {
  fornecedorForm: FormGroup;
  isEditMode = false;
  fornecedorId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private fornecedorService: FornecedorService,
    private snackBar: MatSnackBar,
    private cepService: CepService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog
  ) {
    this.fornecedorForm = this.fb.group({
      tipo: ['', Validators.required],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      cep: ['', {
        validators: [Validators.required],
        asyncValidators: [cepValidator(this.cepService)],
        updateOn: 'blur'
      }],
      cnpj: [''],
      cpf: [''],
      rg: [''],
      dataNascimento: ['']
    });
  }

  ngOnInit(): void {
    // Verifica se está em modo de edição
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.fornecedorId = +params['id'];
        this.carregarFornecedor(this.fornecedorId);
      }
    });

    // Adiciona validações condicionais baseadas no tipo de fornecedor
    this.fornecedorForm.get('tipo')?.valueChanges.subscribe(tipo => {
      if (tipo === 'PJ') {
        this.fornecedorForm.get('cnpj')?.setValidators([Validators.required]);
        this.fornecedorForm.get('cpf')?.clearValidators();
        this.fornecedorForm.get('rg')?.clearValidators();
        this.fornecedorForm.get('dataNascimento')?.clearValidators();
      } else if (tipo === 'PF') {
        this.fornecedorForm.get('cpf')?.setValidators([Validators.required]);
        this.fornecedorForm.get('rg')?.setValidators([Validators.required]);
        this.fornecedorForm.get('dataNascimento')?.setValidators([Validators.required]);
        this.fornecedorForm.get('cnpj')?.clearValidators();
      }

      // Atualiza o estado dos validadores
      Object.keys(this.fornecedorForm.controls).forEach(key => {
        const control = this.fornecedorForm.get(key);
        control?.updateValueAndValidity();
      });
    });
  }

  carregarFornecedor(id: number): void {
    this.fornecedorService._getById(id).subscribe(
      (fornecedor: Fornecedor) => {
        this.fornecedorForm.patchValue({
          tipo: fornecedor.tipo === 'Pessoa Juridica' ? 'PJ' : 'PF',
          nome: fornecedor.nome,
          email: fornecedor.email,
          cep: fornecedor.cep,
          cnpj: fornecedor.cnpj,
          cpf: fornecedor.cpf,
          rg: fornecedor.rg,
          dataNascimento: fornecedor.dataNascimento
        });
      },
      error => {
        this.snackBar.open('Erro ao carregar fornecedor', 'Fechar', { duration: 3000 });
        this.router.navigate(['/home']);
      }
    );
  }

  onSubmit(): void {
    if (this.fornecedorForm.valid) {
      const formValue = this.fornecedorForm.value;
      const tipo = formValue.tipo === 'PJ' ? 'Pessoa Juridica' : 'Pessoa Fisica';
      const fornecedor = new Fornecedor(
        this.fornecedorId,
        formValue.email,
        formValue.cep,
        tipo,
        formValue.nome,
        formValue.dataNascimento,
        formValue.cnpj,
        formValue.cpf,
        formValue.rg
      );

      if (this.isEditMode) {
        this.fornecedorService._update(fornecedor).subscribe(
          (fornecedor: Fornecedor) => {
            this.snackBar.open('Fornecedor atualizado com sucesso', 'Fechar', { duration: 3000 });
            this.router.navigate(['/home']);
          },
          (error: Error) => {
            this.snackBar.open('Erro ao atualizar fornecedor: ' + error.message, 'Fechar', { duration: 3000 });
          }
        );
      } else {
        this.fornecedorService._create(fornecedor).subscribe(
          (fornecedor: Fornecedor) => {
            this.snackBar.open('Fornecedor criado com sucesso', 'Fechar', { duration: 3000 });
            this.limparFormulario();
          },
          (error: Error) => {
            this.snackBar.open('Erro ao criar fornecedor: ' + error.message, 'Fechar', { duration: 3000 });
          }
        );
      }
    } else {
      console.log('Formulário inválido:', this.fornecedorForm.errors);
      console.log('Campos com erro:', Object.keys(this.fornecedorForm.controls).filter(key => this.fornecedorForm.get(key)?.errors));
      this.snackBar.open('Por favor, preencha todos os campos obrigatórios corretamente.', 'Fechar', {
        duration: 5000
      });
    }
  }

  excluirFornecedor(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: 'Tem certeza que deseja excluir este fornecedor?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && this.fornecedorId) {
        this.fornecedorService._delete(this.fornecedorId).subscribe(
          () => {
            this.snackBar.open('Fornecedor excluído com sucesso', 'Fechar', { duration: 3000 });
            this.router.navigate(['/home']);
          },
          error => {
            this.snackBar.open('Erro ao excluir fornecedor', 'Fechar', { duration: 3000 });
          }
        );
      }
    });
  }

  getErrorMessage(field: string): string {
    const control = this.fornecedorForm.get(field);
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
    if (control?.hasError('email')) {
      return 'Email inválido';
    }
    if (control?.hasError('cepInvalido')) {
      return 'CEP inválido';
    }
    return '';
  }

  limparFormulario(): void {
    this.fornecedorForm.reset();
  }
}
