import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, tap, throwError} from "rxjs";
import {CategoryResponse} from "./models/category-response.model";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/categories';
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
      }),
      catchError(error => {
        console.error('Error fetching categories', error);
        this.categoriesSubject.next([]);
        return throwError(() => new Error('Category fetch failed'));
      })
    );
  }

  public getCurrentCategories(): CategoryResponse[] {
    return this.categoriesSubject.getValue();
  }
}
