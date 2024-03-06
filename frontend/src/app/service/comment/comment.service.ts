import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http : HttpClient) { }

  addCommentToTask(projectId : number , taskId : number , commentDto : any): Observable<any>{
    return this.http.put(URL + `projects/${projectId}/tasks/${taskId}/comments` , commentDto , 
    {headers: this.createAuthorizationHeader()
    })
  }

  getAllComments(): Observable<any>{
    return this.http.get(URL + `comments` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  getCommentsByTaskIdAndProjectId(projectId : number , taskId : number): Observable<any>{
    return this.http.get(URL + `projects/${projectId}/tasks/${taskId}/comments` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  updateComment(projectId : number , taskId : number , commentId : number, commentDto : any): Observable<any>{
    return this.http.put(URL + `projects/${projectId}/tasks/${taskId}/comments/${commentId}`, commentDto ,
    {headers: this.createAuthorizationHeader()
    })
  }

  deleteComment(projectId : number , taskId : number , commentId : number): Observable<any>{
    return this.http.delete(URL + `projects/${projectId}/tasks/${taskId}/comments/${commentId}` , 
    {headers: this.createAuthorizationHeader()
    })
  }


  private createAuthorizationHeader(): HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer ' + StorageService.getToken()
    )
  }
}
