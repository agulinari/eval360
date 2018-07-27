import { Injectable } from '@angular/core';
import { of } from 'rxjs/observable/of';
import { User } from '../domain/user';
import { catchError } from 'rxjs/operators';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UserService {

  public API = '//localhost:8762';
  public USERS_API = this.API + '/users';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<User[]> {
    return this.http.get(this.USERS_API)
    .map((result: any) => {
      return result._embedded.users;
    }).pipe(
      catchError(this.handleError<any>('getUsers'))
    );
  }

  get(id: string): Observable<User> {
    return this.http.get(this.USERS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getUser'))
    );
  }

  remove(id: string) {
    return this.http.delete(this.USERS_API + '/' + id).pipe(
      catchError(this.handleError<any>('deleteUser'))
    );
  }

  save(user: any): Observable<any> {
    let result: Observable<Object>;
    if (user.id) {
      result = this.http.put(this.USERS_API + '/' + user.id, user).pipe(
        catchError(this.handleError<any>('putUser'))
      );
    } else {
      result = this.http.post(this.USERS_API, user).pipe(
        catchError(this.handleError<any>('postUser'))
      );
    }
    return result;
  }


  searchUsers(term: string): Observable<User[]> {
    if (!term.trim()) {
      return of([]);
    }

    return this.http.get<User[]>(this.USERS_API + '/search/usernameIgnoreCase?name=' + term)
    .map((result: any) => {
      return result._embedded.users;
    }).pipe(
      catchError(this.handleError<any>('searchUser'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable return
   */
  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);
      // Let the app keep running by returning an empty result
      return of(result as T);

    };
  }

}
