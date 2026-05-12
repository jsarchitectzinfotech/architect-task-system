import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskService } from '../services/task.service';
import { AuthService } from '../services/auth.service';
import { Task } from '../models';
import { TaskFormComponent } from './task-form/task-form.component';

@Component({ selector: 'app-tasks', templateUrl: './tasks.component.html', styleUrls: ['./tasks.component.scss'] })
export class TasksComponent implements OnInit {
  tasks: Task[] = []; loading = true;
  get columns() {
    return this.auth.isManager
      ? ['title','project','assignedTo','status','priority','dueDate','actions']
      : ['title','project','status','priority','dueDate'];
  }

  constructor(private taskSvc: TaskService, public auth: AuthService, private router: Router, private dialog: MatDialog, private snack: MatSnackBar) {}

  ngOnInit() { this.load(); }

  load() {
    const obs = this.auth.isManager ? this.taskSvc.getAll() : this.taskSvc.getMyTasks();
    obs.subscribe({ next: t => { this.tasks = t; this.loading = false; }, error: () => this.loading = false });
  }

  view(id: number) { this.router.navigate(['/tasks', id]); }

  openAssign() {
    this.dialog.open(TaskFormComponent, { width: '600px' }).afterClosed().subscribe(r => { if (r) this.load(); });
  }

  editTask(event: Event, t: Task) {
    event.stopPropagation();
    this.dialog.open(TaskFormComponent, { width: '600px', data: { task: t } }).afterClosed().subscribe(r => { if (r) this.load(); });
  }

  deleteTask(event: Event, t: Task) {
    event.stopPropagation();
    if (!confirm(`Delete task "${t.title}"?`)) return;
    this.taskSvc.delete(t.id).subscribe({
      next: () => { this.tasks = this.tasks.filter(x => x.id !== t.id); this.snack.open('Task deleted', '', { duration: 2000 }); },
      error: () => this.snack.open('Error deleting task', 'Close', { duration: 3000 })
    });
  }
}
