import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Category } from '../models/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private readonly URL: string = `${environment.api}/category`

  constructor(
    private http: HttpClient
  ) { }

  getAll(): Observable<Category[]>{
    return this.http.get<Category[]>(this.URL)
  }
}
