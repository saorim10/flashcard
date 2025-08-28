package com.saorim.flashcard.dto;

import lombok.Data;

@Data
public class FlashcardRequest {

	private String question;
	private String answer;
	private Long categoryId;
	
}
