package com.saorim.flashcard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.saorim.flashcard.model.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

	List<Flashcard> findByUserId(Long userId);

	Optional<Flashcard> findByIdAndUserId(Long id, Long userId);

	List<Flashcard> findByCategoryIdAndUserId(Long categoryId, Long userId);

	@Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId ORDER BY f.lastReviewed ASC NULLS FIRST")
	List<Flashcard> findDueForReviewByUserId(Long userId);

	@Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId AND f.category.id = :categoryId ORDER BY f.lastReviewed ASC NULLS FIRST")
	List<Flashcard> findDueForReviewByCategoryId(Long userId, Long categoryId);

	@Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId AND " +
		       "(LOWER(f.question) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
		       "LOWER(f.answer) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
	List<Flashcard> searchByQuestionOrAnswer(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);

	@Query("SELECT COUNT(f) FROM Flashcard f WHERE f.user.id = :userId AND f.lastReviewed IS NOT NULL")
	long countReviewedFlashcards(@Param("userId") Long userId);

	@Query("SELECT COUNT(f) FROM Flashcard f WHERE f.user.id = :userId AND f.lastReviewed IS NULL")
	long countUnreviewedFlashcards(@Param("userId") Long userId);

	void deleteByUserId(Long userId);

}
