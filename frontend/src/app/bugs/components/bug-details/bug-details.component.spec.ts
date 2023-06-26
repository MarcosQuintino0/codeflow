import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BugDetailsComponent } from './bug-details.component';

describe('BugDetailsComponent', () => {
  let component: BugDetailsComponent;
  let fixture: ComponentFixture<BugDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BugDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BugDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
