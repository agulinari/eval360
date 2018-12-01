import { Observable } from 'rxjs';
import { User } from './user';

export class FeedbackProviderProject {
    id: number;
    idUser: number;
    user?: Observable<User>;
}
