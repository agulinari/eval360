import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { HttpResponse, HttpEventType, HttpErrorResponse } from '@angular/common/http';
import { ProjectService } from '../shared/project.service';


@Component({
    selector: 'app-create-project-dialog',
    templateUrl: 'create-project-dialog.component.html'
})
export class CreateProjectDialogComponent {

    selectedFiles: FileList;
    currentFileUpload: File;
    progress: { percentage: number } = { percentage: 0 };

    constructor(private dialogRef: MatDialogRef<CreateProjectDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private projectService: ProjectService) {

    }

    public cancel() {
        const result = { code: 0,
                         message: ''
                        };
        this.dialogRef.close(result);
    }

    public manual() {
        const result = { code: 2,
            message: ''
        };
        this.dialogRef.close(result);
    }

    selectFile(event) {
        this.selectedFiles = event.target.files;
      }

    upload() {
        this.progress.percentage = 0;

        this.currentFileUpload = this.selectedFiles.item(0);
        this.projectService.importProject(this.currentFileUpload).subscribe(event => {
            if (event.type === HttpEventType.UploadProgress) {
                this.progress.percentage = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
                console.log('File is completely uploaded!');
                const result = {  code: 1,
                            message: 'Projecto importado exitosamente'
                        };
                this.dialogRef.close(result);
            }
        }, (error: HttpErrorResponse) => {
            console.log('Error uploading file!');
            const result = {  code: 3,
                message: JSON.parse(error.error)['message']
             };
             this.dialogRef.close(result);
        });

        this.selectedFiles = undefined;
      }

}
