
import {throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Token } from './token';
import { map, catchError } from 'rxjs/operators';

import * as jwt_decode from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthenticationService {
  private authUrl: string = environment.serverUrl + '/auth';
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post<Token>(this.authUrl, JSON.stringify({ username: username, password: password }), { headers: this.headers })
      .pipe(map((response) => {
        // login successful if there's a jwt token in the response
        const token = response && response.token;
        if (token) {
          // store username and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify({ username: username, token: token }));

          // return true to indicate successful login
          return true;
        } else {
          // return false to indicate failed login
          return false;
        }
      }), catchError((error: any) => observableThrowError(error.json().error || 'Server error')));
  }

  getToken(): String {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const token = currentUser && currentUser.token;
    return token ? token : '';
  }

  logout(): void {
    // clear token remove user from local storage to log user out
    localStorage.removeItem('currentUser');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token ? true : false;
  }

  getRoles(): String[] {
    const token = this.getToken();
    if (!token) {
      return [];
    }
    const tokenPayload = jwt_decode(token);
    return tokenPayload.authorities;
  }

  getUserId(): string {
    const token = this.getToken();
    if (!token) {
      return '';
    }
    const tokenPayload = jwt_decode(token);
    return tokenPayload.jti;
  }

}
