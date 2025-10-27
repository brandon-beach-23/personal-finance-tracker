import {Component, EventEmitter, inject, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CategoryService} from "../../../services/category.service";
import {CategoryResponse} from "../../../models/category-response.model";
import {CategoryRequest} from "../../../models/category-request.model";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-add-category-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './add-category-modal.component.html',
  styleUrl: './add-category-modal.component.css'
})
export class AddCategoryModalComponent implements OnInit{
  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);
  categoryForm!: FormGroup;

  @Output() closeModal = new EventEmitter<void>;
  @Output() categoryAdded = new EventEmitter<CategoryResponse>();

  responseMessage: string = '';

  ngOnInit() {

    this.categoryForm = this.fb.group({
      categoryName: ['', [Validators.required, Validators.maxLength(50)]]
    });
  }

  onSubmit(): void {

    if(this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched();
      return;
    }

    const formValue = this.categoryForm.value;

    const categoryRequest: CategoryRequest = {
      categoryName: formValue.categoryName
    };

    this.categoryService.createCategory(categoryRequest).subscribe({
      next: (response): void => {
        this.responseMessage = `Category '${response.categoryName}' added!`;
        this.categoryAdded.emit(response);
        setTimeout(() => this.closeModal.emit(), 750);
      },
      error: (error): void => {
        this.responseMessage = 'Error: Category creation failed';
        console.error('Category creation failed', error);
      }
    });
  }

  close(): void {
    this.closeModal.emit();
  }
}
