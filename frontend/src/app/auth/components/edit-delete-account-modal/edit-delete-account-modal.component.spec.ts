import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDeleteAccountModalComponent } from './edit-delete-account-modal.component';

describe('EditDeleteAccountModalComponent', () => {
  let component: EditDeleteAccountModalComponent;
  let fixture: ComponentFixture<EditDeleteAccountModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDeleteAccountModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDeleteAccountModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
