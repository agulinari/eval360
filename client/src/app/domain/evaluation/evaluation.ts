import { Section } from './section';

export class Evaluation {
    id: string;
    idProject: number;
    idEvaluee: number;
    idFeedbackProvider: number;
    sections: Section[];
}
