import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from './service/storage/storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'TaskTrackingFrontend';


  isUserLoggedIn : boolean = StorageService.isUserLoggedIn();
  isAdminLoggedIn : boolean = StorageService.isAdminLoggedIn();
  isCompanyAdminLoggedIn : boolean = StorageService.isCompanyAdminLoggedIn();

  constructor(private router : Router){}
  
  ngOnInit(): void {
    this.router.events.subscribe(event =>{
      this.isUserLoggedIn = StorageService.isUserLoggedIn();
      this.isAdminLoggedIn = StorageService.isAdminLoggedIn();
      this.isCompanyAdminLoggedIn = StorageService.isCompanyAdminLoggedIn();
    })
  }

  logout(){
    StorageService.signOut();
    this.router.navigateByUrl('login');
  }
}
