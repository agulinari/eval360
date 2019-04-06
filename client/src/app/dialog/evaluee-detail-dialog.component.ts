import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { Component, Inject } from '@angular/core';


@Component({
    selector: 'app-evaluee-detail-dialog',
    templateUrl: 'evaluee-detail-dialog.component.html'
})
export class EvalueeDetailDialogComponent {

    constructor(private dialogRef: MatDialogRef<EvalueeDetailDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {

    }

    public close() {
        this.dialogRef.close(0);
    }


}
