import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { Component, Inject } from '@angular/core';


@Component({
    selector: 'app-fp-detail-dialog',
    templateUrl: 'fp-detail-dialog.component.html',
    styleUrls: ['fp-detail-dialog.component.css']
})
export class FpDetailDialogComponent {

    constructor(private dialogRef: MatDialogRef<FpDetailDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {

    }

    public close() {
        this.dialogRef.close(0);
    }


}
