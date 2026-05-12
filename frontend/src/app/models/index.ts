export interface User {
  id: number; name: string; email: string; role: string;
  phone?: string; designation?: string; active: boolean;
}

export interface Project {
  id: number; name: string; description?: string; status: string;
  clientName?: string; location?: string; projectCode?: string; createdAt?: string;
}

export interface Task {
  id: number; title: string; description?: string; status: string; priority: string;
  dueDate?: string; estimatedHours?: number; projectName?: string; projectId?: number;
  assignedTo?: User; assignedBy?: User; createdAt?: string; updatedAt?: string;
}

export interface WorkLog {
  id: number; description: string; plannedForTomorrow?: string; blockers?: string;
  hoursSpent?: number; logDate: string; user: User; task?: { id: number; title: string };
}

export interface Artifact {
  id: number; fileName: string; storagePath?: string; fileSize?: number;
  contentType?: string; description?: string; version: number;
  uploadedBy: User; task?: { id: number; title: string }; createdAt?: string;
}

export interface Comment {
  id: number; content: string; author: User;
  task?: { id: number }; createdAt: string;
}

export interface Notification {
  id: number; title: string; message: string; type: string;
  read: boolean; createdAt: string;
}

export interface TaskStatusHistory {
  id: number; oldStatus: string; newStatus: string;
  remarks?: string; changedBy?: User; changedAt: string;
}

export interface DashboardData {
  totalTasks: number; completedTasks: number; inProgressTasks: number;
  pendingTasks: number; totalProjects?: number; activeProjects?: number;
  recentTasks?: Task[];
}
