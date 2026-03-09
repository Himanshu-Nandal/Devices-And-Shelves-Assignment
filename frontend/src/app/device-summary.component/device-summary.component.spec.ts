import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceSummaryComponent } from './device-summary.component';

describe('DeviceSummaryComponent', () => {
  let component: DeviceSummaryComponent;
  let fixture: ComponentFixture<DeviceSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeviceSummaryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceSummaryComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
