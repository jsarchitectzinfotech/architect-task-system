import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';

@Component({ selector: 'app-register-form', template: `
<h2 mat-dialog-title>Add Team Member</h2>
<mat-dialog-content>
  <form [formGroup]="form">
    <mat-form-field appearance="outline" class="full"><mat-label>Full Name</mat-label><input matInput formControlName="name"></mat-form-field>
    <mat-form-field appearance="outline" class="full"><mat-label>Email</mat-label><input matInput formControlName="email" type="email"></mat-form-field>
    <mat-form-field appearance="outline" class="full"><mat-label>Password</mat-label><input matInput formControlName="password" type="password"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Role</mat-label>
      <mat-select formControlName="role">
        <mat-option value="JUNIOR">Junior</mat-option>
        <mat-option value="MANAGER">Manager</mat-option>
      </mat-select>
    </mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Designation</mat-label><input matInput formControlName="designation"></mat-form-field>
    <mat-form-field appearance="outline"><mat-label>Phone</mat-label><input matInput formControlName="phone"></mat-form-field>
  </form>
</mat-dialog-content>
<mat-dialog-actions align="end">
  <button mat-button mat-dialog-close>Cancel</button>
  <button mat-raised-button color="primary" (click)="submit()" [disabled]="loading || form.invalid">Add Member</button>
</mat-dialog-actions>
`, styles: [`.full{width:100%;display:block} mat-form-field{width:100%;margin-bottom:4px}`] })
export class RegisterFormComponent {
  form = this.fb.group({ name: ['', Validators.required], email: ['', [Validators.required, Validators.email]], password: ['', Validators.required], role: ['JUNIOR'], designation: [''], phone: [''] });
  loading = false;
  constructor(private fb: FormBuilder, private userSvc: UserService, private snack: MatSnackBar, private ref: MatDialogRef<RegisterFormComponent>) {}
  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.userSvc.register(this.form.value).subscribe({
      next: () => { this.snack.open('Member added!', '', { duration: 2500 }); this.ref.close(true); },
      error: () => { this.loading = false; this.snack.open('Error adding member', 'Close', { duration: 3000 }); }
    });
  }
}
