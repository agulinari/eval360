import { User } from '../user/user';
import { Observable } from 'rxjs';

export class FeedbackProviderStatus {

    id: number;
    idUser: number;
    user: Observable<User>;
    status: string;
    feedbacksPending: number;
    feedbacksCompleted: number;
    progress: number;
}
