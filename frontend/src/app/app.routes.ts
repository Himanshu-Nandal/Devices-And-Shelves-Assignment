import { Routes } from '@angular/router';
import { DashboardComponent } from './dasboard/dashboard.component';
import { DeviceCreateComponent } from './device-create.component/device-create.component';
import { ShelfCreationComponent } from './shelf-creation.component/shelf-creation.component';


export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'devices/create', component: DeviceCreateComponent },
  { path: 'shelves/create', component: ShelfCreationComponent },
  { path: '**', redirectTo: '' },
];