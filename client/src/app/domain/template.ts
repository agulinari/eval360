import { Section } from "./section";

export class Template {
    id: number;
    title: string;
    idUser: string;
    createdDate: Date;
    sections: Section[];
}
