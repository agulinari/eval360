import { Item } from './item';

export class Section {
    id: number;
    name: string;
    description: string;
    sectionType: string;
    position: number;
    items: Item[];
}
