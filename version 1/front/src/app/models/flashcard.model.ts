// src/app/models/flashcard.model.ts
export interface Flashcard {
  id?: number;
  pergunta: string;
  resposta: string;
  categoria: string;
  dataCriacao?: string;
}
