import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlashcardDialog } from './flashcard-dialog';

describe('FlashcardDialog', () => {
  let component: FlashcardDialog;
  let fixture: ComponentFixture<FlashcardDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FlashcardDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FlashcardDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
