import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateNotificationRememberFeedback } from '../domain/create-notification/create-remember-feedback';
import { CreateRememberUserProvider } from '../domain/create-notification/create-remember-userProvider';
import { ProjectStatus } from '../domain/project-status/project-status';
import { FeedbackProviderStatus } from '../domain/project-status/feedback-provider-status';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  public API: string = environment.serverUrl;
  public NOTIFICATIONS_API = this.API + '/notifications';

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
