import { Routes } from '@angular/router';
import { Cadastro } from './cadastro/cadastro';
import { Login } from './login/login';
import { Home } from './home/home';

export const routes: Routes = [
    { path: '', component: Login },
    { path: 'cadastro', component: Cadastro },
    { path: 'home/:id', component: Home }
];
