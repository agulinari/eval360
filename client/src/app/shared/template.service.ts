import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, pipe } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Template } from '../domain/template';

@Injectable()
export class TemplateService {

  public API = '//localhost:8762';
  public TEMPLATES_API = this.API + '/evaluationTemplates';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Template[]> {
    return this.http.get(this.TEMPLATES_API).pipe(
      map(res => res['_embedded']['evaluationTemplates'])
    );
  }

  get(id: string): Observable<Template> {
    return this.http.get(this.TEMPLATES_API + '/' + id).pipe(
      catchError(this.handleError<any>('getTemplate'))
    );
  }

  remove(id: string) {
    return this.http.delete(this.TEMPLATES_API + '/' + id);
  }

  save(template: Template): Observable<any> {
    let result: Observable<Object>;
    if (template.id) {
      result = this.http.put(this.TEMPLATES_API + '/' + template.id, template);
    } else {
      result = this.http.post(this.TEMPLATES_API, template);
    }
    return result;
  }

  find(filter = '', sortOrder = 'id,asc', pageNumber = 0, pageSize = 10 ): Observable<Template[]> {

    if (filter !== '') {
      return this.http.get(this.TEMPLATES_API + '/search/titleContains', {
        params : new HttpParams()
        .set('title', filter)
        .set('sort', sortOrder)
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
      }).pipe(
        map(res => res['_embedded']['evaluationTemplates'])
      );
    } else {
      return this.http.get(this.TEMPLATES_API, {
        params : new HttpParams()
        .set('sort', sortOrder)
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
      }).pipe(
        map(res => res['_embedded']['evaluationTemplates'])
      );
    }
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
