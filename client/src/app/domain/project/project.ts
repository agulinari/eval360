import { Reviewer } from './reviewer';
import { ProjectAdmin } from './project-admin';
import { FeedbackProviderProject } from '../project-status/feedback-provider-project';
import { EvalueeProject } from '../project-status/evaluee-project';

export class Project {
    id: number;
    status: string;
    name: string;
    description: string;
    reviewers: Reviewer[];
    feedbackProviders: FeedbackProviderProject[];
    evaluees: EvalueeProject[];
    projectAdmins: ProjectAdmin[];
}
