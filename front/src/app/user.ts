import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class User {

  private readonly URL = 'http://localhost:8080/api/users'

  constructor(private readonly client: HttpClient) { }

  public create(body: any): Observable<any> {
    return this.client.post(this.URL, body);
  }

  public login(body: any): Observable<any> {
    return this.client.post(`${this.URL}/auth`, body);
  }

  public find(id: string): Observable<any> {
    return this.client.get(`${this.URL}/${id}`);
  }

  public start(id: string): Observable<any> {
    return this.client.get(`${this.URL}/start/${id}`);
  }

  public stop(): Observable<any> {
    return this.client.get(`${this.URL}/stop`);
  }

}
