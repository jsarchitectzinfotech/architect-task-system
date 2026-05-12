import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Task, TaskStatusHistory } from '../models';

@Injectable({ providedIn: 'root' })
export class TaskService {
  private base = `${environment.apiUrl}/tasks`;
  constructor(private http: HttpClient) {}

  create(data: any) { return this.http.post<Task>(this.base, data); }
  getAll() { return this.http.get<Task[]>(this.base); }
  getById(id: number) { return this.http.get<Task>(`${this.base}/${id}`); }
  getByProject(projectId: number) { return this.http.get<Task[]>(`${this.base}/project/${projectId}`); }
  // Backend filters by role automatically — no separate /my endpoint needed
  getMyTasks() { return this.http.get<Task[]>(this.base); }
  update(id: number, data: any) { return this.http.put<Task>(`${this.base}/${id}`, data); }
  updateStatus(id: number, status: string, remarks?: string) {
    return this.http.patch<Task>(`${this.base}/${id}/status`, { status, remarks });
  }
  delete(id: number) { return this.http.delete(`${this.base}/${id}`); }
  getHistory(id: number) { return this.http.get<TaskStatusHistory[]>(`${this.base}/${id}/history`); }
}
