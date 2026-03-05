import {Component, signal} from '@angular/core';
import { CommonModule } from '@angular/common';

interface SidebarItem {
  name: string;
  // icon: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
} as any)

export class SidebarComponent {
  public devicesOpen = signal(false);
  public shelvesOpen = signal(false);

  public devices = signal<SidebarItem[]>([
    { name: 'Choose a Device' }
  ]);

  public shelves = signal<SidebarItem[]>([
    { name: 'Choose a Shelf' }
  ]);

  public toggleDevices(): void {
    this.devicesOpen.update((v) => !v);
  }

  public toggleShelves(): void {
    this.shelvesOpen.update((v) => !v);
  }
}
