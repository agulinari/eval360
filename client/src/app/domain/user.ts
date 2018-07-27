import { Employee } from './employee';

export class User {
    id: number;
    username: string;
    password: string;
    enabled: boolean;
    employee: any;
    _links?: {
        employee: {
            href: string;
        }
    };
}
