import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="#">
          <i class="fas fa-graduation-cap me-2"></i>
          Flash Cards
        </a>
      </div>
    </nav>
    
    <app-flashcard-list></app-flashcard-list>
  `
})
export class AppComponent {
  title = 'flash-cards-app';
}