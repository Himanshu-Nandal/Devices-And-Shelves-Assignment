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
  devicePage = signal(1);
  devicePageSize = signal(6);
  deviceTotalElements = signal(0);
  deviceTotalPages = computed(() => Math.ceil(this.deviceTotalElements() / this.devicePageSize()));
  
  shelves = signal<Shelf[]>([]);
  shelfSearch = '';
  shelfPage = signal(1);
  shelfPageSize = signal(6);
  shelfTotalElements = signal(0);
  shelfTotalPages = computed(() => Math.ceil(this.shelfTotalElements() / this.shelfPageSize()));

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
    this.deviceService.getDevicePage(this.devicePage(), this.devicePageSize(), this.deviceSearch)
    .subscribe({
      next: (page: Page<Device>) => {
        this.devices.set(page.content);
        this.deviceTotalElements.set(page.totalElements);
      },
      error: (err) => {
        console.error('Error loading devices:', err);
      },
      complete: () => console.log("Finished loading devices")
    });
  }
  
  public onDeviceSearch(): void {
    this.devicePage.set(1);
    this.loadDevices();
  }
  
  // public onDevicePageChange(event: PageEvent): void {
    //   this.devicePage.set(event.pageIndex);
    //   this.devicePageSize.set(event.pageSize);
    //   this.loadDevices();
    // }
    
    public onAddDevice(): void {
      this.router.navigate(['/devices/create']);
    }
    
    public onDeviceClick(arg0: String): void {
      console.log('Clicked device with ID:', arg0);
      this.router.navigate(['/devices', arg0]);
    }

    public nextDevicePage() {
      this.devicePage.set(this.devicePage() + 1);
      this.loadDevices();
    }
    
    public prevDevicePage() {
      if (this.devicePage() > 1) {
      this.devicePage.set(this.devicePage() - 1);
      this.loadDevices();
    }
  }


  // Shelf methods
  public loadShelves() {
    this.shelfService.getShelfPage(this.shelfPage(), this.shelfPageSize(), this.shelfSearch)
    .subscribe({
      next: (page: Page<Shelf>) => {
        this.shelves.set(page.content);
        this.shelfTotalElements.set(page.totalElements);
      },
      error: (err) => {
        console.error('Error loading shelves:', err);
      },
      complete: () => console.log("Finished loading shelves")
    });
  }

  public onShelfSearch(): void {
    this.shelfPage.set(1);
    this.loadShelves();
  }

  // public onShelfPageChange(event: PageEvent): void {
  //   this.shelfPage.set(event.pageIndex);
  //   this.shelfPageSize.set(event.pageSize);
  //   this.loadShelves();
  // }

  public onAddShelf(): void {
    this.router.navigate(['/shelves/create']);
  }

  public onShelfClick(arg0: String): void {
    this.router.navigate(['/shelves', arg0]);
  }

  public nextShelfPage() {
    this.shelfPage.set(this.shelfPage() + 1);
    this.loadShelves();
  }

  public prevShelfPage() {
    if (this.shelfPage() > 1) {
      this.shelfPage.set(this.shelfPage() - 1);
      this.loadShelves();
    }
  }
}
