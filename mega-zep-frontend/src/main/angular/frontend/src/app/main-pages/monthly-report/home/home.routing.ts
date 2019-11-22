import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home.component';
import {ModuleWithProviders} from '@angular/core';


export const homeRoutes: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      pageTitle: 'Monthly Report'
    }
  },
];

export const homeRouting: ModuleWithProviders = RouterModule.forChild(homeRoutes);

