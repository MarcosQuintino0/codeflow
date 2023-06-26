import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/auth/services/auth.service';
import { Page } from 'src/app/shared/models/page.model';
import { FormValidationsService } from 'src/app/shared/service/form-validations.service';
import { HtmlConverter } from 'src/app/shared/utils/html-converter';
import { Bug } from '../../models/bug.model';
import { Reply } from '../../models/reply.model';
import { BugsService } from '../../service/bugs/bugs.service';
import { ReplyService } from '../../service/reply/reply.service';

@Component({
  selector: 'app-reply',
  templateUrl: './reply.component.html',
  styleUrls: ['./reply.component.css']
})
export class ReplyComponent implements OnInit{

  editReplyForm = this.fb.group({
    description: ['', [Validators.required]]
  })

  replyForm = this.fb.group({
    description: ['', [Validators.required]]
  })

  pageReply: Page<Reply> = new Page<Reply>();
  isPostOwner: boolean = false;
  isAnswerOwner: Map<number,boolean> = new Map<number,boolean>();
  replyToDeleteId: number = 0;
  replyToEditId: number = 0;
  isAuthenticated: boolean = false;
  bestAnswerId: number = 0;
  @Input() bug!: Bug;
  
  constructor(
    private authService: AuthService,
    private bugsService: BugsService,
    private replyService: ReplyService,
    private toastr: ToastrService,
    private fb: FormBuilder,
    private translate: TranslateService,
    private formValidations: FormValidationsService,
    private htmlConverter: HtmlConverter
  ){}

  ngOnInit(): void {
    this.getReplies();
    this.isPostOwner = this.isLoggedUserOwner(this.bug.user.username)
    this.authService.isAuthenticatedObs.subscribe(isAuthenticated => {
      this.isAuthenticated = isAuthenticated
    })
  }

  convertReplyDescriptionToHtml(description: string){
    return this.htmlConverter.getCodeSnippetAsHtml(description);
  }

  getReplies(){
    this.pageReply.size = 5;
    this.pageReply.sort = "createdAt,DESC"
    this.bugsService.getAllRepliesByBugId(this.bug.id!!, this.pageReply).subscribe((page) => {
      page.content.forEach( (reply) => {
        this.isAnswerOwner.set(reply.id!!, this.isLoggedUserOwner(reply.user.username))
      })
      this.pageReply.content = page.content;
      this.pageReply.totalElements = page.totalElements;
    });
  }

  getRepliesByPage(page: number){
    this.pageReply.page = page;
    this.pageReply.sort = "createdAt,DESC";
    this.getReplies();
  }

  setBestAnswer(){
    this.bugsService.updateBestAnswer(this.bug.id!!, this.bestAnswerId).subscribe( () => 
    {
      this.toastr.success(
        this.translate.instant("REPLY.UPDATED-SUCCESSFULLY")
      )
      this.getRepliesByPage(1);
    })
  }

  setBestAnswerId(replyId: number){
    this.bestAnswerId = replyId;
  }

  setReplyToDeleteId(replyId: number){
    this.replyToDeleteId = replyId
  }

  setReplyToEditId(replyId: number){
    this.replyToEditId = replyId;
  }

  setDefaultEditField(description: string){
    this.editReplyForm.get('description')?.setValue(description);
  }

  createNewReply(){
    this.bugsService.createBugReply(this.bug.id!!, this.replyForm.value.description!!).subscribe(() => {
      this.toastr.success(
        this.translate.instant("REPLY.CREATED")
      )
      this.getRepliesByPage(1);
      this.replyForm.reset()
    })
  }

  deleteReply(){
    this.replyService.deleteReply(this.replyToDeleteId).subscribe(() => {
      this.toastr.success(
        this.translate.instant("REPLY.DELETED-SUCCESSFULLY")
      )
      this.getRepliesByPage(1);
    })
  }

  updateReply(replyId: number){
    this.replyService.updateReply(replyId, this.editReplyForm.value.description!!).subscribe(() => {
      this.toastr.success(
        this.translate.instant("REPLY.UPDATED-SUCCESSFULLY")
      )
      this.getRepliesByPage(1);
    })
  }

  hasError(formControlName: string): boolean {
    return this.formValidations.hasError(this.replyForm, formControlName);
  }

  getMessageError(formControlName: string): string {
    return this.formValidations.getMessageError(this.replyForm, formControlName);
  }


  isLoggedUserOwner(userOwner: string){
    return userOwner === sessionStorage.getItem('LOGGED_IN_USERNAME')  
  }

}
