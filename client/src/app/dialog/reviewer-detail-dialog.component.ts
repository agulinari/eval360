import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import { Component, Inject } from '@angular/core';


@Component({
    selector: 'app-reviewer-detail-dialog',
    templateUrl: 'reviewer-detail-dialog.component.html'
})
export class ReviewerDetailDialogComponent {

    constructor(private dialogRef: MatDialogRef<ReviewerDetailDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {

    }

    public close() {
        this.dialogRef.close(0);
    }


}
