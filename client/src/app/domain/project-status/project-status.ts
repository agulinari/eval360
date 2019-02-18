import { EvalueeStatus } from './evaluee-status';
import { FeedbackProviderStatus } from './feedback-provider-status';
import { ReviewerStatus } from './reviewer-status';
import { AdminStatus } from './admin-status';

export class ProjectStatus {
    id: number;
    name: string;
    description: string;
    evalueesStatus: EvalueeStatus[];
    feedbackProvidersStatus: FeedbackProviderStatus[];
    reviewersStatus: ReviewerStatus[];
    adminsStatus: AdminStatus[];
    reminder: true;
}
