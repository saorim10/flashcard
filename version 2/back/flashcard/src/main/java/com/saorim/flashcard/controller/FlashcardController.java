package com.saorim.flashcard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saorim.flashcard.model.Flashcard;
import com.saorim.flashcard.service.FlashcardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/flashcards")
@SecurityRequirement(name = "bearerAuth")
public class FlashcardController {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PostMapping
    @Operation(summary = "Create a new flashcard")
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody Flashcard flashcard, Authentication authentication) {
        return ResponseEntity.ok(flashcardService.createFlashcard(flashcard, authentication.getName()));
    }

    @GetMapping
    @Operation(summary = "Get all flashcards for the current user")
    public ResponseEntity<List<Flashcard>> getAllFlashcards(Authentication authentication) {
        return ResponseEntity.ok(flashcardService.getAllFlashcards(authentication.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific flashcard by ID")
    public ResponseEntity<Flashcard> getFlashcard(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(flashcardService.getFlashcard(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a flashcard")
    public ResponseEntity<Flashcard> updateFlashcard(@PathVariable Long id, 
                                                   @RequestBody Flashcard flashcard,
                                                   Authentication authentication) {
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, flashcard, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a flashcard")
    public ResponseEntity<?> deleteFlashcard(@PathVariable Long id, Authentication authentication) {
        flashcardService.deleteFlashcard(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/random")
    @Operation(summary = "Get a random flashcard")
    public ResponseEntity<Flashcard> getRandomFlashcard(Authentication authentication) {
        return ResponseEntity.ok(flashcardService.getRandomFlashcard(authentication.getName()));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get flashcards by category")
    public ResponseEntity<List<Flashcard>> getFlashcardsByCategory(@PathVariable Long categoryId,
                                                                  Authentication authentication) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByCategory(categoryId, authentication.getName()));
    }
    
    @PostMapping("/{id}/review")
    @Operation(summary = "Mark flashcard as reviewed")
    public ResponseEntity<String> markAsReviewed(@PathVariable Long id, Authentication authentication) {
        flashcardService.updateReviewStatus(id, authentication.getName());
        return ResponseEntity.ok("Flashcard marcado como revisado!");
    }

    @GetMapping("/due-for-review")
    @Operation(summary = "Get flashcards due for review")
    public ResponseEntity<List<Flashcard>> getFlashcardsDueForReview(Authentication authentication) {
        List<Flashcard> flashcards = flashcardService.getFlashcardsDueForReview(authentication.getName());
        return ResponseEntity.ok(flashcards);
    }

    @GetMapping("/due-for-review/category/{categoryId}")
    @Operation(summary = "Get flashcards due for review by category")
    public ResponseEntity<List<Flashcard>> getFlashcardsDueForReviewByCategory(@PathVariable Long categoryId, 
                                                                              Authentication authentication) {
        List<Flashcard> flashcards = flashcardService.getFlashcardsDueForReviewByCategory(categoryId, authentication.getName());
        return ResponseEntity.ok(flashcards);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search flashcards by question or answer content")
    public ResponseEntity<List<Flashcard>> searchFlashcards(@RequestParam String q, Authentication authentication) {
        List<Flashcard> flashcards = flashcardService.searchFlashcards(q, authentication.getName());
        return ResponseEntity.ok(flashcards);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get flashcards statistics")
    public ResponseEntity<FlashcardService.FlashcardStats> getFlashcardStats(Authentication authentication) {
        FlashcardService.FlashcardStats stats = flashcardService.getFlashcardStats(authentication.getName());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate a flashcard")
    public ResponseEntity<Flashcard> duplicateFlashcard(@PathVariable Long id, Authentication authentication) {
        Flashcard duplicated = flashcardService.duplicateFlashcard(id, authentication.getName());
        return ResponseEntity.ok(duplicated);
    }

    @PostMapping("/{id}/reset-review")
    @Operation(summary = "Reset flashcard review status")
    public ResponseEntity<String> resetReviewStatus(@PathVariable Long id, Authentication authentication) {
        flashcardService.resetFlashcardReviewStatus(id, authentication.getName());
        return ResponseEntity.ok("Status de revisão resetado!");
    }

    @GetMapping("/random/category/{categoryId}")
    @Operation(summary = "Get random flashcard from specific category")
    public ResponseEntity<Flashcard> getRandomFlashcardByCategory(@PathVariable Long categoryId, Authentication authentication) {
        Flashcard flashcard = flashcardService.getRandomFlashcardByCategory(categoryId, authentication.getName());
        return ResponseEntity.ok(flashcard);
    }
}
