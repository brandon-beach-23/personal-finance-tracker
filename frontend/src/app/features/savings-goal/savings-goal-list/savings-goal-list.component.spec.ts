import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavingsGoalListComponent } from './savings-goal-list.component';

describe('SavingsGoalListComponent', () => {
  let component: SavingsGoalListComponent;
  let fixture: ComponentFixture<SavingsGoalListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SavingsGoalListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SavingsGoalListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
