import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateBugComponent } from './update-bug.component';

describe('UpdateBugComponent', () => {
  let component: UpdateBugComponent;
  let fixture: ComponentFixture<UpdateBugComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateBugComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateBugComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
