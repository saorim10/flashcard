// src/app/components/flashcard-list/flashcard-list.component.ts
import { Component, OnInit } from '@angular/core';
import { FlashcardService } from '../../services/flashcard.service';
import { Flashcard } from '../../models/flashcard.model';

@Component({
  selector: 'app-flashcard-list',
  template: `
    <div class="container mt-4">
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Meus Flashcards</h2>
        <button class="btn btn-primary" (click)="showCreateForm()">
          <i class="fas fa-plus"></i> Novo Flashcard
        </button>
      </div>

      <!-- Formulário de Criação/Edição -->
      <div *ngIf="showForm" class="card mb-4">
        <div class="card-header">
          <h5>{{ editingFlashcard ? 'Editar' : 'Novo' }} Flashcard</h5>
        </div>
        <div class="card-body">
          <form (ngSubmit)="onSubmit()" #flashcardForm="ngForm">
            <div class="mb-3">
              <label for="pergunta" class="form-label">Pergunta</label>
              <textarea 
                class="form-control" 
                id="pergunta" 
                name="pergunta"
                [(ngModel)]="currentFlashcard.pergunta" 
                required
                rows="3"></textarea>
            </div>
            <div class="mb-3">
              <label for="resposta" class="form-label">Resposta</label>
              <textarea 
                class="form-control" 
                id="resposta" 
                name="resposta"
                [(ngModel)]="currentFlashcard.resposta" 
                required
                rows="3"></textarea>
            </div>
            <div class="mb-3">
              <label for="categoria" class="form-label">Categoria</label>
              <input 
                type="text" 
                class="form-control" 
                id="categoria" 
                name="categoria"
                [(ngModel)]="currentFlashcard.categoria" 
                required>
            </div>
            <div class="d-flex gap-2">
              <button type="submit" class="btn btn-success" [disabled]="!flashcardForm.form.valid">
                {{ editingFlashcard ? 'Atualizar' : 'Salvar' }}
              </button>
              <button type="button" class="btn btn-secondary" (click)="cancelForm()">
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Lista de Flashcards -->
      <div class="row">
        <div class="col-md-6 col-lg-4 mb-3" *ngFor="let flashcard of flashcards">
          <div class="card h-100" [class.flipped]="flashcard.id === flippedCard">
            <div class="card-body d-flex flex-column">
              <div class="card-content" *ngIf="flashcard.id !== flippedCard">
                <h6 class="card-subtitle mb-2 text-muted">{{ flashcard.categoria }}</h6>
                <p class="card-text">{{ flashcard.pergunta }}</p>
              </div>
              <div class="card-content" *ngIf="flashcard.id === flippedCard">
                <h6 class="card-subtitle mb-2 text-muted">Resposta</h6>
                <p class="card-text">{{ flashcard.resposta }}</p>
              </div>
              <div class="mt-auto">
                <div class="d-flex justify-content-between align-items-center">
                  <button 
                    class="btn btn-outline-primary btn-sm" 
                    (click)="flipCard(flashcard.id!)">
                    {{ flashcard.id === flippedCard ? 'Ver Pergunta' : 'Ver Resposta' }}
                  </button>
                  <div class="btn-group">
                    <button 
                      class="btn btn-outline-warning btn-sm" 
                      (click)="editFlashcard(flashcard)">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button 
                      class="btn btn-outline-danger btn-sm" 
                      (click)="deleteFlashcard(flashcard.id!)">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </div>
                <small class="text-muted mt-2 d-block">
                  Criado em: {{ formatDate(flashcard.dataCriacao!) }}
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div *ngIf="flashcards.length === 0" class="text-center py-5">
        <i class="fas fa-graduation-cap fa-3x text-muted mb-3"></i>
        <h4 class="text-muted">Nenhum flashcard encontrado</h4>
        <p class="text-muted">Comece criando seu primeiro flashcard!</p>
      </div>
    </div>
  `,
  styles: [`
    .card {
      transition: transform 0.9s ease;
    }
    .card:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    .flipped {
      background-color: #94f4caff;
    }
    .card-text {
      min-height: 60px;
    }
  `]
})
export class FlashcardListComponent implements OnInit {
  flashcards: Flashcard[] = [];
  currentFlashcard: Flashcard = this.getEmptyFlashcard();
  editingFlashcard: Flashcard | null = null;
  showForm = false;
  flippedCard: number | null = null;

  constructor(private flashcardService: FlashcardService) {}

  ngOnInit(): void {
    this.loadFlashcards();
  }

  loadFlashcards(): void {
    this.flashcardService.findAll().subscribe({
      next: (flashcards) => this.flashcards = flashcards,
      error: (error) => console.error('Erro ao carregar flashcards:', error)
    });
  }

  showCreateForm(): void {
    this.showForm = true;
    this.editingFlashcard = null;
    this.currentFlashcard = this.getEmptyFlashcard();
  }

  editFlashcard(flashcard: Flashcard): void {
    this.showForm = true;
    this.editingFlashcard = flashcard;
    this.currentFlashcard = { ...flashcard };
  }

  onSubmit(): void {
    if (this.editingFlashcard) {
      this.updateFlashcard();
    } else {
      this.createFlashcard();
    }
  }

  createFlashcard(): void {
    this.flashcardService.create(this.currentFlashcard).subscribe({
      next: () => {
        this.loadFlashcards();
        this.cancelForm();
      },
      error: (error) => console.error('Erro ao criar flashcard:', error)
    });
  }

  updateFlashcard(): void {
    this.flashcardService.update(this.editingFlashcard!.id!, this.currentFlashcard).subscribe({
      next: () => {
        this.loadFlashcards();
        this.cancelForm();
      },
      error: (error) => console.error('Erro ao atualizar flashcard:', error)
    });
  }

  deleteFlashcard(id: number): void {
    if (confirm('Tem certeza que deseja excluir este flashcard?')) {
      this.flashcardService.delete(id).subscribe({
        next: () => this.loadFlashcards(),
        error: (error) => console.error('Erro ao excluir flashcard:', error)
      });
    }
  }

  flipCard(id: number): void {
    this.flippedCard = this.flippedCard === id ? null : id;
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingFlashcard = null;
    this.currentFlashcard = this.getEmptyFlashcard();
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('pt-BR');
  }

  private getEmptyFlashcard(): Flashcard {
    return {
      pergunta: '',
      resposta: '',
      categoria: ''
    };
  }
}