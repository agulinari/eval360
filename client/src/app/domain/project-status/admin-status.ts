import { Observable } from 'rxjs';
import { User } from '../user/user';

export class AdminStatus {

    id: number;
    idUser: number;
    user: Observable<User>;
    creator: boolean;
}
