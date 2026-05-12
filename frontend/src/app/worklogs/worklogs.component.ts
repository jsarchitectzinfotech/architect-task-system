import { Component, OnInit } from '@angular/core';
import { WorklogService } from '../services/worklog.service';
import { WorkLog } from '../models';

@Component({ selector: 'app-worklogs', templateUrl: './worklogs.component.html' })
export class WorklogsComponent implements OnInit {
  logs: WorkLog[] = []; loading = true;
  constructor(private svc: WorklogService) {}
  ngOnInit() { this.svc.getMyLogs().subscribe({ next: l => { this.logs = l; this.loading = false; }, error: () => this.loading = false }); }
}
