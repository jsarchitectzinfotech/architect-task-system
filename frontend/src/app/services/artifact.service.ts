import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Artifact } from '../models';

@Injectable({ providedIn: 'root' })
export class ArtifactService {
  constructor(private http: HttpClient) {}

  getByTask(taskId: number) {
    return this.http.get<Artifact[]>(`${environment.apiUrl}/tasks/${taskId}/artifacts`);
  }

  upload(taskId: number, file: File, description?: string) {
    const fd = new FormData();
    fd.append('file', file);
    if (description) fd.append('description', description);
    return this.http.post<Artifact>(`${environment.apiUrl}/tasks/${taskId}/artifacts`, fd);
  }

  getDownloadUrl(id: number) {
    return this.http.get(`${environment.apiUrl}/artifacts/${id}/download`, { responseType: 'text' });
  }

  delete(id: number) {
    return this.http.delete(`${environment.apiUrl}/artifacts/${id}`);
  }
}
