import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDeleteTransactionModalComponent } from './edit-delete-transaction-modal.component';

describe('EditDeleteTransactionModalComponent', () => {
  let component: EditDeleteTransactionModalComponent;
  let fixture: ComponentFixture<EditDeleteTransactionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDeleteTransactionModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDeleteTransactionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
