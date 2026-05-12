import { Component, Input } from '@angular/core';
@Component({
  selector: 'app-status-badge',
  template: '<span class="badge" [ngClass]="statusClass">{{label}}</span>',
  styles: [`.badge{display:inline-flex;align-items:center;justify-content:center;padding:6px 12px;border-radius:16px;font-size:12px;font-weight:600;letter-spacing:0.3px;text-transform:capitalize}
  .assigned{background:#e3f2fd;color:#1565c0}.in-progress{background:#fff3e0;color:#e65100}
  .under-review{background:#f3e5f5;color:#6a1b9a}.approved{background:#e8f5e9;color:#2e7d32}
  .revision{background:#fce4ec;color:#c62828}.active{background:#e8f5e9;color:#2e7d32}
  .low{background:#f1f8e9;color:#558b2f}.medium{background:#fff8e1;color:#f57f17}
  .high{background:#fbe9e7;color:#bf360c}.urgent{background:#fce4ec;color:#880e4f}`]
})
export class StatusBadgeComponent {
  @Input() status = '';
  get statusClass() {
    const m: any = {'ASSIGNED':'assigned','IN_PROGRESS':'in-progress','UNDER_REVIEW':'under-review','APPROVED':'approved','REVISION_REQUESTED':'revision','ACTIVE':'active','LOW':'low','MEDIUM':'medium','HIGH':'high','URGENT':'urgent'};
    return m[this.status] || 'assigned';
  }
  get label() { return this.status?.replace(/_/g,' ') || ''; }
}
