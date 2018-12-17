import { CreateFeedbackProvider } from './create-feedback-provider';
import { CreateReviewer } from './create-reviewer';

export class CreateEvaluee {
    id: number;
    idUser: number;
    feedbackProviders: CreateFeedbackProvider[];
    reviewers: CreateReviewer[];
}
