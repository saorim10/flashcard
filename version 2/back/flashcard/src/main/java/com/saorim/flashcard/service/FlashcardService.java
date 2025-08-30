package com.saorim.flashcard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saorim.flashcard.model.Category;
import com.saorim.flashcard.model.Flashcard;
import com.saorim.flashcard.model.User;
import com.saorim.flashcard.repository.CategoryRepository;
import com.saorim.flashcard.repository.FlashcardRepository;
import com.saorim.flashcard.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public FlashcardService(FlashcardRepository flashcardRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.flashcardRepository = flashcardRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Flashcard createFlashcard(Flashcard flashcard, String username) {
        User user = getUserByUsername(username);
        flashcard.setUser(user);
        
        if (flashcard.getCategory() != null && flashcard.getCategory().getId() != null) {
            Category category = categoryRepository.findByIdAndUserId(
                flashcard.getCategory().getId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            flashcard.setCategory(category);
        }
        
        return flashcardRepository.save(flashcard);
    }

    public List<Flashcard> getAllFlashcards(String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findByUserId(user.getId());
    }

    public Flashcard getFlashcard(Long id, String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new EntityNotFoundException("Flashcard not found"));
    }

    public Flashcard updateFlashcard(Long id, Flashcard flashcardDetails, String username) {
        Flashcard flashcard = getFlashcard(id, username);
        
        flashcard.setQuestion(flashcardDetails.getQuestion());
        flashcard.setAnswer(flashcardDetails.getAnswer());
        
        if (flashcardDetails.getCategory() != null && flashcardDetails.getCategory().getId() != null) {
            Category category = categoryRepository.findByIdAndUserId(
                flashcardDetails.getCategory().getId(), flashcard.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            flashcard.setCategory(category);
        }
        
        return flashcardRepository.save(flashcard);
    }

    public void deleteFlashcard(Long id, String username) {
        Flashcard flashcard = getFlashcard(id, username);
        flashcardRepository.delete(flashcard);
    }

    public Flashcard getRandomFlashcard(String username) {
        User user = getUserByUsername(username);
        List<Flashcard> flashcards = flashcardRepository.findByUserId(user.getId());
        
        if (flashcards.isEmpty()) {
            throw new EntityNotFoundException("No flashcards found");
        }
        
        return flashcards.get(random.nextInt(flashcards.size()));
    }

    public List<Flashcard> getFlashcardsByCategory(Long categoryId, String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findByCategoryIdAndUserId(categoryId, user.getId());
    }

    public void updateReviewStatus(Long id, String username) {
        Flashcard flashcard = getFlashcard(id, username);
        flashcard.setLastReviewed(LocalDateTime.now());
        flashcard.setReviewCount(flashcard.getReviewCount() + 1);
        flashcardRepository.save(flashcard);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
    
    /**
     * Busca flashcards que precisam de revisão
     */
    public List<Flashcard> getFlashcardsDueForReview(String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findDueForReviewByUserId(user.getId());
    }

    /**
     * Busca flashcards que precisam de revisão por categoria
     */
    public List<Flashcard> getFlashcardsDueForReviewByCategory(Long categoryId, String username) {
        User user = getUserByUsername(username);
        
        // Verificar se a categoria pertence ao usuário
        categoryRepository.findByIdAndUserId(categoryId, user.getId())
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        
        return flashcardRepository.findDueForReviewByCategoryId(user.getId(), categoryId);
    }

    /**
     * Busca flashcards aleatórios por categoria
     */
    public Flashcard getRandomFlashcardByCategory(Long categoryId, String username) {
        User user = getUserByUsername(username);
        List<Flashcard> flashcards = flashcardRepository.findByCategoryIdAndUserId(categoryId, user.getId());
        
        if (flashcards.isEmpty()) {
            throw new EntityNotFoundException("No flashcards found in this category");
        }
        
        return flashcards.get(random.nextInt(flashcards.size()));
    }

    /**
     * Conta flashcards por usuário
     */
    public long countFlashcardsByUser(String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findByUserId(user.getId()).size();
    }

    /**
     * Conta flashcards por categoria
     */
    public long countFlashcardsByCategory(Long categoryId, String username) {
        User user = getUserByUsername(username);
        return flashcardRepository.findByCategoryIdAndUserId(categoryId, user.getId()).size();
    }

    /**
     * Obtém estatísticas de flashcards
     */
    public FlashcardStats getFlashcardStats(String username) {
        User user = getUserByUsername(username);
        List<Flashcard> allFlashcards = flashcardRepository.findByUserId(user.getId());
        
        long totalFlashcards = allFlashcards.size();
        long reviewedFlashcards = allFlashcards.stream()
            .filter(f -> f.getLastReviewed() != null)
            .count();
        long unreviewedFlashcards = totalFlashcards - reviewedFlashcards;
        
        return new FlashcardStats(totalFlashcards, reviewedFlashcards, unreviewedFlashcards);
    }

    /**
     * Busca flashcards por termo de pesquisa
     */
    public List<Flashcard> searchFlashcards(String searchTerm, String username) {
        User user = getUserByUsername(username);
        List<Flashcard> allFlashcards = flashcardRepository.findByUserId(user.getId());
        
        return allFlashcards.stream()
            .filter(f -> f.getQuestion().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        f.getAnswer().toLowerCase().contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Reseta contadores de revisão de um flashcard
     */
    public void resetFlashcardReviewStatus(Long id, String username) {
        Flashcard flashcard = getFlashcard(id, username);
        flashcard.setLastReviewed(null);
        flashcard.setReviewCount(0);
        flashcardRepository.save(flashcard);
    }

    /**
     * Duplica um flashcard
     */
    public Flashcard duplicateFlashcard(Long id, String username) {
        Flashcard original = getFlashcard(id, username);
        
        Flashcard duplicate = new Flashcard();
        duplicate.setQuestion(original.getQuestion() + " (Cópia)");
        duplicate.setAnswer(original.getAnswer());
        duplicate.setCategory(original.getCategory());
        duplicate.setUser(original.getUser());
        duplicate.setReviewCount(0);
        duplicate.setLastReviewed(null);
        
        return flashcardRepository.save(duplicate);
    }

    // ========== Classe FlashcardStats ==========
    public static class FlashcardStats {
        private long totalFlashcards;
        private long reviewedFlashcards;
        private long unreviewedFlashcards;
        
        public FlashcardStats(long totalFlashcards, long reviewedFlashcards, long unreviewedFlashcards) {
            this.totalFlashcards = totalFlashcards;
            this.reviewedFlashcards = reviewedFlashcards;
            this.unreviewedFlashcards = unreviewedFlashcards;
        }
        
        // Getters e Setters
        public long getTotalFlashcards() { return totalFlashcards; }
        public void setTotalFlashcards(long totalFlashcards) { this.totalFlashcards = totalFlashcards; }
        
        public long getReviewedFlashcards() { return reviewedFlashcards; }
        public void setReviewedFlashcards(long reviewedFlashcards) { this.reviewedFlashcards = reviewedFlashcards; }
        
        public long getUnreviewedFlashcards() { return unreviewedFlashcards; }
        public void setUnreviewedFlashcards(long unreviewedFlashcards) { this.unreviewedFlashcards = unreviewedFlashcards; }
    }
}
