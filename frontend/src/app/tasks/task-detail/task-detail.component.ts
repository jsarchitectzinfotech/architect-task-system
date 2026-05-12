import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, Validators } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { WorklogService } from '../../services/worklog.service';
import { ArtifactService } from '../../services/artifact.service';
import { CommentService } from '../../services/comment.service';
import { AuthService } from '../../services/auth.service';
import { Task, WorkLog, Artifact, Comment, TaskStatusHistory } from '../../models';
import { TaskFormComponent } from '../task-form/task-form.component';

@Component({ selector: 'app-task-detail', templateUrl: './task-detail.component.html', styleUrls: ['./task-detail.component.scss'] })
export class TaskDetailComponent implements OnInit {
  task: Task | null = null; worklogs: WorkLog[] = []; artifacts: Artifact[] = [];
  comments: Comment[] = []; history: TaskStatusHistory[] = [];
  loading = true; selectedFile: File | null = null;
  newComment = ''; fileDescription = '';
  managerStatuses = ['ASSIGNED','IN_PROGRESS','UNDER_REVIEW','APPROVED','REVISION_REQUESTED'];
  juniorStatuses = ['IN_PROGRESS','UNDER_REVIEW'];
  selectedStatus = ''; statusRemarks = '';

  logForm = this.fb.group({ description: ['', Validators.required], plannedForTomorrow: [''], blockers: [''], hoursSpent: [null] });

  constructor(
    private route: ActivatedRoute, private router: Router,
    private taskSvc: TaskService, private worklogSvc: WorklogService,
    private artifactSvc: ArtifactService, private commentSvc: CommentService,
    public auth: AuthService, private snack: MatSnackBar,
    private fb: FormBuilder, private dialog: MatDialog
  ) {}

  ngOnInit() { this.route.params.subscribe(p => this.loadAll(+p['id'])); }

  loadAll(id: number) {
    this.taskSvc.getById(id).subscribe(t => { this.task = t; this.selectedStatus = t.status; this.loading = false; });
    this.worklogSvc.getByTask(id).subscribe(w => this.worklogs = w);
    this.artifactSvc.getByTask(id).subscribe(a => this.artifacts = a);
    this.commentSvc.getByTask(id).subscribe(c => this.comments = c);
    this.taskSvc.getHistory(id).subscribe(h => this.history = h);
  }

  updateStatus() {
    if (!this.task) return;
    this.taskSvc.updateStatus(this.task.id, this.selectedStatus, this.statusRemarks).subscribe({
      next: t => { this.task = t; this.snack.open('Status updated', '', { duration: 2000 }); this.taskSvc.getHistory(t.id).subscribe(h => this.history = h); },
      error: () => this.snack.open('Error updating status', 'Close', { duration: 3000 })
    });
  }

  submitLog() {
    if (!this.task || this.logForm.invalid) return;
    this.worklogSvc.addLog(this.task.id, this.logForm.value).subscribe({
      next: l => { this.worklogs = [l, ...this.worklogs]; this.logForm.reset(); this.snack.open('Log saved!', '', { duration: 2000 }); },
      error: () => this.snack.open('Error saving log', 'Close', { duration: 3000 })
    });
  }

  onFileSelected(event: any) { this.selectedFile = event.target.files[0]; }

  uploadFile() {
    if (!this.task || !this.selectedFile) return;
    this.artifactSvc.upload(this.task.id, this.selectedFile, this.fileDescription).subscribe({
      next: a => { this.artifacts = [a, ...this.artifacts]; this.selectedFile = null; this.fileDescription = ''; this.snack.open('File uploaded!', '', { duration: 2000 }); },
      error: () => this.snack.open('Upload failed', 'Close', { duration: 3000 })
    });
  }

  addComment() {
    if (!this.task || !this.newComment.trim()) return;
    this.commentSvc.add(this.task.id, this.newComment).subscribe({
      next: c => { this.comments.push(c); this.newComment = ''; },
      error: () => this.snack.open('Error adding comment', 'Close', { duration: 3000 })
    });
  }

  downloadArtifact(id: number) {
    this.artifactSvc.getDownloadUrl(id).subscribe(url => window.open(url, '_blank'));
  }

  deleteArtifact(a: Artifact) {
    if (!confirm(`Delete file "${a.fileName}"?`)) return;
    this.artifactSvc.delete(a.id).subscribe({
      next: () => { this.artifacts = this.artifacts.filter(x => x.id !== a.id); this.snack.open('File deleted', '', { duration: 2000 }); },
      error: () => this.snack.open('Error deleting file', 'Close', { duration: 3000 })
    });
  }

  editTask() {
    if (!this.task) return;
    this.dialog.open(TaskFormComponent, { width: '600px', data: { task: this.task } })
      .afterClosed().subscribe(r => { if (r && this.task) this.loadAll(this.task.id); });
  }

  deleteTask() {
    if (!this.task || !confirm(`Delete task "${this.task.title}"?`)) return;
    this.taskSvc.delete(this.task.id).subscribe({
      next: () => { this.snack.open('Task deleted', '', { duration: 2000 }); this.router.navigate(['/tasks']); },
      error: () => this.snack.open('Error deleting task', 'Close', { duration: 3000 })
    });
  }

  get availableStatuses() { return this.auth.isManager ? this.managerStatuses : this.juniorStatuses; }
}
