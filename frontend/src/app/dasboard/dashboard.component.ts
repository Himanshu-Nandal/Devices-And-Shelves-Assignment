import { Component, computed, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Device, Page } from '../models/device.model';
import { Shelf } from '../models/shelf.model';
import { DeviceService } from '../services/device.service';
import { ShelfService } from '../services/shelf.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  imports: [FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
} as any)
export class DashboardComponent implements OnInit {
  devices = signal<Device[]>([]);
  deviceSearch = '';
  devicePage = signal(0);
  devicePageSize = 6;
  deviceTotalElements = 0;
  deviceTotalPages = computed(() => Math.ceil(this.deviceTotalElements / this.devicePageSize));

  shelves = signal<Shelf[]>([]);
  shelfSearch = '';
  shelfPage = signal(0);
  shelfPageSize = 6;
  shelfTotalElements = 0;
  shelfTotalPages = computed(() => Math.ceil(this.shelfTotalElements / this.shelfPageSize));

  constructor(
    private deviceService: DeviceService,
    private shelfService: ShelfService,
    private router: Router,
  ) {}

  public ngOnInit(): void {
    this.loadDevices();
    this.loadShelves();
  }
  

  // Device methods
  public loadDevices() {
    this.deviceService.getDevices(this.devicePage(), this.devicePageSize, this.deviceSearch)
    .subscribe({
      next: (page: Page<Device>) => {
        this.devices.set(page.content);
        this.deviceTotalElements = page.totalElements;
      },
      error: (err) => {
        console.error('Error loading devices:', err);
      },
      complete: () => console.log("Finished loading devices")
    });
  }

  public onDeviceSearch(): void {
    this.devicePage.set(0);
    this.loadDevices();
  }

  // public onDevicePageChange(event: PageEvent): void {
  //   this.devicePage = event.pageIndex;
  //   this.devicePageSize = event.pageSize;
  //   this.loadDevices();
  // }
  
  public onAddDevice(): void {
    this.router.navigate(['/devices/create']);
  }
  
  public nextDevicePage() {
    this.devicePage.set(this.devicePage() + 1);
    this.loadDevices();
  }

  public prevDevicePage() {
    if (this.devicePage() > 0) {
      this.devicePage.set(this.devicePage() - 1);
      this.loadDevices();
    }
  }


  // Shelf methods
  public loadShelves() {
    this.shelfService.getShelves(this.shelfPage(), this.shelfPageSize, this.shelfSearch)
    .subscribe({
      next: (page: Page<Shelf>) => {
        this.shelves.set(page.content);
        this.shelfTotalElements = page.totalElements;
      },
      error: (err) => {
        console.error('Error loading shelves:', err);
      },
      complete: () => console.log("Finished loading shelves")
    });
  }

  public onShelfSearch(): void {
    this.shelfPage.set(0);
    this.loadShelves();
  }

  // public onShelfPageChange(event: PageEvent): void {
  //   this.shelfPage = event.pageIndex;
  //   this.shelfPageSize = event.pageSize;
  //   this.loadShelves();
  // }

  public onAddShelf(): void {
    this.router.navigate(['/shelves/create']);
  }

  public nextShelfPage() {
    this.shelfPage.set(this.shelfPage() + 1);
    this.loadShelves();
  }

  public prevShelfPage() {
    if (this.shelfPage() > 0) {
      this.shelfPage.set(this.shelfPage() - 1);
      this.loadShelves();
    }
  }
}
