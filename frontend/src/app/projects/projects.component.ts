import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProjectService } from '../services/project.service';
import { AuthService } from '../services/auth.service';
import { Project } from '../models';
import { ProjectFormComponent } from './project-form/project-form.component';

@Component({ selector: 'app-projects', templateUrl: './projects.component.html', styleUrls: ['./projects.component.scss'] })
export class ProjectsComponent implements OnInit {
  projects: Project[] = []; loading = true;

  constructor(private projSvc: ProjectService, public auth: AuthService, private dialog: MatDialog, private snack: MatSnackBar) {}

  ngOnInit() { this.load(); }

  load() {
    this.projSvc.getAll().subscribe({ next: p => { this.projects = p; this.loading = false; }, error: () => this.loading = false });
  }

  openCreate() {
    this.dialog.open(ProjectFormComponent, { width: '560px', minWidth: '320px', maxHeight: '90vh' })
      .afterClosed().subscribe(r => { if (r) this.load(); });
  }

  openEdit(event: Event, p: Project) {
    event.stopPropagation();
    this.dialog.open(ProjectFormComponent, { width: '560px', minWidth: '320px', maxHeight: '90vh', data: { project: p } })
      .afterClosed().subscribe(r => { if (r) this.load(); });
  }

  delete(event: Event, p: Project) {
    event.stopPropagation();
    if (!confirm(`Delete project "${p.name}"? This will also delete all its tasks.`)) return;
    this.projSvc.delete(p.id).subscribe({
      next: () => { this.projects = this.projects.filter(x => x.id !== p.id); this.snack.open('Project deleted', '', { duration: 2500 }); },
      error: () => this.snack.open('Error deleting project', 'Close', { duration: 3000 })
    });
  }
}
