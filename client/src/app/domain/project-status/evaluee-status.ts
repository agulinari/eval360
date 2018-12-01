import { User } from '../user';
import { Observable } from 'rxjs';

export class EvalueeStatus {

    id: number;
    user: Observable<User>;
    status: string;
    feedbackRequests: number;
    feedbackResponses: number;
    progress: number;
}
