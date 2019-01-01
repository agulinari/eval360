import { User } from '../user/user';
import { Observable } from 'rxjs';
import { FeedbackProviderDetail } from './feedback-provider-detail';

export class EvalueeStatus {

    id: number;
    idUser: number;
    username: string;
    avatar: string;
    status: string;
    pendingFeedbacks: number;
    completedFeedbacks: number;
    feedbackProviders: FeedbackProviderDetail[];
}
