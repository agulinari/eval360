import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { TemplateList } from '../domain/template/template-list';
import { Evaluation } from '../domain/evaluation/evaluation';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {
  public API = '//localhost:8762';
  public EVALUATIONS_API = this.API + '/evaluations';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Evaluation[]> {
    return this.http.get(this.EVALUATIONS_API).pipe(
      catchError(this.handleError<any>('getAll'))
    );
  }

  get(id: string): Observable<Evaluation> {
    return this.http.get(this.EVALUATIONS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getEvaluation'))
    );
  }

  remove(id: string) {
    return this.http.delete(this.EVALUATIONS_API + '/' + id);
  }

  save(evaluation: Evaluation): Observable<any> {
    let result: Observable<Object>;
    result = this.http.post(this.EVALUATIONS_API, evaluation);

    return result;
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
