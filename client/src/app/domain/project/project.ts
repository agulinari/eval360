import { Reviewer } from './reviewer';
import { FeedbackProviderProject } from '../project-status/feedback-provider-project';
import { EvalueeProject } from '../project-status/evaluee-project';
import { AdminProject } from '../project-status/admin-project';

export class Project {
    id: number;
    status: string;
    name: string;
    description: string;
    idEvaluationTemplate: number;
    reviewers: Reviewer[];
    feedbackProviders: FeedbackProviderProject[];
    evaluees: EvalueeProject[];
    projectAdmins: AdminProject[];
}
