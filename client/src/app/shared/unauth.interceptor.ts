import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/empty';
import { Router } from '@angular/router';

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {
    constructor(
        private router: Router
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).catch((err: any) => {
            if (err instanceof HttpErrorResponse && err.status === 401) {
                this.router.navigate(['/login']);

                // this response is handled
                // stop the chain of handlers by returning empty
                return Observable.empty();
            }

            // rethrow so other error handlers may pick this up
            return Observable.throw(err);
        });
    }

}
