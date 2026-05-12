import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { Notification } from '../models';

@Component({ selector: 'app-notifications', templateUrl: './notifications.component.html' })
export class NotificationsComponent implements OnInit {
  notifications: Notification[] = []; loading = true;

  constructor(private notifSvc: NotificationService) {}

  ngOnInit() {
    this.notifSvc.getAll().subscribe({ next: n => { this.notifications = n; this.loading = false; }, error: () => this.loading = false });
  }

  markRead(n: Notification) {
    if (n.read) return;
    this.notifSvc.markAsRead(n.id).subscribe(() => { n.read = true; this.notifSvc.getUnreadCount().subscribe(); });
  }

  markAllRead() {
    this.notifSvc.markAllAsRead().subscribe(() => { this.notifications.forEach(n => n.read = true); this.notifSvc.unreadCount$.next(0); });
  }
}
