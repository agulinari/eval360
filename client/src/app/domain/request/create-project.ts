import { CreateEvaluee } from './create-evaluee';
import { CreateAdmin } from './create-admin';

export class CreateProject {
    id: number;
    idTemplate: number;
    name: string;
    description: string;
    admins: CreateAdmin[];
    evaluees: CreateEvaluee[];
}
