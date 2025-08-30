package com.saorim.flashcard.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saorim.flashcard.config.UserDetailsImpl;
import com.saorim.flashcard.dto.JwtResponse;
import com.saorim.flashcard.dto.LoginRequest;
import com.saorim.flashcard.dto.SignupRequest;
import com.saorim.flashcard.exception.ResourceNotFoundException;
import com.saorim.flashcard.model.User;
import com.saorim.flashcard.repository.UserRepository;
import com.saorim.flashcard.security.JwtTokenProvider;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Autentica um usuário e retorna o token JWT
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            String jwt = tokenProvider.generateToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return new JwtResponse(jwt,
                                 userDetails.getId(),
                                 userDetails.getUsername(),
                                 userDetails.getEmail());
                                 
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenciais inválidas");
        }
    }

    /**
     * Registra um novo usuário
     */
    public String registerUser(SignupRequest signUpRequest) {
        // Verificar se username já existe
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso!");
        }

        // Verificar se email já existe
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso!");
        }

        // Validar dados de entrada
        validateSignupRequest(signUpRequest);

        // Criar novo usuário
        User user = new User();
        user.setUsername(signUpRequest.getUsername().trim());
        user.setEmail(signUpRequest.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return "Usuário registrado com sucesso!";
    }

    /**
     * Valida se o token JWT é válido
     */
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    /**
     * Obtém o username do token JWT
     */
    public String getUsernameFromToken(String token) {
        return tokenProvider.getUsernameFromToken(token);
    }

    /**
     * Busca usuário por username
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));
    }

    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
    }

    /**
     * Verifica se username existe
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Verifica se email existe
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Atualiza senha do usuário
     */
    public void updatePassword(String username, String newPassword) {
        User user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Valida os dados de registro
     */
    private void validateSignupRequest(SignupRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("Username deve ter pelo menos 3 caracteres");
        }

        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email deve ser válido");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
    }
}