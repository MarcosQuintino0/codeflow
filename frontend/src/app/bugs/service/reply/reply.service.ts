import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from 'src/app/shared/models/page.model';
import { environment } from 'src/environments/environment.prod';
import { Reply } from '../../models/reply.model';

@Injectable({
  providedIn: 'root'
})
export class ReplyService {

  private readonly URL: string = `${environment.api}/reply`

  constructor(private http: HttpClient) { }

  updateReply(replyId: number, description: string){
    return this.http.put(this.URL + "/" + replyId, {
      description: description
    })
  }

  deleteReply(replyId: number){
    return this.http.delete(this.URL + "/" + replyId)
  }

}
