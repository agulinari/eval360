import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AuthenticationService } from './authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private authenticationService: AuthenticationService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = `Bearer ${this.authenticationService.getToken()}`;
        // add a custom header
        const customReq = request.clone({
            setHeaders: {
                'Authorization': token
            }
        });

        // pass on the modified request object
        return next.handle(customReq);
    }
}
