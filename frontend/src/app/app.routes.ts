import { Routes } from '@angular/router';
import { DashboardComponent } from './dasboard/dashboard.component';
import { DeviceCreateComponent } from './device-create.component/device-create.component';
import { ShelfCreationComponent } from './shelf-creation.component/shelf-creation.component';
import { DeviceSummaryComponent } from './device-summary.component/device-summary.component';
import { ShelfSummaryComponent } from './shelf-summary.component/shelf-summary.component';


export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'devices/create', component: DeviceCreateComponent },
  { path: 'shelves/create', component: ShelfCreationComponent },
  { path: 'devices/:id', component: DeviceSummaryComponent },
  { path: 'shelves/:id', component: ShelfSummaryComponent },
  { path: '**', redirectTo: '' },
];