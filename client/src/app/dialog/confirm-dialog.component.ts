import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';


@Component({
    selector: 'app-confirm-dialog',
    templateUrl: 'confirm-dialog.component.html'
})
export class ConfirmDialogComponent {

    constructor(private dialogRef: MatDialogRef<ConfirmDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any) {

    }

    public cancel() {
        this.dialogRef.close(0);
    }

    public ok() {
        this.dialogRef.close(1);
    }

}
