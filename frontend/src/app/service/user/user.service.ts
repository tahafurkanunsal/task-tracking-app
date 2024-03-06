import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http : HttpClient) { }


  getAllUsers(): Observable<any>{
    return this.http.get(URL + `users` , 
    {headers : this.createAuthorizationHeader(),
    })
  }

  updateUser(userId : number , userUpdateDto : any): Observable<any>{
    return this.http.put(URL + `users/${userId}` , userUpdateDto , {
      headers : this.createAuthorizationHeader()
    })
  }

  deleteUser(userId : number): Observable<any>{
    return this.http.delete(URL + `users/${userId}` , 
    {headers : this.createAuthorizationHeader()
    })
  }


  private createAuthorizationHeader(): HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer ' + StorageService.getToken()
    )
  }
}
