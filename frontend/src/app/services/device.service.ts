import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Device, Page } from '../models/device.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeviceService {
    private readonly baseUrl = 'http://localhost:8080/api/devices';

    constructor(private http: HttpClient) { }

    public getDevices(page: number, size: number, search: string = ''): Observable<Page<Device>> {
        let params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());
        if (search) {
            params = params.set('search', search);
        }
        return this.http.get<Page<Device>>(this.baseUrl, { params });
    }

    public getDevice(id: string): Observable<Device> {
        return this.http.get<Device>(`${this.baseUrl}/${id}`);
    }

    public createDevice(device: Device): Observable<Device> {
        return this.http.post<Device>(`${this.baseUrl}/create`, device);
    }

    public updateDevice(id: string, device: Device): Observable<Device> {
        return this.http.put<Device>(`${this.baseUrl}/${id}`, device);
    }

    public deleteDevice(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}