
import { EvalueeDetail } from './evaluee-detail';

export class FeedbackProviderStatus {

    id: number;
    idUser: number;
    username: string;
    avatar: string;
    status: string;
    pendingFeedbacks: number;
    completedFeedbacks: number;
    evaluees: EvalueeDetail[];

}
