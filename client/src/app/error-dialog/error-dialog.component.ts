import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';


@Component({
    selector: 'dialog-overview',
    templateUrl: 'error-dialog.component.html'
})
export class ErrorDialogComponent {

    constructor(private dialogRef: MatDialogRef<ErrorDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {

    }

    public closeDialog() {
        this.dialogRef.close();
    }

}
