import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/fromPromise';
import { OktaAuthService } from '@okta/okta-angular';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private oktaAuth: OktaAuthService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return Observable.fromPromise(this.handleAccess(request, next));
    }

    private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<HttpEvent<any>> {
        // Solo agregar a dominios conocidos ya que no queremos mandar nuestros toes a cualquiera
        // Ademas, la Api Giphy falla cuando el request incluye un token
        if (request.urlWithParams.indexOf('localhost') > -1) {
            const accessToken = await this.oktaAuth.getAccessToken();
            request = request.clone({
                setHeaders: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        }
        return next.handle(request).toPromise();
    }
}