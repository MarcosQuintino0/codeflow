import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorsService implements HttpInterceptor{

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError((response: HttpErrorResponse) => {

      if(response.error && response.error.message && response.status != 400) {
        this.toastr.error(response.error.message);
      }

      if(response.error && response.error.errors){
        (response.error.errors as string[]).forEach(error => {
          this.toastr.error(error);
        })
      }

      return throwError(() => response)
    }))
  }
}
