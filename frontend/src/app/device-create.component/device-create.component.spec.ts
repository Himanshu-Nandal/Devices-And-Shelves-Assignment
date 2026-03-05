import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceCreateComponent } from './device-create.component';

describe('DeviceCreateComponent', () => {
  let component: DeviceCreateComponent;
  let fixture: ComponentFixture<DeviceCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeviceCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
