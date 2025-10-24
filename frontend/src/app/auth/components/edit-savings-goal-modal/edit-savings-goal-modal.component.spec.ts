import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSavingsGoalModalComponent } from './edit-savings-goal-modal.component';

describe('EditSavingsGoalModalComponent', () => {
  let component: EditSavingsGoalModalComponent;
  let fixture: ComponentFixture<EditSavingsGoalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditSavingsGoalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditSavingsGoalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
