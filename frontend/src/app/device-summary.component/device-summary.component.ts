import { Component, OnInit, Signal, signal } from '@angular/core';
import { DeviceService } from '../services/device.service';
import { Device, ShelfPosition } from '../models/device.model';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Shelf } from '../models/shelf.model';
import { ShelfService } from '../services/shelf.service';

@Component({
  selector: 'app-device-summary.component',
  imports: [FormsModule],
  templateUrl: './device-summary.component.html',
  styleUrls: ['./device-summary.component.css'],
} as any)
export class DeviceSummaryComponent implements OnInit {
  device = {} as Device;
  shelfPositions: ShelfPosition[] = [];
  availableShelves: Shelf[] = [];
  deviceSummary: Observable<any> | null = null;
  deviceId: string = "";
  
  constructor(
    private deviceService: DeviceService,
    private shelfService: ShelfService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  public ngOnInit(): void {
    this.deviceId = this.route.snapshot.paramMap.get('id') || '';
    // if (!this.deviceId) {
      //   console.error('Device ID is missing in route parameters');
      //   this.router.navigate(['/']);
      //   return;
      // }
  // if(!this.deviceSummary) {
    //   console.error('Device summary observable is null');
    //   return;
    // }
    this.loadDeviceSummary();
    // this.loadShelfPositions();
  }
      
    // DEVICE METHODS
  public loadDeviceSummary(): void {
    this.deviceSummary = this.deviceService.getDeviceSummary(this.deviceId);
    this.deviceSummary?.subscribe({
      next: (record) => {
        this.device = record.device;
        this.shelfPositions = record.shelfPositions;
        // if (!this.device) {
        //   console.error('Device data is missing in the response');
        //   return;
        // }
        // if (!this.device.deviceName){
        //   console.error('Device name is missing in the response');
        // return;
        // }
        // else {
        // console.log('Loaded device summary for:', this.device.deviceName);
        // }
        this.loadShelves();
      },
      error: (err) => {
      console.error('Failed to load device summary', err);
      },
      complete: () => console.log('Finished loading device summary')
    });
  }
  
  public change: boolean = false;
  
  public onDeviceUpdate(): void {
    // this.deviceService.updateDevice(this.form.deviceId, this.form).subscribe({
    this.deviceService.updateDevice(this.deviceId, this.device).subscribe({
      next: () => {
        this.change = false;
        this.loadDeviceSummary();
      },
      error: (err) => {
        console.error('Failed to update device', err);
      },
      complete: () => console.log('Device update process completed')
    });
  }
  
  public onDeviceDelete(): void {
  if (confirm('Are you sure you want to delete this device?')) {
    // this.deviceService.deleteDevice(this.form.deviceId).subscribe({
      this.deviceService.deleteDevice(this.deviceId).subscribe({
        next: () => {this.router.navigate(['/']);},
        error: (err) => console.error('Failed to delete device', err),
        complete: () => console.log('Device deletion process completed')
      });
    }
  }
  
  public toggleChange() {
    this.change = !this.change;
  }


  // SHELF POSITION METHODS
  // public loadShelfPositions():void {
  //   this.deviceSummary?.subscribe({
  //     next: (record) => {
  //       this.shelfPositions = record.shelfPositions;
  //       // if (!this.shelfPositions) {
  //         //   console.error('Shelf positions data is missing in the response');
  //         //   return;
  //         // }
  //         this.loadShelves();
  //     },
  //     error: (err) => {
  //       console.error('Failed to load device summary', err);
  //     },
  //     complete: () => console.log('Finished loading device summary')
  //   });
  // }
        
  public loadShelves(): void {
    this.shelfService.getShelves().subscribe({
      next: (shelves) => {
        this.availableShelves = shelves;
        // if (!this.availableShelves) {
        //   console.error('Shelf data is missing in the response');
        //   return;
        // }
        // else {
        //   console.log('Loaded shelves for device summary:', this.availableShelves, 'shelves available');
        // }
      },
      error: (err) => {
        console.error('Failed to load shelves', err);
      },
      complete: () => console.log('Finished loading shelves')
    });
  }
  
  editShelf = signal(false);
  
  toggleEditShelf(): void {
    this.editShelf.set(!this.editShelf());
  }
  
  
  public onShelfPositionUpdate(position: ShelfPosition, shelfId: string): void {

    const payload: ShelfPosition = { 
      shelfPositionId: position.shelfPositionId,
      deviceId: position.deviceId,
      shelfId: shelfId,
      shelfName: position.shelfName,
      index: position.index,
      isOccupied: position.isOccupied,
      isDeleted: position.isDeleted,
      createdAt: position.createdAt,
      updatedAt: position.updatedAt
    };
    this.deviceService.updateShelfPosition(payload).subscribe({
      next: () => {this.loadDeviceSummary(); /* this.loadShelfPositions(); */},
      error: (err) => console.error('Failed to update shelf position', err),
      complete: () => console.log('Shelf position update process completed')
    });
  }

  public onShelfPositionDelete(spId: string): void {
    if (confirm('Are you sure you want to delete this shelf position?')) {
      this.deviceService.deleteShelfPosition(spId).subscribe({
        next: () => this.loadDeviceSummary(),
        error: (err) => console.error('Failed to delete shelf position', err),
        complete: () => console.log('Shelf position deletion process completed')
      });
    }
  }


  public goBack(): void {
    this.router.navigate(['/']);
  }
}
