import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { ToastrService } from 'ngx-toastr';
import { BugsService } from 'src/app/bugs/service/bugs/bugs.service';
import { CategoryService } from 'src/app/shared/service/category.service';
import { FormValidationsService } from 'src/app/shared/service/form-validations.service';
import { Bug } from '../../models/bug.model';

@Component({
  selector: 'app-update-bug',
  templateUrl: './update-bug.component.html',
  styleUrls: ['./update-bug.component.css']
})
export class UpdateBugComponent implements OnInit{
  
  categoriesDropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'id',
    textField: 'name',
    enableCheckAll: false,
    itemsShowLimit: 6,
    searchPlaceholderText: this.translate.instant("CATEGORY.SEARCH"),
    noDataAvailablePlaceholderText: this.translate.instant("CATEGORY.NOT-FOUND"),
    allowSearchFilter: true
  };

  bugId = 0;
  bugDetail = <Bug>{};
  categoriesList : any[] = [];
  selectedCategories : any[] = [];

  editBugForm = this.fb.group({
    title: ['', [Validators.required]],
    description: ['', [Validators.required]],
    categories: new FormControl([] as any[], [Validators.required])
  })

  loading: boolean = false

  constructor(
    private fb: FormBuilder,
    private bugsService: BugsService,
    private formValidations: FormValidationsService,
    private translate: TranslateService,
    private categoryService: CategoryService,
    private routerAct: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ){}

  ngOnInit(): void {
    this.categoriesDropdownSettings.searchPlaceholderText = this.translate.instant("CATEGORY.SEARCH")
    this.categoriesDropdownSettings.noDataAvailablePlaceholderText = this.translate.instant("CATEGORY.NOT-FOUND")

    this.bugId = this.routerAct.snapshot.params['id'];
    this.getBugById();
    this.getCategories();
  }

  hasError(formControlName: string): boolean {
    return this.formValidations.hasError(this.editBugForm, formControlName);
  }

  getMessageError(formControlName: string): string {
    return this.formValidations.getMessageError(this.editBugForm, formControlName);
  }

  getBugById(){
    this.bugsService.getById(this.bugId).subscribe(
      bug => {
        this.bugDetail = bug;
        this.setDefaultValuesForm(bug.title, bug.description, bug.categories);
      },
      err => {
        if(err.status == 404){
          this.router.navigate(['/bugs']);
        }
      }
    )
  }

  setDefaultValuesForm(title: string, description: string, categories: any[]){
    this.editBugForm.get('categories')?.setValue(categories);
    this.editBugForm.get('title')?.setValue(title);
    this.editBugForm.get('description')?.setValue(description);
  }

  getCategories(){
    this.categoryService.getAll().subscribe((categories) => {
      this.categoriesList = categories;
    })
  }

  updateBug(){
    this.bugsService.updateBug(
      this.bugId,
      this.editBugForm.value.title!!,
      this.editBugForm.value.description!!,
      this.editBugForm.value.categories!!.map(c => ({id: c.id}))
    ).subscribe((newBug) => {
      this.toastr.success(
        this.translate.instant("BUG.UPDATE.UPDATED-MESSAGE")
      );
      this.router.navigate(['/bugs/' + newBug.id]);
    }).add(() => {this.loading = false});
  }
}
