// src/app/services/flashcard.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Flashcard } from '../models/flashcard.model';

@Injectable({
  providedIn: 'root'
})
export class FlashcardService {
  private readonly API_URL = 'http://localhost:8080/api/flashcard';

  constructor(private http: HttpClient) {}

  findAll(): Observable<Flashcard[]> {
    return this.http.get<Flashcard[]>(this.API_URL);
  }

  create(flashcard: Flashcard): Observable<Flashcard> {
    return this.http.post<Flashcard>(this.API_URL, flashcard);
  }

  update(id: number, flashcard: Flashcard): Observable<Flashcard> {
    return this.http.put<Flashcard>(`${this.API_URL}/${id}`, flashcard);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
