import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable, OnInit } from "@angular/core";
import { Page } from "../models/device.model";
import { Shelf } from "../models/shelf.model";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root' 
})
export class ShelfService implements OnInit {
    private readonly baseUrl = 'http://localhost:8080/api/shelves';
    
    // public shelves: Observable<Shelf[]> | null = null;
    
    public ngOnInit(): void {
    //     this.shelves = this.getShelves();
    }
    
    constructor(private http: HttpClient) { }
    
    public getShelfPage(page: number, size: number, search: string = ''):Observable<Page<Shelf>> {
        let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());
        
        if (search) {
            params = params.set('search', search);
        }
        return this.http.get<Page<Shelf>>(this.baseUrl, { params });
    }
    
    public getShelves(): Observable<Shelf[]> {
        return this.http.get<Shelf[]>(`${this.baseUrl}/all`);
    }
    
    public getShelf(id: string): Observable<Shelf> {
        return this.http.get<Shelf>(`${this.baseUrl}/${id}`);
    }
    
    public createShelf(shelf: Shelf): Observable<Shelf> {
        return this.http.post<Shelf>(`${this.baseUrl}/create`, shelf);
    }
    
    public updateShelf(id: string, shelf: Shelf): Observable<Shelf> {  
        return this.http.put<Shelf>(`${this.baseUrl}/${id}`, shelf);
    }
    
    public deleteShelf(id: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}