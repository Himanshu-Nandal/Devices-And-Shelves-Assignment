import {Component, OnInit, signal} from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeviceService } from '../services/device.service';
import { ShelfService } from '../services/shelf.service';
import { Device } from '../models/device.model';
import { Shelf } from '../models/shelf.model';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
} as any)

export class SidebarComponent implements OnInit {
  devices: Device[] = [];
  shelves: Shelf[] = [];

  constructor(
    private deviceService: DeviceService,
    private shelfService: ShelfService
  ) {}

  public ngOnInit(): void {
    this.loadDevices();
    this.loadShelves();
  }

  
  public loadDevices(): void{
    console.log('Loading devices...');
    this.deviceService.getDevices().subscribe({
      next: (record) => {
        console.log(record);
        
        console.log('Received devices data:');
        this.devices = record["devices"] || [];
        if (!this.devices) {
          console.error('Devices data is missing in the response');
          return;
        }
        if (!this.devices){
          console.error('Device name is missing in the response');
          return;
        }
        else {
          console.log('Devices loaded successfully:', this.devices);
        }
      },
      error: (err) => {
        console.error('Error loading devices:', err);
      },
      complete: () => console.log("Finished loading devices")
    }); 
  }
  
  public loadShelves(): void {
    this.shelfService.getShelves().subscribe({
      next: (record) => {
        this.shelves = record;
      },
      error: (err) => {
        console.error('Error loading shelves:', err);
      },
      complete: () => console.log("Finished loading shelves")
    }); 
  }

  
  public devicesOpen = signal(false);
  public shelvesOpen = signal(false);

  public toggleDevices(): void {
    this.devicesOpen.update((v) => !v);
  }

  public toggleShelves(): void {
    this.shelvesOpen.update((v) => !v);
  }
}
