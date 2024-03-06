import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProjectService } from 'src/app/service/project/project.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent {


  projectForm! : FormGroup;

  constructor(private formBuilder : FormBuilder ,
    private snackBar : MatSnackBar,
    private projectService : ProjectService,
    private router : Router){}

    ngOnInit(): void {
      this.projectForm = this.formBuilder.group({
        name: [null , [Validators.required]],
        description: [null , [Validators.required]],
      })
    }


    createProject(): void{

      const user = JSON.parse(localStorage.getItem('user'));
      const companyId = user.companyId;

      if(this.projectForm.valid){
        this.projectService.createProjectForCompany(companyId , this.projectForm.value).subscribe
        (res =>{
          if(res.name != null){
            this.snackBar.open("Project Posted Successfully!" , 'Close', {
              duration: 5000
            });
            this.router.navigateByUrl('/company-admin/dashboard');
          }else{
            this.snackBar.open(res.message , 'Close' , {duration:5000 , panelClass: 'error-snackbar'});
          }
        })
      }else{
        this.projectForm.markAllAsTouched();
      }
    }
}
