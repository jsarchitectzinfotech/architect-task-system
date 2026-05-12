import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User } from '../models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private base = `${environment.apiUrl}/users`;
  constructor(private http: HttpClient) {}

  getAll() { return this.http.get<User[]>(this.base); }
  getJuniors() { return this.http.get<User[]>(`${this.base}/juniors`); }
  getById(id: number) { return this.http.get<User>(`${this.base}/${id}`); }
  register(data: any) { return this.http.post<User>(`${environment.apiUrl}/auth/register`, data); }
  toggleActive(id: number) { return this.http.patch<User>(`${this.base}/${id}/toggle-active`, {}); }
}
