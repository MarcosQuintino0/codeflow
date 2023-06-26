import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { FormValidationsService } from 'src/app/shared/service/form-validations.service';
import { CustomValidations } from 'src/app/shared/utils/custom-validations';
import { UserService } from '../../service/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  loading: boolean = false

  form = this.fb.group({
    username: ['',[Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirmPassword: ['', [Validators.required, CustomValidations.matchPassword('password')]]
  });

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router,
    private translate: TranslateService,
    private fb: FormBuilder,
    private formValidations: FormValidationsService
    ) { }

  ngOnInit(): void {
  }


  hasError(formControlName: string): boolean {
    return this.formValidations.hasError(this.form, formControlName);
  }

  getMessageError(formControlName: string): string {
    return this.formValidations.getMessageError(this.form, formControlName);
  }

  register(){
    this.loading = true
    this.userService.register(
      this.form.value.username!!,
      this.form.value.email!!,
      this.form.value.password!!
    ).subscribe(() => {
      this.toastr.success(
        this.translate.instant("REGISTER.REGISTERED_SUCCESSFULLY")
      )
      this.router.navigate(['/user/login'])
    }).add(() => this.loading = false)
  }

}
