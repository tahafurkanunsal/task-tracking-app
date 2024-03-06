import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http : HttpClient) { }


  createProjectForCompany(companyId : number , projectCreateDto : any): Observable<any>{
    return this.http.post(URL + `companies/${companyId}/projects` ,  projectCreateDto,
    {headers : this.createAuthorizationHeader(),
    }) 
  }

  getAllProjectsByCompany(companyId : number): Observable<any> {
    return this.http.get(URL + `companies/${companyId}/all-projects` ,
        {headers : this.createAuthorizationHeader(),
    }) 
  }

  getProjectDetailsByCompany(companyId : number , projectId : number): Observable<any> {
    return this.http.get(URL + `companies/${companyId}/projects/${projectId}` ,
    {headers : this.createAuthorizationHeader(),
    })
  }

  updateProject(companyId : number , projectId : number , projectUpdateDto : any): Observable <any> {
    return this.http.put(URL + `companies/${companyId}/projects/${projectId}` , projectUpdateDto ,
    {headers: this.createAuthorizationHeader()
    })
  }

  deleteCompany(companyId : number , projectId : number): Observable <any> {
    return this.http.put(URL + `companies/${companyId}/projects/${projectId}`,
    {headers: this.createAuthorizationHeader()
    })
  }

  private createAuthorizationHeader(): HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer ' + StorageService.getToken()
    )
  }
}
