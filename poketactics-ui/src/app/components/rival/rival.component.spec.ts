import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RivalComponent } from './rival.component';

describe('RivalComponent', () => {
  let component: RivalComponent;
  let fixture: ComponentFixture<RivalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RivalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RivalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
