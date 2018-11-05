import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable ,  of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class AreaService {

  public API = '//localhost:8762';
  public AREAS_API = this.API + '/areas';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.AREAS_API).pipe(
      catchError(this.handleError<any>('getEmployees'))
    );
  }

  get(id: string): Observable<any> {
    return this.http.get(this.AREAS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getEmployees'))
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
