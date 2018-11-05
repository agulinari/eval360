import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable ,  of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class PositionService {

  public API = '//localhost:8762';
  public POSITIONS_API = this.API + '/positions';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.POSITIONS_API).pipe(
      catchError(this.handleError<any>('getPositions'))
    );
  }

  get(id: string): Observable<any> {
    return this.http.get(this.POSITIONS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getPosition'))
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
