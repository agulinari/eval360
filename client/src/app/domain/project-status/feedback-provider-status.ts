
import { EvalueeDetail } from './evaluee-detail';

export class FeedbackProviderStatus {

    id: number;
    idUser: number;
    username: string;
    mail: string;
    avatar: string;
    status: string;
    pendingFeedbacks: number;
    completedFeedbacks: number;
    evaluees: EvalueeDetail[];
    reminder: boolean;
}
