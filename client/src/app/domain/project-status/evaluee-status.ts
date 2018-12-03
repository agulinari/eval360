import { User } from '../user';
import { Observable } from 'rxjs';

export class EvalueeStatus {

    id: number;
    idUser: number;
    user: Observable<User>;
    status: string;
    feedbackRequests: number;
    feedbackResponses: number;
    progress: number;
}
