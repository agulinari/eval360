import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  public API: string = environment.serverUrl;
  public REPORTS_API = this.API + '/reports';

  constructor(private http: HttpClient) {
  }

  get(idEvaluee: string): Observable<Blob> {
    return this.http.get(this.REPORTS_API + '/' + idEvaluee, {responseType: 'blob'}).pipe(
      catchError(this.handleError<any>('getReport'))
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
