import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/';


@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http : HttpClient) { }


  createTask(projectId : number , taskCreateDto : any): Observable<any>{
    return this.http.post(URL + `projects/${projectId}/tasks` ,     
    {headers: this.createAuthorizationHeader()
    })
  }

  getAllTasksByProject(projectId : number) : Observable<any>{
    return this.http.get(URL + `projects/${projectId}/tasks` ,
    {headers: this.createAuthorizationHeader()
    })
  }

  getTaskDetails(projectId : number , taskId : number) : Observable<any> {
    return this.http.get(URL + `projects/${projectId}/tasks/${taskId}/details` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  getTasksByUserId(assignedId : number) : Observable<any>{
    return this.http.get(URL + `users/${assignedId}/all-tasks` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  getTasksByProjectForAUser(projectId : number , userId : number): Observable<any>{
    return this.http.get(URL + `projects/${projectId}/users/${userId}/tasks` ,
    {headers: this.createAuthorizationHeader()
    })
  }

  getTasksByTaskTag(tag : string): Observable<any>{
    const param = new HttpParams().set('tag', tag);
    return this.http.get(URL + `tasks` , 
    {params : param , headers : this.createAuthorizationHeader()
    })
  }

  getTasksByTaskStatus(status : string): Observable<any>{
    const param = new HttpParams().set('status', status);
    return this.http.get(URL + `tasks` , 
    {params : param , headers : this.createAuthorizationHeader()
    })
  }

  getTasksByDate(startDate: Date, endDate: Date): Observable<any> {
    let params = new HttpParams()
      .set('startDate', startDate.toISOString())
      .set('endDate', endDate.toISOString());

      return this.http.get(URL + `tasks` , 
      {params : params , headers : this.createAuthorizationHeader()
      })
  }

  getUsersAssignedToATask(projectId : number , taskId : number): Observable<any>{
    return this.http.get(URL + `projects/${projectId}/tasks/${taskId}/assigned-users`,
    {headers: this.createAuthorizationHeader()
    }) 
  }

  assignAUserForTask(projectId : number , taskId : number , userId : number): Observable<any>{
    return this.http.put(URL + `projects/${projectId}/tasks/${taskId}/assignee/${userId}` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  unassignAUserForTask(projectId : number , taskId : number , userId : number): Observable<any>{
    return this.http.put(URL + `projects/${projectId}/tasks/${taskId}/unassignee/${userId}` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  updateTask(projectId : number , taskId : number , taskUpdateDto ): Observable<any>{
    return this.http.put(URL + `projects/${projectId}/tasks/${taskId}`, taskUpdateDto , 
    {headers: this.createAuthorizationHeader()
    })
  }

  changeTaskStatus(taskId : number , status : string): Observable<any>{
    const param = new HttpParams().set('status', status);
    return this.http.put(URL + `tasks/${taskId}/status`,
    {params : param ,headers: this.createAuthorizationHeader()
    })
  }

  deleteTask(projectId : number , taskId : number): Observable <any> {
    return this.http.delete(URL + `projects/${projectId}/tasks/${taskId}`,
    {headers: this.createAuthorizationHeader()
    })
  }



  private createAuthorizationHeader(): HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer ' + StorageService.getToken()
    )
  }
}
