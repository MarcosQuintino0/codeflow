import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router} from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { HtmlConverter } from 'src/app/shared/utils/html-converter';
import { Bug } from '../../models/bug.model';
import { BugsService } from '../../service/bugs/bugs.service';

@Component({
  selector: 'app-bug-details',
  templateUrl: './bug-details.component.html',
  styleUrls: ['./bug-details.component.css'],
})
export class BugDetailsComponent implements OnInit{

  bugId: number = 0;
  bugDetail = <Bug>{};
  isPostOwner: boolean = false;
  isAuthenticated: boolean = false;
  
  constructor(
    private bugsService: BugsService,
    private authService: AuthService,
    private routerAct: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private translate: TranslateService,
    private htmlConverter: HtmlConverter
  ){}

  ngOnInit(): void {
    this.bugId = this.routerAct.snapshot.params['id'];
    this.getBugById();
    this.authService.isAuthenticatedObs.subscribe(isAuthenticated => {
      this.isAuthenticated = isAuthenticated
    })
  }

  getBugById(){
    this.bugsService.getById(this.bugId).subscribe(
      bug => {
        bug.description = this.htmlConverter.getCodeSnippetAsHtml(bug.description)

        this.bugDetail = bug;
        this.isPostOwner = this.isLoggedUserOwner(this.bugDetail.user.username);
      },
      err => {
        if(err.status == 404){
          this.router.navigate(['/bugs']);
        }
      }
    )
  }

  deleteBug(): void{
    this.bugsService.deleteById(this.bugId).subscribe(() => {
    this.toastr.success(
        this.translate.instant("BUG.DELETED-SUCCESSFULLY")
      );
    });
    
    this.router.navigate(["/bugs"]);
  }


  isLoggedUserOwner(userOwner: string){
   return userOwner === sessionStorage.getItem('LOGGED_IN_USERNAME')  
  }

}
