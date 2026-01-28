import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CreatePasteRequest {
  content: string;
  ttl_seconds?: number;
  max_views?: number;
}

export interface CreatePasteResponse {
  id: string;
  url: string;
}

export interface GetPasteResponse {
  content: string;
  remainingViews?: number;  
  expiresAt?: string;        
}

@Injectable({
  providedIn: 'root'
})
export class PasteService {

  private readonly BASE_URL = environment.apiBaseUrl;;

  constructor(private http: HttpClient) {}

  createPaste(payload: CreatePasteRequest): Observable<CreatePasteResponse> {
    return this.http.post<CreatePasteResponse>(
      `${this.BASE_URL}/pastes`,
      payload
    );
  }

  getPaste(id: string): Observable<GetPasteResponse> {
  return this.http.get<GetPasteResponse>(
    `${this.BASE_URL}/pastes/${id}`
  );
}
}
