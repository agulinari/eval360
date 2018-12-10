import { FeedbackProvider } from './feedback-provider';
import { User } from '../user/user';

export class Evaluee {
    id: number;
    user: User;
    feedbackProviders: FeedbackProvider[];
}