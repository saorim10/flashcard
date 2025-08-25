package com.saorim.flashcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saorim.flashcard.model.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

}
