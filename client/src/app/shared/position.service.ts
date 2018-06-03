import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PositionService {

  public API = '//localhost:8080';
  public POSITIONS_API = this.API + '/positions';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get(this.POSITIONS_API);
  }

  get(id: string): Observable<any> {
    return this.http.get(this.POSITIONS_API + '/' + id);
  }

}
