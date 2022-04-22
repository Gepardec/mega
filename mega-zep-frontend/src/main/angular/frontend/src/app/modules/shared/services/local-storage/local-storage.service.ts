import {Injectable} from '@angular/core';
import {Config} from '../../models/Config';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  private megaConfig = 'MEGA_CONFIG';
  private megaUserStartPage = 'MEGA_USER_STARTPAGE';

  getConfig(): Config {
    return JSON.parse(localStorage.getItem(this.megaConfig));
  }

  saveConfig(config: Config): void {
    localStorage.setItem(this.megaConfig, JSON.stringify(config));
  }

  removeConfig(): void {
    localStorage.removeItem(this.megaConfig);
  }

  getUserStartPage(): string {
    return localStorage.getItem(this.megaUserStartPage);
  }

  saveUserStartPage(userStartPage: string): void {
    localStorage.setItem(this.megaUserStartPage, userStartPage);
  }

  removeUserStartPage(): void {
    localStorage.removeItem(this.megaUserStartPage);
  }
}
