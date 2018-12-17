import { FeedbackProvider } from './feedback-provider';
import { User } from '../user/user';
import { Reviewer } from './reviewer';

export class Evaluee {
    id: number;
    user: User;
    feedbackProviders: FeedbackProvider[];
    reviewers: Reviewer[];
}
