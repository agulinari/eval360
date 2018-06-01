import { Area } from './area';
import { Position } from './position';
import { Contact } from './contact';
import { Address } from './address';

export class Employee {
    id: number;
    name: string;
    lastname: string;
    birthDate: Date;
    startDate: Date;
    photo: string;
    contactInfo: Contact;
    address: Address;
    area: Area;
    position: Position;
    _links?: {
        self: {
            href: string;
        }
    };
}
