import { User } from '../user/user';
import { Observable } from 'rxjs';

export class EvalueeItem {

    id: number;
    idUser: number;
    user: Observable<User>;
    relationship: string;

}
