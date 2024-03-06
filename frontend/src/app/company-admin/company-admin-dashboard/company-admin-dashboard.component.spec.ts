import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyAdminDashboardComponent } from './company-admin-dashboard.component';

describe('CompanyAdminDashboardComponent', () => {
  let component: CompanyAdminDashboardComponent;
  let fixture: ComponentFixture<CompanyAdminDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyAdminDashboardComponent]
    });
    fixture = TestBed.createComponent(CompanyAdminDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
