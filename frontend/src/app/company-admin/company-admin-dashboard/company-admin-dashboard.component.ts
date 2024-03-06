import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';

import { ProjectService } from 'src/app/service/project/project.service';
import { StorageService } from 'src/app/service/storage/storage.service';
import { TaskService } from 'src/app/service/task/task.service';


@Component({
  selector: 'app-company-admin-dashboard',
  templateUrl: './company-admin-dashboard.component.html',
  styleUrls: ['./company-admin-dashboard.component.scss']
})
export class CompanyAdminDashboardComponent {

  projects: any[] = [];
  tasks: any[] = [];
  companyId: number;

  constructor(
    private projectService: ProjectService,
    private taskService: TaskService,
    private activatedRoute: ActivatedRoute,
    private dialog: MatDialog
  ) {
    this.companyId = StorageService.getCompany()
  }


  ngOnInit() {
    
    this.getAllProjects();
  }

  getAllProjects() {
    const user = JSON.parse(localStorage.getItem('user'));
    const companyId = user.companyId;
    this.projects = [];
    this.projectService.getAllProjectsByCompany(companyId).subscribe(res => {
      res.forEach(project => {
        this.projects.push(project);
        this.getAllTasksByProject(project.id);
      });
    });
  }

  getAllTasksByProject(projectId: number) {
    this.taskService.getAllTasksByProject(projectId).subscribe(res => {
      res.forEach(task => {
        this.tasks.push(task);
      });
    });
  }
}
