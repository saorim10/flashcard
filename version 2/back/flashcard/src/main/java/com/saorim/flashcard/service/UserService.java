package com.saorim.flashcard.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saorim.flashcard.exception.ResourceNotFoundException;
import com.saorim.flashcard.model.User;
import com.saorim.flashcard.repository.CategoryRepository;
import com.saorim.flashcard.repository.FlashcardRepository;
import com.saorim.flashcard.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FlashcardRepository flashcardRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                      CategoryRepository categoryRepository,
                      FlashcardRepository flashcardRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.flashcardRepository = flashcardRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca todos os usuários
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Busca usuário por username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + username));
    }

    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
    }

    /**
     * Cria um novo usuário
     */
    public User createUser(User user) {
        // Validar se username já existe
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso: " + user.getUsername());
        }

        // Validar se email já existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso: " + user.getEmail());
        }

        // Criptografar senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    /**
     * Atualiza dados do usuário
     */
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Verificar se o novo username já existe (se foi alterado)
        if (!user.getUsername().equals(userDetails.getUsername()) 
            && userRepository.existsByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso: " + userDetails.getUsername());
        }

        // Verificar se o novo email já existe (se foi alterado)
        if (!user.getEmail().equals(userDetails.getEmail()) 
            && userRepository.existsByEmail(userDetails.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso: " + userDetails.getEmail());
        }

        // Atualizar campos
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);
    }

    /**
     * Atualiza senha do usuário
     */
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        User user = getUserById(id);

        // Verificar senha atual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        // Validar nova senha
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
        }

        // Atualizar senha
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Atualiza senha do usuário por username
     */
    public void updatePasswordByUsername(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);
        updatePassword(user.getId(), currentPassword, newPassword);
    }

    /**
     * Deleta usuário e todos os seus dados
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        
        // Deletar flashcards do usuário
        flashcardRepository.deleteByUserId(id);
        
        // Deletar categorias do usuário
        categoryRepository.deleteByUserId(id);
        
        // Deletar usuário
        userRepository.delete(user);
    }

    /**
     * Deleta usuário por username
     */
    public void deleteUserByUsername(String username) {
        User user = getUserByUsername(username);
        deleteUser(user.getId());
    }

    /**
     * Verifica se usuário existe por username
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Verifica se usuário existe por email
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Conta total de usuários
     */
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * Obtém estatísticas do usuário
     */
    @Transactional(readOnly = true)
    public UserStats getUserStats(String username) {
        User user = getUserByUsername(username);
        
        long totalCategories = categoryRepository.findByUserId(user.getId()).size();
        long totalFlashcards = flashcardRepository.findByUserId(user.getId()).size();
        
        return new UserStats(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            totalCategories,
            totalFlashcards
        );
    }

    /**
     * Classe interna para estatísticas do usuário
     */
    public static class UserStats {
        private Long userId;
        private String username;
        private String email;
        private long totalCategories;
        private long totalFlashcards;

        public UserStats(Long userId, String username, String email, long totalCategories, long totalFlashcards) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.totalCategories = totalCategories;
            this.totalFlashcards = totalFlashcards;
        }

        // Getters e Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public long getTotalCategories() { return totalCategories; }
        public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }
        
        public long getTotalFlashcards() { return totalFlashcards; }
        public void setTotalFlashcards(long totalFlashcards) { this.totalFlashcards = totalFlashcards; }
    }
}