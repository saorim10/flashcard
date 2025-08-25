package com.saorim.flashcard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saorim.flashcard.model.Flashcard;
import com.saorim.flashcard.service.FlashcardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flashcard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FlashcardController {

	private final FlashcardService service;

	@GetMapping
    public List<Flashcard> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Flashcard create(@RequestBody Flashcard flashcard) {
        return service.create(flashcard);
    }

    @PutMapping("/{id}")
    public Flashcard update(@PathVariable Long id, @RequestBody Flashcard flashcard) {
        return service.update(id, flashcard);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }

}
