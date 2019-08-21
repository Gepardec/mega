import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MainLayoutService {

  private _toggleSidenav: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);

  constructor() {
  }

  get toggleSidenav(): BehaviorSubject<boolean> {
    return this._toggleSidenav;
  }

  setToggleSidenav(value: boolean) {
    this._toggleSidenav.next(value);
  }
}
