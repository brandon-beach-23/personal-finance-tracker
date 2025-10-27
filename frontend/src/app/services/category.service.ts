import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, tap, throwError} from "rxjs";
import {CategoryResponse} from "../models/category-response.model";
import {catchError} from "rxjs/operators";
import {CategoryRequest} from "../models/category-request.model";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/api/categories`;
  private categoriesSubject = new BehaviorSubject<CategoryResponse[]>([]);
  public categories$ = this.categoriesSubject.asObservable();

  constructor() {
    this.getAllCategories().subscribe();
  }

  public getAllCategories(): Observable<CategoryResponse[]> {
    if (this.categoriesSubject.getValue().length > 0) {
      return this.categories$;
    }

    return this.http.get<CategoryResponse[]>(this.apiUrl).pipe(
      tap(categories => {
        this.categoriesSubject.next(categories);
        console.log('Categories loaded successfully');
        console.log('Service: Categories loaded and Subject updated:', categories);
      }),
      catchError(error => {
        console.error('Error fetching categories', error);
        this.categoriesSubject.next([]);
        return throwError(() => new Error('Category fetch failed')) as Observable<CategoryResponse[]>;
      })
    );
  }

  public getCurrentCategories(): CategoryResponse[] {
    return this.categoriesSubject.getValue();
  }

  public createCategory(categoryRequest: CategoryRequest): Observable<CategoryResponse> {

    const normalizedCategoryRequest: CategoryRequest = {
      ...categoryRequest, categoryName: categoryRequest.categoryName.trim().toUpperCase()
    };

    return this.http.post<CategoryResponse>(this.apiUrl, normalizedCategoryRequest).pipe(
      tap(newCategory => {

        const currentCategories = this.categoriesSubject.getValue();
        this.categoriesSubject.next([...currentCategories, newCategory]);
        console.log('Category created and cached:', newCategory.categoryName);
      }),
      catchError(error => {
        console.error('Error creating category', error);
        return throwError(() => new Error('Category creation failed'));
      })
    );
  }
}
