import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Project } from '../domain/project';
import { map, catchError } from 'rxjs/operators';
import { CreateProject } from '../domain/request/create-project';
import { ProjectList } from '../domain/project-list';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  public API = '//localhost:8762';
  public PROJECTS_API = this.API + '/projects';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Project[]> {
    return this.http.get(this.PROJECTS_API)
    .pipe(map((result: any) => {
      return result._embedded.projects;
    }),
      catchError(this.handleError<any>('getAll'))
    );
  }

  get(id: string): Observable<Project> {
    return this.http.get(this.PROJECTS_API + '/' + id).pipe(
      catchError(this.handleError<any>('getProject'))
    );
  }

  remove(id: string) {
    return this.http.delete(this.PROJECTS_API + '/' + id).pipe(
      catchError(this.handleError<any>('deleteProject'))
    );
  }

  createProject(project: CreateProject): Observable<any> {
    const result = this.http.post(this.PROJECTS_API + '/create', project);
    return result;
  }

  save(project: any): Observable<any> {
    let result: Observable<Object>;
    if (project.id) {
      result = this.http.put(this.PROJECTS_API + '/' + project.id, project);
    } else {
      result = this.http.post(this.PROJECTS_API, project);
    }
    return result;
  }

  findActiveProjectsByTemplate(idTemplate: string): Observable<Project[]> {
    return this.http.get(this.PROJECTS_API + '/search/findActiveProjectsByTemplate', {
      params : new HttpParams()
      .set('idTemplate', idTemplate)
    }).pipe(
      map(res => res['_embedded']['projects'])
    );
  }

  findActiveProjectsByUser(idUser: string): Observable<Project[]> {
    return this.http.get(this.PROJECTS_API + '/search/findActiveProjectsByUser', {
      params : new HttpParams()
      .set('idUser', idUser)
    }).pipe(
      map(res => res['_embedded']['projects'])
    );
  }

  find(idUser: string, filter = '', sortOrder = 'id,asc', pageNumber = 0, pageSize = 10 ): Observable<ProjectList> {
      return this.http.get(this.PROJECTS_API + '/search/user', {
        params : new HttpParams()
        .set('idUser', idUser)
        .set('name', filter)
        .set('sort', sortOrder)
        .set('page', pageNumber.toString())
        .set('size', pageSize.toString())
      }).pipe(
        map(res => {
          const projectList = {
            projects: res['_embedded']['projects'],
            total: res['page']['totalElements']
          };
          return projectList;
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
