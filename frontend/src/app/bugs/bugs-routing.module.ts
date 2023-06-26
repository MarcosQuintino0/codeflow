import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes, UrlSegment } from '@angular/router';
import { BugsComponent } from './components/bugs/bugs.component';
import { BugDetailsComponent } from './components/bug-details/bug-details.component';
import { NewBugComponent } from './components/new-bug/new-bug.component';
import { UpdateBugComponent } from './components/update-bug/update-bug.component';
import { AuthGuardService } from '../auth/guards/auth-guard.service';

export function bugDetailsMatcher(url: UrlSegment[]){
  if(url.length == 1 && url[0].path.match(/^\d+$/)){
    return {consumed: url.slice(0,1), posParams: {id: new UrlSegment(url[0].path, {})}}
   }else return null
}
export function updateBugMatcher(url: UrlSegment[]){
  if(url.length > 1 && url[0].path.match(/^\d+$/) && url[1].path === 'update'){
    
    const posParams: { [key: string]: UrlSegment } = {};
    posParams['id'] = url[0];
    posParams['update'] = url[1];
    
    return {consumed: url, posParams: posParams}
   } else return null
}

const routes: Routes = [
  {path: '', component: BugsComponent, pathMatch: 'full'},
  {path: 'new', canActivate:[AuthGuardService], component: NewBugComponent, pathMatch: 'full'},
  {matcher: bugDetailsMatcher, component: BugDetailsComponent},
  {matcher: updateBugMatcher, canActivate:[AuthGuardService] ,component: UpdateBugComponent},
  {path: '**', redirectTo: 'bugs'}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports:[
    RouterModule
  ]
})
export class BugsRoutingModule { }
