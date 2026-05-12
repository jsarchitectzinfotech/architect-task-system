import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Comment } from '../models';

@Injectable({ providedIn: 'root' })
export class CommentService {
  constructor(private http: HttpClient) {}

  getByTask(taskId: number) {
    return this.http.get<Comment[]>(`${environment.apiUrl}/tasks/${taskId}/comments`);
  }

  add(taskId: number, content: string) {
    return this.http.post<Comment>(`${environment.apiUrl}/tasks/${taskId}/comments`, { content });
  }
}
