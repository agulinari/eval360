import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { CompletedEvaluee } from '../domain/evaluation-list/completed-evaluee';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material';
import { AuthenticationService } from '../shared/authentication.service';
import { ReportService } from '../shared/report.service';
import { ProjectService } from '../shared/project.service';
import { ErrorDialogComponent } from '../error-dialog/error-dialog.component';

@Component({
  selector: 'app-report-list',
  templateUrl: './report-list.component.html',
  styleUrls: ['./report-list.component.css']
})
export class ReportListComponent implements OnInit, OnDestroy {

  sub: Subscription;
  projectId;
  completedEvaluees: CompletedEvaluee[] = [];
  loading = false;

  constructor( private route: ActivatedRoute,
    private router: Router,
    public dialog: MatDialog,
    private authenticationService: AuthenticationService,
    private reportService: ReportService,
    private projectService: ProjectService) { }

  ngOnInit() {

    this.sub = this.route.parent.params.subscribe(params => {
      this.projectId = params['id'];
      if (this.projectId ) {
        const userId = this.authenticationService.getUserId();
        this.getCompletedEvaluees(this.projectId , userId);
      }
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  getCompletedEvaluees(id, userId) {
    this.loading = true;
    this.completedEvaluees = [];

    this.projectService.getCompletedEvaluees(id, userId).subscribe((completedEvaluees: CompletedEvaluee[]) => {
      this.completedEvaluees = completedEvaluees;
    },
    err => {
      console.log('Error obteniendo evaluaciones finalizadas', err);
      this.showError('Se produjo un error al obtener evaluaciones finalizadas');
      this.gotoList();
    },
    () => {
      this.loading = false;
    });
  }

  downloadReport(evalueeId) {
    this.reportService.get(evalueeId).subscribe(res => {
      console.log(res);
      const newBlob = new Blob([res], { type: 'application/pdf' });

      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(newBlob);
        return;
    }
     // For other browsers:
            // Create a link pointing to the ObjectURL containing the blob.
            const data = window.URL.createObjectURL(newBlob);

            const link = document.createElement('a');
            link.href = data;
            link.download = 'report.pdf';
            // this is necessary as link.click() does not work on the latest firefox
            link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

            setTimeout(function () {
                // For Firefox it is necessary to delay revoking the ObjectURL
                window.URL.revokeObjectURL(data);
            }, 100);

    }, error => {
      console.log(error);
    });
  }

  showError(error: string): void {
    this.dialog.open(ErrorDialogComponent, {
      data: {errorMsg: error}, width: '250px'
    });
  }

  gotoList() {
    this.router.navigate(['/main/project-list']);
  }

}
