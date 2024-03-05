import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/auth/';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http : HttpClient , private storageService : StorageService) { }

  register(signupRequest : any): Observable<any>{
    return this.http.post(URL + "signup", signupRequest);
  }

  login(email : string , password : string): Observable<boolean>{
    const headers = new HttpHeaders().set('Content-Type' , 'application/json');
    const body = {email , password};

    return this.http.post<any>(URL+ 'login', body , { headers , observe: 'response'}).pipe(
      map((res) => {
        const token = res.headers.get('Authorization')?.substring(7);
        const user = res.body;
        if(token && user){
          this.storageService.saveToken(token);
          this.storageService.saveUser(user);
          return true;
        }
        return false;
      })
    );
  }
}