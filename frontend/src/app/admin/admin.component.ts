import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../services/user.service';
import { User } from '../models';
import { RegisterFormComponent } from './register-form/register-form.component';

@Component({ selector: 'app-admin', templateUrl: './admin.component.html' })
export class AdminComponent implements OnInit {
  users: User[] = []; loading = true;
  columns = ['name','email','role','designation','active','actions'];

  constructor(private userSvc: UserService, private dialog: MatDialog) {}

  ngOnInit() { this.load(); }

  load() { this.userSvc.getAll().subscribe({ next: u => { this.users = u; this.loading = false; }, error: () => this.loading = false }); }

  openRegister() { this.dialog.open(RegisterFormComponent, { width: '500px' }).afterClosed().subscribe(r => { if (r) this.load(); }); }

  toggle(u: User) { this.userSvc.toggleActive(u.id).subscribe(updated => u.active = updated.active); }
}
