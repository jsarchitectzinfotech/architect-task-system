import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { WorkLog } from '../models';

@Injectable({ providedIn: 'root' })
export class WorklogService {
  constructor(private http: HttpClient) {}

  getByTask(taskId: number) {
    return this.http.get<WorkLog[]>(`${environment.apiUrl}/tasks/${taskId}/worklogs`);
  }

  addLog(taskId: number, data: any) {
    return this.http.post<WorkLog>(`${environment.apiUrl}/tasks/${taskId}/worklogs`, data);
  }

  getMyLogs() {
    return this.http.get<WorkLog[]>(`${environment.apiUrl}/worklogs/my`);
  }
}
