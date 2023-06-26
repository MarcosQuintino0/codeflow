import { HttpClient } from '@angular/common/http';
import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, interval, Observable, Subscribable, Subscription, tap } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { Tokens } from '../models/tokens.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnDestroy{

  private readonly URL: string = `${environment.api}/auth`
  private readonly AUTH_TOKEN_KEY: string = "AUTH_TOKEN"
  private readonly AUTH_TOKEN_EXPIRES_AT_KEY: string = "AUTH_TOKEN_EXPIRES_AT"
  private readonly REFRESH_TOKEN_KEY: string = "REFRESH_TOKEN"
  private readonly REFRESH_TOKEN_EXPIRES_AT_KEY: string = "REFRESH_TOKEN_EXPIRES_AT"
  private LOGGED_IN_USERNAME_KEY: string = "LOGGED_IN_USERNAME"

  private _isAuthenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  public isAuthenticatedObs: Observable<boolean> = this._isAuthenticatedSubject.asObservable();

  private refreshInterval: Subscription = new Subscription();

  constructor(private http: HttpClient) { }

  ngOnDestroy(): void{
    this.refreshInterval.unsubscribe()
  }

  refreshTokenInterval(authTokenExpirationDate: Date){
    if(this.isAuthenticated()){
      const authTokenExpiresAt = new Date(authTokenExpirationDate).getTime() - new Date().getTime()
      const reloadInterval = authTokenExpiresAt - (60*1000)
      this.refreshInterval = interval(reloadInterval).subscribe(() => {
        this.refreshToken()
      })
    }
  }

  login(username: string, password: string){
    return this.http.post<Tokens>(`${this.URL}/login`, {
      username: username,
      password: password
    }).pipe(
      tap(tokens => {
        this.saveTokens(tokens, username)
        this.refreshTokenInterval(tokens.authTokenExpiresAt)
      })
    )
  }

  refreshToken(){
    this.http.post<Tokens>(`${this.URL}/refresh`, {
      bearerToken: this.getAuthToken(),
      refreshToken: this.getRefreshToken()
    }).subscribe(
      (tokens) => {this.saveTokens(tokens, '')}
    );
  }

  logout(){
    this._isAuthenticatedSubject.next(false);
    sessionStorage.clear()
  }

  getAuthToken(): string{
    return sessionStorage.getItem(this.AUTH_TOKEN_KEY)!;
  }

  getRefreshToken(): string{
    return sessionStorage.getItem(this.REFRESH_TOKEN_KEY)!;
  }
  
  isRefreshTokenExpired(){
    var refreshTokenExpiresAt = new Date(sessionStorage.getItem('REFRESH_TOKEN_EXPIRES_AT')!!)
    return new Date().toString() > refreshTokenExpiresAt.toString()
  }

  isAuthenticated(){
    return !!this.getAuthToken() && !this.isRefreshTokenExpired()
  }

  refreshAuthenticatedState(){
    this._isAuthenticatedSubject.next(this.isAuthenticated());
  }

  saveTokens(tokens: Tokens, username: string): void {

    sessionStorage.setItem(this.AUTH_TOKEN_KEY, tokens.authToken)
    sessionStorage.setItem(this.REFRESH_TOKEN_KEY, tokens.refreshToken)
    sessionStorage.setItem(this.AUTH_TOKEN_EXPIRES_AT_KEY, tokens.authTokenExpiresAt.toString())
    sessionStorage.setItem(this.REFRESH_TOKEN_EXPIRES_AT_KEY, tokens.refreshTokenExpiresAt.toString())
    
    if(username.length > 0){sessionStorage.setItem(this.LOGGED_IN_USERNAME_KEY, username)}

    this._isAuthenticatedSubject.next(true);
  }
}
