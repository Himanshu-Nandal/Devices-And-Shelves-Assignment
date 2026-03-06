import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShelfCreationComponent } from './shelf-creation.component';

describe('ShelfCreationComponent', () => {
  let component: ShelfCreationComponent;
  let fixture: ComponentFixture<ShelfCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShelfCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShelfCreationComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
