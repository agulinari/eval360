import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AreaService {

  public API = '//localhost:8080';
  public AREAS_API = this.API + '/areas';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.AREAS_API);
  }

  get(id: string): Observable<any> {
    return this.http.get(this.AREAS_API + '/' + id);
  }

}
