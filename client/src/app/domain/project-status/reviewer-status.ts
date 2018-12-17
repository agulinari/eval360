import { User } from '../user/user';
import { Observable } from 'rxjs';

export class ReviewerStatus {

    id: number;
    idUser: number;
    user: Observable<User>;
    feedbacksAvailable: number;

}
