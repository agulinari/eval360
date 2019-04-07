import { Authority } from './authority';

export class User {
    id: number;
    username: string;
    password: string;
    passwordReset: boolean;
    mail: string;
    enabled: boolean;
    authorities: Authority[];
}
