import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSavingsGoalModalComponent } from './add-savings-goal-modal.component';

describe('AddSavingsGoalModalComponent', () => {
  let component: AddSavingsGoalModalComponent;
  let fixture: ComponentFixture<AddSavingsGoalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSavingsGoalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddSavingsGoalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
