import { Component, OnInit, Inject, Optional } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TaskService } from '../../services/task.service';
import { ProjectService } from '../../services/project.service';
import { UserService } from '../../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User, Project, Task } from '../../models';

@Component({ selector: 'app-task-form', templateUrl: './task-form.component.html' })
export class TaskFormComponent implements OnInit {
  form = this.fb.group({
    title: ['', Validators.required],
    description: [''],
    assignedToId: [null, Validators.required],
    projectId: [null, Validators.required],
    priority: ['MEDIUM'],
    dueDate: [null],
    estimatedHours: [null]
  });
  juniors: User[] = []; projects: Project[] = []; loading = false;
  priorities = ['LOW','MEDIUM','HIGH','URGENT'];
  isEdit = false;

  constructor(
    private fb: FormBuilder, private taskSvc: TaskService,
    private projectSvc: ProjectService, private userSvc: UserService,
    private snack: MatSnackBar, private dialogRef: MatDialogRef<TaskFormComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: { task: Task } | null
  ) {}

  ngOnInit() {
    this.userSvc.getJuniors().subscribe(u => this.juniors = u);
    this.projectSvc.getAll().subscribe(p => this.projects = p);

    if (this.data?.task) {
      this.isEdit = true;
      const t = this.data.task;
      this.form.patchValue({
        title: t.title,
        description: t.description ?? '',
        assignedToId: (t.assignedTo as any)?.id ?? null,
        projectId: (t as any).projectId ?? null,
        priority: t.priority,
        dueDate: t.dueDate as any,
        estimatedHours: t.estimatedHours as any
      });
    }
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    const action = this.isEdit
      ? this.taskSvc.update(this.data!.task.id, this.form.value)
      : this.taskSvc.create(this.form.value);

    action.subscribe({
      next: () => {
        this.snack.open(this.isEdit ? 'Task updated!' : 'Task assigned!', '', { duration: 2500 });
        this.dialogRef.close(true);
      },
      error: () => { this.loading = false; this.snack.open('Error saving task', 'Close', { duration: 3000 }); }
    });
  }
}
