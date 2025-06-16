import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../user';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    User
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login implements OnInit {

  form: FormGroup<any> = new FormGroup({});

  constructor(
    private readonly router: Router,
    private readonly userService: User,
    private readonly builder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.form = this.builder.group({
      cpf: ['', Validators.required],
      senha: ['', Validators.required]
    })
  }

  goCadastro() {
    this.router.navigate(['/cadastro']);
  }

  login() {
    this.userService.login(this.form.value).subscribe((user) => this.router.navigate(['/home', user.id]));
  }

}
