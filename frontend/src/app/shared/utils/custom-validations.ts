import { AbstractControl, ValidatorFn } from "@angular/forms";

export class CustomValidations{

    public static matchPassword(formControlName: string): ValidatorFn {
        return (confirmPasswordControl: AbstractControl) => {
            const passwordControl = confirmPasswordControl.root.get(formControlName);
            if(confirmPasswordControl.value !== passwordControl?.value) {
                return { matchpassword: true };
            }
            return null;
        }
    }
}