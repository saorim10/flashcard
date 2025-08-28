package com.saorim.flashcard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saorim.flashcard.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserame (String username);
	Optional<User> findByEmail (String email);
	Boolean existsByUsername (String username);
	Boolean existsByEmail (String email);
}
