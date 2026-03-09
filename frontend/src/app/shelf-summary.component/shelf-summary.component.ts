import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ShelfPosition } from '../models/device.model';
import { Shelf } from '../models/shelf.model';
import { DeviceService } from '../services/device.service';
import { ShelfService } from '../services/shelf.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-shelf-summary.component',
  imports: [FormsModule],
  templateUrl: './shelf-summary.component.html',
  styleUrls: ['./shelf-summary.component.css'],
} as any)
export class ShelfSummaryComponent {
  shelf = {} as Shelf;
  shelfId: string = "";
  shelfSummary: Observable<any> | null = null;
  // shelfPositions: ShelfPosition[] = [];
  // shelfposition: ShelfPosition = {} as ShelfPosition;

  constructor(
    private deviceService: DeviceService,
    private shelfService: ShelfService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  public ngOnInit(): void {
    this.shelfId = this.route.snapshot.paramMap.get('id') || '';
    if (!this.shelfId) {
      console.error('Shelf ID is missing in route parameters');
      this.router.navigate(['/']);
      return;
    }
    this.shelfSummary = this.shelfService.getShelf(this.shelfId);
    if(!this.shelfSummary) {
      console.error('Shelf summary observable is null');
      return;
    }
    this.loadShelfSummary();
    // this.loadShelfPositions();
  }
  
  // SHELF METHODS
  public loadShelfSummary(): void {
    this.shelfSummary?.subscribe({
      next: (record) => {
        this.shelf = record.content;
        if (!this.shelf) {
          console.error('Shelf data is missing in the response');
          return;
        }
        if (!this.shelf.shelfName){
          console.error('Shelf name is missing in the response');
          return;
        }
        else {
          console.log('Loaded shelf summary for:', this.shelf.shelfName);
        }
      },
      error: (err) => {
        console.error('Failed to load shelf summary', err);
      },
      complete: () => console.log('Finished loading shelf summary')
    });
  }
  
  public change: boolean = false;

  public onShelfUpdate(): void {
    // this.deviceService.updateDevice(this.form.deviceId, this.form).subscribe({
    this.shelfService.updateShelf(this.shelfId, this.shelf).subscribe({
      next: () => {
        this.change = false;
        this.loadShelfSummary();
      },
      error: (err) => {
        console.error('Failed to update shelf', err);
      },
      complete: () => console.log('Shelf update process completed')
    });
  }
  
  public onShelfDelete(): void {
    if (confirm('Are you sure you want to delete this shelf?')) {
      // this.deviceService.deleteDevice(this.form.deviceId).subscribe({
        this.shelfService.deleteShelf(this.shelfId).subscribe({
          next: () => {this.router.navigate(['/']);},
          error: (err) => console.error('Failed to delete shelf', err),
        complete: () => console.log('Shelf deletion process completed')
      });
    }
  }
  
  public toggleChange() {
    this.change = !this.change;
  }
  // SHELF POSITION METHODS
  // public onShelfPositionUpdate(spId: String, shelfId: string): void {
  //   this.shelfService.updateShelfPosition(spId, shelfId).subscribe({
  //     next: () => this.loadShelfPositions(),
  //     error: (err) => console.error('Failed to update shelf position', err),
  //     complete: () => console.log('Shelf position update process completed')
  //   });
  // }

  // public onShelfPositionDelete(spId: String): void {
  //   if (confirm('Are you sure you want to delete this shelf position?')) {
  //     this.shelfService.deleteShelfPosition(spId).subscribe({
  //       next: () => this.loadShelfPositions(),
  //       error: (err) => console.error('Failed to delete shelf position', err),
  //       complete: () => console.log('Shelf position deletion process completed')
  //     });
  //   }
  // }


  public goBack(): void {
    this.router.navigate(['/']);
  }
}
