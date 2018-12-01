import { User } from '../user';
import { Observable } from 'rxjs';

export class FeedbackProviderStatus {

    id: number;
    user: Observable<User>;
    status: string;
    feedbacksPending: number;
    feedbacksCompleted: number;
    progress: number;
}
