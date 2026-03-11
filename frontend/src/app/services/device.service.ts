import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Device, Page, ShelfPosition } from '../models/device.model';
import { Observable } from 'rxjs';
import { Shelf } from '../models/shelf.model';

@Injectable({
  providedIn: 'root'
})
export class DeviceService implements OnInit {
    private readonly baseUrl = 'http://localhost:8080/api/devices';

    // devices: Observable<Device[]> | null = null;
    
    constructor(private http: HttpClient) { }

    public ngOnInit(): void {
        // this.devices = this.getDevices();
    }
    
    public getDevicePage(page: number, size: number, search: string = ''): Observable<Page<Device>> {
        let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
        if (search) {
            params = params.set('search', search);
        }
        return this.http.get<Page<Device>>(this.baseUrl, { params });
    }
    
    public getDevices(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/all`);
    }

    public getDevice(id: string): Observable<Device> {
        return this.http.get<Device>(`${this.baseUrl}/${id}`);
    }

    public getDeviceSummary(id: string): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/${id}`);
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
    
    
    updateShelfPosition(payload: ShelfPosition): Observable<ShelfPosition> {
    // updateShelfPosition(spId: string, shelfId: string) {
        // let params = new HttpParams()
        // .set('shelfId', shelfId);
        return this.http.put<ShelfPosition>(`${this.baseUrl}/shelfPositions/update/${payload.shelfPositionId}`, payload); 
    }
    
    deleteShelfPosition(spId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/shelfPositions/delete/${spId}`);
    }
}
///update/{shelfPositionId}/{shelfId}