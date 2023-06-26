import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { BugsService } from '../bugs/service/bugs/bugs.service';
import { UserService } from '../usuario/service/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  totalUsers = 0;
  totalBugsSolved = 0;

  constructor(
    private bugsService: BugsService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.bugsService.getCountOfSolvedBugs().subscribe((body) =>{
      this.totalBugsSolved = body.totalBugs
    })

    this.userService.getCountOfUsers().subscribe((body) => {
      this.totalUsers = body.totalUsers
    })
  }

}
