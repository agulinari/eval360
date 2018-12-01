import { EvalueeFeedbackProvider } from './evaluee-feedback-provider';
import { Observable } from 'rxjs';
import { User } from './user';


export class EvalueeProject {
    id: number;
    idUser: number;
    user?: Observable<User>;
    feedbackProviders: EvalueeFeedbackProvider[];
}
