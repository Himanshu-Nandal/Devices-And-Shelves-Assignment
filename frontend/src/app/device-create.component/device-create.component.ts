import { Component, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Shelf } from '../models/shelf.model';
import { DeviceService } from '../services/device.service';
import { ShelfService } from '../services/shelf.service';

@Component({
  selector: 'app-device-create.component',
  imports: [ReactiveFormsModule],
  templateUrl: './device-create.component.html',
  styleUrls: ['./device-create.component.css'],
} as any)
export class DeviceCreateComponent implements OnInit {
  form!: FormGroup;
  positionCount = signal(0);

  constructor(
    private fb: FormBuilder,
    private deviceService: DeviceService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      deviceId: [''],
      deviceName: ['', Validators.required],
      partNumber: ['', Validators.required],
      buildingName: ['', Validators.required],
      deviceType: ['', Validators.required],
      totalShelfPositions: [0, [Validators.required, Validators.min(1), Validators.max(50)]],
      imageUrl: [''],
      
    });

  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      deviceId: this.form.value.deviceId,
      deviceName: this.form.value.deviceName,
      partNumber: this.form.value.partNumber,
      buildingName: this.form.value.buildingName,
      deviceType: this.form.value.deviceType,
      totalShelfPositions: this.form.value.totalShelfPositions,
      imageUrl: this.form.value.imageUrl,
      createdAt: '', // Backend will set this
      updatedAt: ''  // Backend will set this
    };

    this.deviceService.createDevice(payload).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => console.error('Failed to create device', err),
      complete: () => console.log('Device creation process completed')
    });
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }
}
