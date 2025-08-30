package com.saorim.flashcard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FlashcardRequest {
    @NotBlank(message = "Pergunta é obrigatória")
    @Size(max = 1000, message = "Pergunta não pode ter mais de 1000 caracteres")
    private String question;
    
    @NotBlank(message = "Resposta é obrigatória")
    @Size(max = 1000, message = "Resposta não pode ter mais de 1000 caracteres")
    private String answer;
    
    private Long categoryId;
}