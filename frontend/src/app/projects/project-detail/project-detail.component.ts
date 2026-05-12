import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { TaskService } from '../../services/task.service';
import { AuthService } from '../../services/auth.service';
import { Project, Task } from '../../models';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskFormComponent } from '../../tasks/task-form/task-form.component';
import { ProjectFormComponent } from '../project-form/project-form.component';

@Component({ selector: 'app-project-detail', templateUrl: './project-detail.component.html', styleUrls: ['./project-detail.component.scss'] })
export class ProjectDetailComponent implements OnInit {
  project: Project | null = null; tasks: Task[] = []; loading = true;
  columns = ['title','assignedTo','status','priority','dueDate','actions'];

  constructor(
    private route: ActivatedRoute, private projSvc: ProjectService,
    private taskSvc: TaskService, public auth: AuthService,
    private router: Router, private dialog: MatDialog, private snack: MatSnackBar
  ) {}

  ngOnInit() {
    this.route.params.subscribe(p => {
      const id = +p['id'];
      this.projSvc.getById(id).subscribe(pr => { this.project = pr; this.loading = false; });
      this.taskSvc.getByProject(id).subscribe(t => this.tasks = t);
    });
  }

  openAssign() {
    this.dialog.open(TaskFormComponent, { width: '600px' })
      .afterClosed().subscribe(r => { if (r && this.project) this.taskSvc.getByProject(this.project.id).subscribe(t => this.tasks = t); });
  }

  editProject() {
    this.dialog.open(ProjectFormComponent, { width: '560px', minWidth: '320px', maxHeight: '90vh', data: { project: this.project } })
      .afterClosed().subscribe(r => { if (r && this.project) this.projSvc.getById(this.project.id).subscribe(p => this.project = p); });
  }

  deleteProject() {
    if (!this.project || !confirm(`Delete project "${this.project.name}"? This will also delete all its tasks.`)) return;
    this.projSvc.delete(this.project.id).subscribe({
      next: () => { this.snack.open('Project deleted', '', { duration: 2000 }); this.router.navigate(['/projects']); },
      error: () => this.snack.open('Error deleting project', 'Close', { duration: 3000 })
    });
  }

  editTask(t: Task) {
    this.dialog.open(TaskFormComponent, { width: '600px', data: { task: t } })
      .afterClosed().subscribe(r => { if (r && this.project) this.taskSvc.getByProject(this.project.id).subscribe(ts => this.tasks = ts); });
  }

  deleteTask(t: Task) {
    if (!confirm(`Delete task "${t.title}"?`)) return;
    this.taskSvc.delete(t.id).subscribe({
      next: () => { this.tasks = this.tasks.filter(x => x.id !== t.id); this.snack.open('Task deleted', '', { duration: 2000 }); },
      error: () => this.snack.open('Error deleting task', 'Close', { duration: 3000 })
    });
  }

  viewTask(id: number) { this.router.navigate(['/tasks', id]); }
}
