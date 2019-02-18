import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { CreateNotificationRememberFeedback } from '../domain/create-notification/create-remember-feedback';
import { CreateRememberUserProvider } from '../domain/create-notification/create-remember-userProvider';
import { ProjectStatus } from '../domain/project-status/project-status';
import { FeedbackProviderStatus } from '../domain/project-status/feedback-provider-status';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  public API = '//localhost:8762';
  public NOTIFICATIONS_API = this.API + '/remembers';

  constructor(private http: HttpClient) {
  }

  notificateToProviders(feedbackProviderStatus: FeedbackProviderStatus, projectStatus: ProjectStatus): Observable<any> {

    const notifyRememberFeedback = new CreateNotificationRememberFeedback();
    const createRememberUserProvider = new CreateRememberUserProvider();
    const listRemembers: Array<CreateRememberUserProvider> = [createRememberUserProvider];
    projectStatus.evalueesStatus.forEach(evaluee => {
        if (evaluee.feedbackProviders.find(fp => ((fp.id === feedbackProviderStatus.id) && (fp.status === 'PENDIENTE')) !== undefined)) {
            notifyRememberFeedback.idEvalueeFP = evaluee.id;
        }
    });

    createRememberUserProvider.mail = 'sivori.daniel@gmail.com';
    createRememberUserProvider.username = feedbackProviderStatus.username;
    notifyRememberFeedback.idProject = projectStatus.id;

    notifyRememberFeedback.providers = listRemembers;

    return this.http.post(this.NOTIFICATIONS_API + '/providers', notifyRememberFeedback);

  }

}
