import { Section } from './section';

export class Evaluation {
    id: string;
    idProject: number;
    idEvaluee: number;
    idFeedbackProvider: number;
    username: string;
    relationship: string;
    sections: Section[];
}
