import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Project } from '../models';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private base = `${environment.apiUrl}/projects`;
  constructor(private http: HttpClient) {}

  getAll() { return this.http.get<Project[]>(this.base); }
  getById(id: number) { return this.http.get<Project>(`${this.base}/${id}`); }
  create(data: any) { return this.http.post<Project>(this.base, data); }
  update(id: number, data: any) { return this.http.put<Project>(`${this.base}/${id}`, data); }
  // Backend expects status as a query param
  updateStatus(id: number, status: string) {
    return this.http.patch<Project>(`${this.base}/${id}/status`, {}, { params: { status } });
  }
  delete(id: number) { return this.http.delete(`${this.base}/${id}`); }
}
