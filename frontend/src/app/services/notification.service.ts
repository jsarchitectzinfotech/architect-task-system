import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Notification } from '../models';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private base = `${environment.apiUrl}/notifications`;
  unreadCount$ = new BehaviorSubject<number>(0);
  constructor(private http: HttpClient) {}

  getAll() { return this.http.get<Notification[]>(this.base); }

  getUnreadCount() {
    return this.http.get<{ count: number }>(`${this.base}/unread-count`).pipe(
      map(r => r.count),
      tap(count => this.unreadCount$.next(count))
    );
  }

  markAsRead(id: number) { return this.http.patch(`${this.base}/${id}/read`, {}); }

  markAllAsRead() { return this.http.patch(`${this.base}/mark-all-read`, {}); }
}
