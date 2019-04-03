import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { HttpClient,HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { StatisticSp } from '../domain/statistics-status-project/statistics-sp';
import { StatisticSpSection } from '../domain/statistics-status-project/statistics-sp-section';
import { StatisticSpItem } from '../domain/statistics-status-project/statistics-sp-item';
import { StatisticSpPoint } from '../domain/statistics-status-project/statistics-sp-point';
import { StatisticSpEvaluee } from '../domain/statistics-status-project/statistics-sp-evaluee';

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

  getStatisticProject(projectId: string, idTemplate: string): Observable<StatisticSp> {
       
    return this.http.get(this.REPORTS_API + '/statistics-project',{
      params : new HttpParams()
      .set('idProject', projectId)
      .set('idEvaluationTemplate', idTemplate)
    }).pipe(
      catchError(this.handleError<any>('getStatisticProject'))
    );
  }

loadStatisticsSps(): StatisticSp {
    let statisticSp = new StatisticSp();
    statisticSp.nameProject = 'Nombre proyecto';

    /* EVALUEES */
    let stSpEvFM = this.getSpEvaluee(1, "Fernando Martinez");
    let stSpEvES = this.getSpEvaluee(2, "Estefania Segovia");
    let stSpEvJP = this.getSpEvaluee(3, "Juan Perez");
    let stSpEvJB = this.getSpEvaluee(4, "José Bonifacio");
    /* SECTION 1 */
    let stSpSec1 = new StatisticSpSection();
    stSpSec1.name = 'Planificación';

    let stSpSec1Item1 = this.getItemsEvalueePoints(1, 3, 2, 5, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        1, "Estableciendo metas", "Estableciendo metas");
    let stSpSec1Item2 = this.getItemsEvalueePoints(2, 1, 6, 2, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        2, "Desglosando tareas", "Desglosando tareas");
    let stSpSec1Item3 = this.getItemsEvalueePoints(3, 4, 2, 3, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        3, "Priorizando", "Priorizando");
    let stSpSec1Item4 = this.getItemsEvalueePoints(4, 5, 2, 2, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        4, "Admin el tiempo", "Admin el tiempo");

    stSpSec1.statisticSpItems = Array.of<StatisticSpItem>(stSpSec1Item1, stSpSec1Item2, stSpSec1Item3, stSpSec1Item4);

    /* SECTION 2 */
    let stSpSec2 = new StatisticSpSection();
    stSpSec2.name = 'Cumplimiento';

    let stSpSec2Item1 = this.getItemsEvalueePoints(1, 2, 4, 5, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        1, "Siendo productivo", "Siendo productivo");
    let stSpSec2Item2 = this.getItemsEvalueePoints(1, 2, 5, 2, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        2, "Comunic progreso", "Comunic progreso");
    let stSpSec2Item3 = this.getItemsEvalueePoints(1, 2, 3, 3, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        3, "Ident y resolv problemas", "Ident y resolv problemas");
    let stSpSec2Item4 = this.getItemsEvalueePoints(3, 4, 1, 5, stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB,
        4, "Comp tecnica", "Comp tecnica");

    stSpSec2.statisticSpItems = Array.of<StatisticSpItem>(stSpSec2Item1, stSpSec2Item2, stSpSec2Item3, stSpSec2Item4);

    /* SP STATUS */
    let spStatus = new StatisticSp();
    spStatus.nameProject = "Proyecto Prueba";
    spStatus.statisticsSpSections = Array.of<StatisticSpSection>(stSpSec1, stSpSec2);
    spStatus.statisticsSpEvaluees = Array.of<StatisticSpEvaluee>(stSpEvFM, stSpEvES, stSpEvJP, stSpEvJB);
    return spStatus;
}

getItemsEvalueePoints(pointA: number, pointB: number, pointC: number, pointD: number,
    evalA: StatisticSpEvaluee, evalB: StatisticSpEvaluee, evalC: StatisticSpEvaluee,
    evalD: StatisticSpEvaluee, idItem: number, descrItem: string, titleItem: string): StatisticSpItem {

    /* POINTS SECTION X ITEM X */
    let stSpPtXItXFM = this.getSpPoint(pointA, evalA);
    let stSpPtXItXES = this.getSpPoint(pointB, evalB);
    let stSpPtXItXJP = this.getSpPoint(pointC, evalC);
    let stSpPtXItXJB = this.getSpPoint(pointD, evalD);
    let apoint = new Array(stSpPtXItXFM, stSpPtXItXES, stSpPtXItXJP, stSpPtXItXJB);
    /* ITEMS SECTION X */
    let stSpIt1 = this.getSpItem(idItem, descrItem, titleItem, apoint);

    return stSpIt1;
}

getSpEvaluee(id: number, name: string): StatisticSpEvaluee {
    let statisticSpEvaluee = new StatisticSpEvaluee();
    statisticSpEvaluee.id = id;
    statisticSpEvaluee.name = name;
    return statisticSpEvaluee;
}

getSpPoint(point: number, statisticsSpEvaluees: StatisticSpEvaluee): StatisticSpPoint {
    let statisticSpPoint = new StatisticSpPoint();
    statisticSpPoint.point = point;
    statisticSpPoint.statisticSpEvaluee = statisticsSpEvaluees;
    return statisticSpPoint;
}

getSpItem(id: number, description: string, title: string, points: Array<StatisticSpPoint>): StatisticSpItem {
    let statisticSpItem = new StatisticSpItem();
    statisticSpItem.id = id;
    statisticSpItem.description = description;
    statisticSpItem.title = title;
    statisticSpItem.points = points;
    return statisticSpItem;
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
