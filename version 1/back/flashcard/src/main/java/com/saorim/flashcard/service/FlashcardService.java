package com.saorim.flashcard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.saorim.flashcard.model.Flashcard;
import com.saorim.flashcard.repository.FlashcardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlashcardService {

	private final FlashcardRepository repository;


	public List<Flashcard> findAll() {
		return repository.findAll();
	}

	public Flashcard findById(Long id) {
		return repository.findById(id).orElseThrow();
	}

	public Flashcard create(Flashcard flashcard) {
		return repository.save(flashcard);
	}

	public Flashcard update(Long id, Flashcard novo) {
		Flashcard atual = repository.findById(id).orElseThrow();
		atual.setPergunta(novo.getPergunta());
		atual.setResposta(novo.getResposta());
		atual.setCategoria(novo.getCategoria());
		return repository.save(atual);
	}

	public void remove(Long id) {
		repository.deleteById(id);
	}

}
