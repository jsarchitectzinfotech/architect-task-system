import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.scss']
})
export class ProjectFormComponent implements OnInit {
  form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    clientName: [''],
    projectCode: [''],
    location: ['']
  });
  loading = false;
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    private projSvc: ProjectService,
    private snack: MatSnackBar,
    private ref: MatDialogRef<ProjectFormComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: { project: Project } | null
  ) {}

  ngOnInit() {
    if (this.data?.project) {
      this.isEdit = true;
      const p = this.data.project;
      this.form.patchValue({
        name: p.name,
        description: p.description ?? '',
        clientName: p.clientName ?? '',
        projectCode: p.projectCode ?? '',
        location: p.location ?? ''
      });
    }
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    const action = this.isEdit
      ? this.projSvc.update(this.data!.project.id, this.form.value)
      : this.projSvc.create(this.form.value);

    action.subscribe({
      next: () => {
        this.snack.open(this.isEdit ? 'Project updated!' : 'Project created!', '', { duration: 2500 });
        this.ref.close(true);
      },
      error: () => {
        this.loading = false;
        this.snack.open('Error saving project', 'Close', { duration: 3000 });
      }
    });
  }
}
