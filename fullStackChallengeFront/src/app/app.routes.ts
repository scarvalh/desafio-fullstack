import { Routes } from '@angular/router';

import { CadastroEmpresaComponent } from './components/cadastro-empresa/cadastro-empresa.component';
import { CadastroFornecedorComponent } from './components/cadastro-fornecedor/cadastro-fornecedor.component';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  {
    path:'',
    redirectTo:'home',
    pathMatch:'full'
  },
  {
    path:'home',
    component: HomeComponent,
    pathMatch:'full'
  },
  {
    path:'cadastro-empresa',
    component: CadastroEmpresaComponent,
    pathMatch:'full'
  },
  {
    path:'editar-empresa/:id',
    component: CadastroEmpresaComponent,
    pathMatch:'full'
  },
  {
    path:'cadastro-fornecedor',
    component: CadastroFornecedorComponent,
    pathMatch:'full'
  },
  {
    path:'editar-fornecedor/:id',
    component: CadastroFornecedorComponent,
    pathMatch:'full'
  }
];
