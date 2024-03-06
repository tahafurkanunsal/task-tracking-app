import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyAdminComponent } from './company-admin.component';
import { CompanyAdminDashboardComponent } from './company-admin-dashboard/company-admin-dashboard.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DemoAngularMaterailModule } from '../DemoAngularMaterialModule';
import { CompanyAdminRoutingModule } from './company-admin-routing.module';
import { CreateProjectComponent } from './create-project/create-project.component';


@NgModule({
  declarations: [
    CompanyAdminComponent,
    CompanyAdminDashboardComponent,
    CreateProjectComponent,
  ],
  imports: [
    CommonModule,
    CompanyAdminRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    DemoAngularMaterailModule
  ]
})
export class CompanyAdminModule { }
