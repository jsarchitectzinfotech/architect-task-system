import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { NotificationService } from '../../../services/notification.service';

@Component({ selector: 'app-layout', templateUrl: './layout.component.html', styleUrls: ['./layout.component.scss'] })
export class LayoutComponent implements OnInit {
  unreadCount = 0;
  constructor(public auth: AuthService, public notifService: NotificationService) {}
  ngOnInit() { this.notifService.getUnreadCount().subscribe(); this.notifService.unreadCount$.subscribe(c => this.unreadCount = c); }
  logout() { this.auth.logout(); }
}
