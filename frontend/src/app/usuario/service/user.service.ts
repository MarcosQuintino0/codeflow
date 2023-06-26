import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { TotalUsers } from 'src/app/bugs/models/count.model';
import { environment } from 'src/environments/environment.prod';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private readonly URL: string = `${environment.api}/user`

  constructor(
    private http: HttpClient
  ) { }

  public register(username: string, email: string, password: string): Observable<User>{
    return this.http.post<User>(`${this.URL}/register`, {
      username: username,
      email: email,
      password: password
    })
  }

  public getCountOfUsers(){
    return this.http.get<TotalUsers>(this.URL)
  }

}
