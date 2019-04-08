import { Inject, Injectable } from '@angular/core';
import { SESSION_STORAGE, StorageService } from 'angular-webstorage-service';
 
@Injectable()
export class LocalStorageService {
 
    constructor(@Inject(SESSION_STORAGE) private storage: StorageService) {
 
    }
 
    public setItem(keyItem:string,valueItem:any): void {
        this.storage.set(keyItem, valueItem);
    }

    public getItem(keyItem:string):any{
        return this.storage.get(keyItem);
    }
}