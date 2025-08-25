package com.saorim.flashcard.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Flashcard {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String pergunta;
	private String resposta;
	private String categoria;

	private LocalDateTime dataCriacao = LocalDateTime.now();
}
