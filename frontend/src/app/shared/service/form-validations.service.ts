import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class FormValidationsService {
  constructor(private translate: TranslateService) { }

  hasError(form: FormGroup, formControlName: string): boolean {
    const field = form.get(formControlName);
    return !!(field?.invalid && (field.touched || field.dirty));
  }

  getMessageError(form: FormGroup, formControlName: string): string {
    const field = form.get(formControlName);
    if(field?.hasError('required')) {
      return this.translate.instant("FORM_VALIDATIONS.REQUIRED");
    } else if(field?.hasError('email')) {
      return this.translate.instant("FORM_VALIDATIONS.INVALID_EMAIL");
    } else if(field?.hasError('minlength')) {
      return this.translate.instant("FORM_VALIDATIONS.PASSWORD_MIN_LENGTH", {
        "size": field.getError("minlength").requiredLength,
      });
    } else if(field?.hasError('matchpassword')) {
      return this.translate.instant("FORM_VALIDATIONS.MATCH_PASSWORD");
    } else {
      return "";
    }

  }
}
