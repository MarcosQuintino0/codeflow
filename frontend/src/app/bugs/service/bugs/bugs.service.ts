import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from 'src/app/shared/models/page.model';
import { environment } from 'src/environments/environment.prod';
import { Bug } from '../../models/bug.model';
import { TotalBugsSolved } from '../../models/count.model';
import { Reply } from '../../models/reply.model';

@Injectable({
  providedIn: 'root'
})
export class BugsService {

  private readonly URL: string = `${environment.api}/bug`

  constructor(
    private http: HttpClient
  ) { }

  getAll(paramConfig: Page<Bug>): Observable<Page<Bug>>{
    return this.http.get<Page<Bug>>(this.URL, {
      params: {
        page: paramConfig.page-1,
        size: paramConfig.size,
        sort: paramConfig.sort,
        ...(paramConfig.filters ?? {})
      }
    })
  }

  getAllUserBugs(paramConfig: Page<Bug>){
    return this.http.get<Page<Bug>>(this.URL + "/user", {
      params: {
        page: paramConfig.page-1,
        size: paramConfig.size,
        sort: paramConfig.sort,
        ...(paramConfig.filters ?? {})
      }
    })
  }

  createBug(title: string, description: string, categories: any[]): Observable<Bug>{
    return this.http.post<Bug>(this.URL, {
      title: title,
      description: description,
      categories: categories
    })
  }

  getAllRepliesByBugId(bugId: number, paramConfig: Page<Reply>): Observable<Page<Reply>>{
    return this.http.get<Page<Reply>>(this.URL + "/" + bugId + "/reply",{
      params: {
        page: paramConfig.page-1,
        size: paramConfig.size,
        sort: ["bestAnswer,DESC", paramConfig.sort],
        ...(paramConfig.filters ?? {})
      }}
    )
  }

  updateBestAnswer(idBug: number, idReply: number){
    return this.http.put<void>(this.URL + "/" + idBug + "/reply/" + idReply, null);
  }

  updateBug(id: number, title: string, description: string, categories: any[]): Observable<Bug>{
    return this.http.put<Bug>(this.URL + "/" + id, {
      title: title,
      description: description,
      categories: categories
    })
  }

  createBugReply(bugId: number, description: string){
    return this.http.post(this.URL + "/" + bugId + "/reply", {
      description: description
    })
  }

  deleteById(bugId: number) {
    return this.http.delete(this.URL + "/" + bugId);
  }

  getById(id: number): Observable<Bug>{
    return this.http.get<Bug>(this.URL + "/" + id)
  }

  getCountOfSolvedBugs(){
    return this.http.get<TotalBugsSolved>(this.URL + "/solved")
  }
}
