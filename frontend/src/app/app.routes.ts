import { Routes } from '@angular/router';
import { DashboardComponent } from './dasboard/dashboard.component';
import { DeviceCreateComponent } from './device-create.component/device-create.component';


export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'devices/create', component: DeviceCreateComponent },
  { path: '**', redirectTo: '' },
];