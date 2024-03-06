import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyAdminDashboardComponent } from './company-admin-dashboard/company-admin-dashboard.component';
import { CompanyAdminComponent } from './company-admin.component';
import { CreateProjectComponent } from './create-project/create-project.component';

const routes: Routes = [
  { path: '', component: CompanyAdminComponent },
  { path: 'dashboard', component: CompanyAdminDashboardComponent },
  { path: 'addProject', component: CreateProjectComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyAdminRoutingModule { }
