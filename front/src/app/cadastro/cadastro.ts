import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cadastro',
  imports: [
    ReactiveFormsModule
  ],
  providers: [
    User
  ],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.scss'
})
export class Cadastro implements OnInit {

  form: FormGroup<any> = new FormGroup({});

  constructor(
    private readonly builder: FormBuilder,
    private readonly userService: User,
    private readonly router: Router
  ) {}


  ngOnInit(): void {

    this.form = this.builder.group({
      nome: ['', Validators.required],
      cpf: ['', Validators.compose([
        Validators.required,
        Validators.maxLength(11),
        Validators.minLength(11),
        ])
      ],
      senha: ['', Validators.required],
    });

  }

  onSubmit() {
    this.userService.create(this.form.value)
      .subscribe(() => this.router.navigate(['']));    
  }

}
