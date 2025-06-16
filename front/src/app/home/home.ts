import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { User } from '../user';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { delay } from 'rxjs';

@Component({
  selector: 'app-home',
  imports: [
    CommonModule
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit {

  loading = false;
  user: any = null;

  constructor(
    private readonly service: User,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      
      if (!id) return;
  
      this.service.find(id).subscribe(user => {
        this.user = user;
        this.cdr.detectChanges();
      });
    });
  }

  onReciclar() {
    this.loading = true;
    this.service.start(this.user.id)
      .subscribe();
  }

  stopReciclar() {
    this.loading = false;
    this.service.stop()
      .pipe(delay(2000))
      .subscribe(() => window.location.reload());
  }

  get total() {
    if (!this.user?.quantidadeTotal || this.user?.quantidadeTotal == 0) {
      return 0;
    }

    return this.user.quantidadeTotal * 0.07;
  }

}
