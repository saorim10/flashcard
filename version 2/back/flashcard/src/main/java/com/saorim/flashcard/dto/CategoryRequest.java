package com.saorim.flashcard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 100, message = "Nome da categoria não pode ter mais de 100 caracteres")
    private String name;
    
    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    private String description;
}