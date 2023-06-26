import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  languageSelected!: string;
  isLoggedIn: boolean = false;
  loggedInUsername: string = ''

  constructor(
    private translate: TranslateService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
    ) { }

  ngOnInit(): void {
    this.selectLanguage(environment.defaultLanguage);
    this.authService.refreshAuthenticatedState();
    this.authService.isAuthenticatedObs.subscribe(isAuthenticated => {
      this.isLoggedIn = isAuthenticated
      if(isAuthenticated){
        this.loggedInUsername = sessionStorage.getItem("LOGGED_IN_USERNAME")!!
      }
    })
  }

  selectLanguage(language: string) {
    this.translate.use(language);
    this.languageSelected = this.translate.currentLang; 
  }

  getLoggedInUsername(){
    var username = this.loggedInUsername;
    if(this.loggedInUsername.length > 20){
      username = this.loggedInUsername.substring(0,19) + "..."
    }
    return username
  }

  logout(){
    this.authService.logout()
    this.toastr.success(
      this.translate.instant("LOGIN.LOGOUT_SUCESSFULLY")
    )
  }

  getIsLoggedIn(){
    
  }

}
