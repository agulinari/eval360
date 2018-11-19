import { CreateEvaluee } from "./create-evaluee";

export class CreateProject {
    id: number;
    idTemplate: number;
    name: string;
    description: string;
    evaluees: CreateEvaluee[];
}
