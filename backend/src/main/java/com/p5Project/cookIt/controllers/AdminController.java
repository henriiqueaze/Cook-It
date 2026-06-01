package com.p5Project.cookIt.controllers;

import com.p5Project.cookIt.dtos.AdminSummaryDTO;
import com.p5Project.cookIt.dtos.BannedWordDTO;
import com.p5Project.cookIt.dtos.CommentDTO;
import com.p5Project.cookIt.dtos.RecipeDTO;
import com.p5Project.cookIt.dtos.UserDTO;
import com.p5Project.cookIt.dtos.requests.CreateBannedWordRequest;
import com.p5Project.cookIt.entities.UserRole;
import com.p5Project.cookIt.exceptions.ForbiddenOperationException;
import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.mappers.BannedWordMapper;
import com.p5Project.cookIt.mappers.CommentMapper;
import com.p5Project.cookIt.mappers.RecipeMapper;
import com.p5Project.cookIt.mappers.UserMapper;
import com.p5Project.cookIt.repository.CommentRepository;
import com.p5Project.cookIt.repository.IngredientRepository;
import com.p5Project.cookIt.repository.RecipeRepository;
import com.p5Project.cookIt.repository.UserRepository;
import com.p5Project.cookIt.services.ModerationService;
import com.p5Project.cookIt.services.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ModerationService moderationService;
    private final BannedWordMapper bannedWordMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeService recipeService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @GetMapping("/summary")
    @Transactional(readOnly = true)
    public AdminSummaryDTO getSummary() {
        List<com.p5Project.cookIt.entities.User> users = userRepository.findAll();

        return new AdminSummaryDTO(
                users.size(),
                users.stream().filter(user -> user.getRole() == UserRole.ADMIN).count(),
                users.stream().filter(com.p5Project.cookIt.entities.User::isBanned).count(),
                recipeRepository.count(),
                ingredientRepository.count(),
                commentRepository.count(),
                moderationService.getAllBannedWords().size()
        );
    }

    @GetMapping("/users")
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @PutMapping("/users/{id}/ban")
    @Transactional
    public UserDTO banUser(@PathVariable String id) {
        return updateUserBanStatus(id, true);
    }

    @PutMapping("/users/{id}/unban")
    @Transactional
    public UserDTO unbanUser(@PathVariable String id) {
        return updateUserBanStatus(id, false);
    }

    @GetMapping("/recipes")
    @Transactional(readOnly = true)
    public List<RecipeDTO> listRecipes() {
        return recipeService.getAllRecipes(Pageable.unpaged()).getContent();
    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/comments")
    @Transactional(readOnly = true)
    public List<CommentDTO> listComments() {
        return commentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable String id) {
        commentRepository.deleteById(id);
    }

    @GetMapping("/banned-words")
    @Transactional(readOnly = true)
    public List<BannedWordDTO> listBannedWords() {
        return bannedWordMapper.toDTOList(moderationService.getAllBannedWords());
    }

    @PostMapping("/banned-words")
    public BannedWordDTO createBannedWord(@Valid @RequestBody CreateBannedWordRequest request) {
        return bannedWordMapper.toDTO(moderationService.createBannedWord(request));
    }

    @PutMapping("/banned-words/{id}")
    public BannedWordDTO updateBannedWord(@PathVariable String id, @Valid @RequestBody CreateBannedWordRequest request) {
        return bannedWordMapper.toDTO(moderationService.updateBannedWord(id, request));
    }

    @DeleteMapping("/banned-words/{id}")
    public void deleteBannedWord(@PathVariable String id) {
        moderationService.deleteBannedWord(id);
    }

    private UserDTO updateUserBanStatus(String id, boolean banned) {
        var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == UserRole.ADMIN && banned) {
            throw new ForbiddenOperationException("Administrators cannot be banned from the admin panel");
        }

        user.setBanned(banned);
        return userMapper.toDTO(userRepository.save(user));
    }
}
