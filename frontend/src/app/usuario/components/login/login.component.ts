import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { FormValidationsService } from 'src/app/shared/service/form-validations.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loading: boolean = false;

  form = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private translate: TranslateService,
    private fb: FormBuilder,
    private formValidations: FormValidationsService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  hasError(formControlName: string): boolean {
    return this.formValidations.hasError(this.form, formControlName);
  }

  getMessageError(formControlName: string): string {
    return this.formValidations.getMessageError(this.form, formControlName);
  }

  login(){
    this.loading = true;
    this.authService.login(
      this.form.value.username!!,
      this.form.value.password!!
    ).subscribe(() => {
      this.toastr.success(
        this.translate.instant("LOGIN.AUTHENTICATED_SUCESSFULLY")
      )
      this.router.navigate(["/bugs"])
    }).add(() => this.loading = false)
  }

}
