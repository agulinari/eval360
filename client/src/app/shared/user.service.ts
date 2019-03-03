import { Injectable } from '@angular/core';
import { of ,  Observable } from 'rxjs';
import { User } from '../domain/user/user';
import { map, catchError } from 'rxjs/operators';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { UserList } from '../domain/user/user-list';

@Injectable()
export class UserService {

  public API = '//zuul-360.herokuapp.com';
  public USERS_API = this.API + '/users';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<User[]> {
    return this.http.get(this.USERS_API)
    .pipe(map((result: any) => {
      return result._embedded.users;
    }),
      catchError(this.handleError<any>('getUsers'))
    );
  }

  get(id: string): Observable<User> {
    return this.http.get(this.USERS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getUser'))
    );
  }

  remove(id: string) {
    return this.http.delete(this.USERS_API + '/' + id);
  }

  save(user: any): Observable<any> {
    let result: Observable<Object>;
    if (user.id) {
      result = this.http.put(this.USERS_API + '/' + user.id, user);
    } else {
      result = this.http.post(this.USERS_API, user);
    }
    return result;
  }


  find(filter = '', sortOrder = 'id,asc', pageNumber = 0, pageSize = 10 ): Observable<UserList> {
      return this.http.get(this.USERS_API + '/search/usernameContains', {
        params : new HttpParams()
        .set('name', filter)
        .set('sort', sortOrder)
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
      }).pipe(
        map(res => {
          const userList = {
            users: res['_embedded']['users'],
            total: res['page']['totalElements']
          };
          return userList;
        })
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
