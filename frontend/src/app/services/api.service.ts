import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getEmployees(): Observable<any> {
    return this.http.get(`${this.apiUrl}/employees`);
  }

  getLocations(): Observable<any> {
    return this.http.get(`${this.apiUrl}/employees/locations`);
  }

  addEmployee(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/employees`, payload);
  }

  addLocation(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/employees/locations`, payload);
  }

  saveAttendance(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/attendance`, payload);
  }

  uploadAttendanceWorkbook(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(`${this.apiUrl}/attendance/upload`, formData);
  }

  saveAdvance(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/finance/advance`, payload);
  }

  saveExpense(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/finance/expense`, payload);
  }

  saveTravel(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/finance/travel`, payload);
  }

  getEmployeeReport(employeeId: number, start: string, end: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reports/employee/${employeeId}?start=${start}&end=${end}`);
  }

  getLocationReport(locationId: number, start: string, end: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reports/location/${locationId}?start=${start}&end=${end}`);
  }

  getConsolidatedReport(start: string, end: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reports/consolidated?start=${start}&end=${end}`);
  }
 updateEmployee(id: number, payload: any): Observable<any> {
  return this.http.put(`${this.apiUrl}/employees/${id}`, payload);
}

deleteEmployee(id: number): Observable<any> {
  return this.http.delete(`${this.apiUrl}/employees/${id}`);
}

 getQuotationRates(): Observable<any> {
    return this.http.get(`${this.apiUrl}/quotations/rates`);
  }

  createQuotation(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/quotations`, payload);
  }

  downloadQuotationPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/quotations/${id}/pdf`, {
      responseType: 'blob'
    });
  }
}
