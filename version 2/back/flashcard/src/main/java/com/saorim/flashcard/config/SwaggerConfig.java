package com.saorim.flashcard.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Flashcard API",
        version = "1.0",
        description = "API para gerenciamento de flashcards"
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
}

//package com.saorim.flashcard.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@OpenAPIDefinition(
//    info = @Info(
//        title = "Flashcard API",
//        version = "1.0.0",
//        description = "API RESTful para gerenciamento de flashcards com sistema de categorias e revisão",
//        contact = @Contact(
//            name = "Equipe de Desenvolvimento",
//            email = "dev@flashcard.com",
//            url = "https://github.com/saorim/flashcard"
//        ),
//        license = @License(
//            name = "MIT License",
//            url = "https://opensource.org/licenses/MIT"
//        )
//    ),
//    servers = {
//        @Server(
//            url = "http://localhost:8080",
//            description = "Servidor de Desenvolvimento"
//        )
//    }
//)
//@SecurityScheme(
//    name = "bearerAuth",
//    type = SecuritySchemeType.HTTP,
//    bearerFormat = "JWT",
//    scheme = "bearer",
//    description = "Token JWT para autenticação. Formato: 'Bearer {token}'"
//)
//public class SwaggerConfig {
//    
//}