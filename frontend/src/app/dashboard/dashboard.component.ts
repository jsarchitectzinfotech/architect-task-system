import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../services/dashboard.service';
import { TaskService } from '../services/task.service';
import { AuthService } from '../services/auth.service';
import { DashboardData, Task } from '../models';

@Component({ selector: 'app-dashboard', templateUrl: './dashboard.component.html', styleUrls: ['./dashboard.component.scss'] })
export class DashboardComponent implements OnInit {
  summary: DashboardData | null = null;
  myTasks: Task[] = [];
  loading = true;

  constructor(private dashSvc: DashboardService, private taskSvc: TaskService, public auth: AuthService) {}

  ngOnInit() {
    this.dashSvc.getSummary().subscribe({ next: s => { this.summary = s; this.loading = false; }, error: () => this.loading = false });
    this.taskSvc.getMyTasks().subscribe(t => this.myTasks = t);
  }
}
