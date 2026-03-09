import { Component, signal } from '@angular/core';
import { ShelfService } from '../services/shelf.service';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shelf-creation.component',
  imports: [ReactiveFormsModule],
  templateUrl: './shelf-creation.component.html',
  styleUrls: ['./shelf-creation.component.css'],
}as any)
export class ShelfCreationComponent {
  form!: FormGroup;
  positionCount = signal(0);

  constructor(
    private fb: FormBuilder,
    private shelfService: ShelfService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      shelfId: [''],
      shelfName: ['', Validators.required],
      partNumber: ['', Validators.required],
      imageUrl: [''],
      shelfPositionId: [''],
    });

  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      shelfId: this.form.value.shelfId,
      shelfName: this.form.value.shelfName,
      partNumber: this.form.value.partNumber,
      imageUrl: this.form.value.imageUrl,
      shelfPositionId: this.form.value.shelfPositionId,
      createdAt: '', // Backend will set this
      updatedAt: '',  // Backend will set this
    };

    this.shelfService.createShelf(payload).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => console.error('Failed to create shelf', err),
      complete: () => console.log('Shelf creation process completed')
    });
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }
}
