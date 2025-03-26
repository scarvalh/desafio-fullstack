import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { ActivatedRoute, Router, RouterModule, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatToolbar, MatButton, RouterOutlet],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(private router: Router){}

  onCadastroEmpresaClick(){
    this.router.navigate(['cadastro-empresa'])
  }

  onHomeClick(){
    this.router.navigate(['home'])
  }

  onCadastroFornecedorClick(){
    this.router.navigate(['cadastro-fornecedor'])
  }

}
