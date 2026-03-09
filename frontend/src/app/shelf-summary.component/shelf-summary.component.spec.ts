import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShelfSummaryComponent } from './shelf-summary.component';

describe('ShelfSummaryComponent', () => {
  let component: ShelfSummaryComponent;
  let fixture: ComponentFixture<ShelfSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShelfSummaryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShelfSummaryComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
