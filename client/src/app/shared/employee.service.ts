import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { Employee } from '../domain/employee';
import { catchError } from 'rxjs/operators';

@Injectable()
export class EmployeeService {
  public API = '//localhost:8080';
  public EMPLOYEES_API = this.API + '/employees';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.EMPLOYEES_API).pipe(
      catchError(this.handleError<any>('getEmployees'))
    );
  }

  get(id: string): Observable<any> {
    return this.http.get(this.EMPLOYEES_API + '/' + id).pipe(
      catchError(this.handleError<any>('getEmployee'))
    );
  }

  save(employee: any): Observable<any> {
    let result: Observable<Object>;
    if (employee.id) {
      result = this.http.put(this.EMPLOYEES_API + '/' + employee.id, employee).pipe(
        catchError(this.handleError<any>('putEmployee'))
      );
    } else {
      result = this.http.post(this.EMPLOYEES_API, employee).pipe(
        catchError(this.handleError<any>('postEmployee'))
      );
    }
    return result;
  }

  remove(id: string) {
    return this.http.delete(this.EMPLOYEES_API + '/' + id).pipe(
      catchError(this.handleError<any>('deleteEmployee'))
    );
  }

  searchEmployees(term: string): Observable<Employee[]> {
    if (!term.trim()) {
      return of([]);
    }

    return this.http.get<Employee[]>(this.EMPLOYEES_API + '/findByName?name=' + term).pipe(
      catchError(this.handleError<any>('searchEmployee'))
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
