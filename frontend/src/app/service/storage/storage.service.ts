import { Injectable } from '@angular/core';

const TOKEN = 'token';
const USER = 'user';
const COMPANY = 'companyId'

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

public saveToken(token: string): void {
  window.localStorage.removeItem(TOKEN);
  window.localStorage.setItem(TOKEN , token);
  }

  public saveUser(user): void {
  window.localStorage.removeItem(USER);
  window.localStorage.setItem(USER , JSON.stringify(user));

  }

  public saveCompany(company): void {
    window.localStorage.removeItem(COMPANY);
    window.localStorage.setItem(COMPANY , company);
  }

  static getToken(): string {
    return localStorage.getItem(TOKEN);
  }
  static getUser(): any {
    return JSON.parse(localStorage.getItem(USER));
  }

  static getCompany(): any {
    return JSON.parse(localStorage.getItem(COMPANY));
  }
  

  static getUserId(): string {
    const user = this.getUser();
    if( user == null){
      return '';
    }
    return user.userId;
  }

  static getUserRole(): string {
    const user = this.getUser();
    if( user == null){
      return '';
    }
    return user.role;
  }

  static isAdminLoggedIn(): boolean {
    if(this.getToken() == null){
      return false;
    }
    const role: string = this.getUserRole();
    return role == 'ADMIN';
  }

  static isCompanyAdminLoggedIn(): boolean {
    if(this.getToken() == null){
      return false;
    }
    const role : string = this.getUserRole();
    return role == 'COMPANY_ADMIN'
  }
  
  static isUserLoggedIn(): boolean {
    if(this.getToken() == null){
      return false;
    }
    const role: string = this.getUserRole();
    return role == 'USER';
  } 

  static signOut(): void{
    window.localStorage.removeItem(TOKEN);
    window.localStorage.removeItem(USER);
    
  } 
}