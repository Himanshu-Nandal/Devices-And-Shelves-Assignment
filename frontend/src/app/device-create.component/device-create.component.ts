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
  availableShelves = signal<Shelf[]>([]);

  positionCount = signal(0);

  constructor(
    private fb: FormBuilder,
    private deviceService: DeviceService,
    private shelfService: ShelfService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      model: ['', Validators.required],
      totalPositions: [0, [Validators.required, Validators.min(1), Validators.max(50)]],
      positions: this.fb.array([]),
    });

    this.loadAvailableShelves();
  }
  
  get positions(): FormArray {
    return this.form.get('positions') as FormArray;
  }

  public loadAvailableShelves(): void {
    
  }
}
