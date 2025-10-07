import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegistrationRequest} from './models/registration-request.model';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {    }

  register(data: RegistrationRequest): Observable<any> {
    const url = `${this.apiUrl}/register`;
    return this.http.post(url, data);
  }
}
