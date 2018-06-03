import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class EmployeeService {
  public API = '//localhost:8080';
  public EMPLOYEES_API = this.API + '/employees';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.EMPLOYEES_API);
  }

  get(id: string): Observable<any> {
    return this.http.get(this.EMPLOYEES_API + '/' + id);
  }

  save(employee: any): Observable<any> {
    let result: Observable<Object>;
    if (employee.id) {
      result = this.http.put(this.EMPLOYEES_API + '/' + employee.id, employee);
    } else {
      result = this.http.post(this.EMPLOYEES_API, employee);
    }
    return result;
  }

  remove(href: string) {
    return this.http.delete(href);
  }
}
