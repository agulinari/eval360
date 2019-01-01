
import { EvalueeDetail } from './evaluee-detail';

export class ReviewerStatus {

    id: number;
    idUser: number;
    username: string;
    avatar: string;
    pendingReports: number;
    completedReports: number;
    evaluees: EvalueeDetail[];
}
