import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from '../storage/storage.service';

const URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http : HttpClient) { }

  createCompany(companyInfoDto : any): Observable<any>{
    return this.http.post(URL + `companies` ,  companyInfoDto,
    {headers : this.createAuthorizationHeader(),
    }) 
  }

  getAllCompanies(): Observable<any>{
    return this.http.get(URL + `app/companies` ,
    {headers : this.createAuthorizationHeader(),
    })
  }

  getCompanyByCompanyName(companyName: string): Observable<any>{
    const params = new HttpParams().set('companyName', companyName);
    return this.http.get(URL + `app/companies` , 
    {params : params , headers : this.createAuthorizationHeader(),
    })
  }

  getPendingCompanies(): Observable<any>{
    return this.http.get(URL + `app/companies/pending-companies` , 
    {headers : this.createAuthorizationHeader(),
    })
  }

  getCompanyProjectsByCompanyId(companyId : number): Observable<any>{
    return this.http.get(URL+`companies/${companyId}/projects`, 
    {headers: this.createAuthorizationHeader()
    })
  }

  getCompanyEmployeesByCompanyId(companyId : number): Observable<any>{
    return this.http.get(URL+`companies/${companyId}/employee`, 
    {headers: this.createAuthorizationHeader()
    })
  }


  addEmployeeForCompany(companyId : number , userId : number): Observable<any>{
    return this.http.patch(URL + `companies/${companyId}/add-employee/${userId}` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  removeEmployeeForCompany(companyId : number , userId : number): Observable<any>{
    return this.http.patch(URL + `companies/${companyId}/remove-employee/${userId}` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  approveCompany(companyId : number): Observable<any>{
    return this.http.put(URL + `app/companies/${companyId}/approve` , 
    {headers: this.createAuthorizationHeader()
    })
  }

  updateCompany(companyId : number , companyInfoDto : any): Observable <any> {
    return this.http.put(URL + `companies/${companyId}` , companyInfoDto ,
    {headers: this.createAuthorizationHeader()
    })
  }

  deleteCompany(companyId : number): Observable <any> {
    return this.http.delete(URL + `companies/${companyId}`,
    {headers: this.createAuthorizationHeader()
    })
  }

  private createAuthorizationHeader(): HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer ' + StorageService.getToken()
    )
  }
}
