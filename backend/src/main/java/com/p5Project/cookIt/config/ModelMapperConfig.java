package com.p5Project.cookIt.config;

import com.p5Project.cookIt.models.dtos.*;
import com.p5Project.cookIt.models.dtos.comment.CommentDTO;
import com.p5Project.cookIt.models.dtos.comment.CreateCommentDTO;
import com.p5Project.cookIt.models.dtos.image.CreateImageDTO;
import com.p5Project.cookIt.models.dtos.image.ImageDTO;
import com.p5Project.cookIt.models.dtos.ingredient.IngredientDTO;
import com.p5Project.cookIt.models.dtos.pantryItem.CreatePantryItemDTO;
import com.p5Project.cookIt.models.dtos.pantryItem.PantryItemDTO;
import com.p5Project.cookIt.models.dtos.rating.CreateRatingDTO;
import com.p5Project.cookIt.models.dtos.rating.RatingDTO;
import com.p5Project.cookIt.models.dtos.recipe.CreateRecipeDTO;
import com.p5Project.cookIt.models.dtos.recipe.RecipeDTO;
import com.p5Project.cookIt.models.dtos.recipeIngredient.RecipeIngredientCreateDTO;
import com.p5Project.cookIt.models.dtos.recipeIngredient.RecipeIngredientDTO;
import com.p5Project.cookIt.models.dtos.tag.CreateTagDTO;
import com.p5Project.cookIt.models.dtos.tag.TagDTO;
import com.p5Project.cookIt.models.dtos.user.UserDTO;
import com.p5Project.cookIt.models.entities.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();

        mm.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        addConverters(mm);
        configureTypeMaps(mm);

        return mm;
    }

    private Converter<UUID, User> uuidToUser() {
        return ctx -> {
            UUID id = ctx.getSource();
            if (id == null) return null;
            User u = new User();
            u.setId(id);
            return u;
        };
    }

    private Converter<UUID, Recipe> uuidToRecipe() {
        return ctx -> {
            UUID id = ctx.getSource();
            if (id == null) return null;
            Recipe r = new Recipe();
            r.setId(id);
            return r;
        };
    }

    private Converter<UUID, Ingredient> uuidToIngredient() {
        return ctx -> {
            UUID id = ctx.getSource();
            if (id == null) return null;
            Ingredient i = new Ingredient();
            i.setId(id);
            return i;
        };
    }

    private Converter<UUID, Tag> uuidToTag() {
        return ctx -> {
            UUID id = ctx.getSource();
            if (id == null) return null;
            Tag t = new Tag();
            t.setId(id);
            return t;
        };
    }

    private void addConverters(ModelMapper mm) {
        mm.addConverter(uuidToUser());
        mm.addConverter(uuidToRecipe());
        mm.addConverter(uuidToIngredient());
        mm.addConverter(uuidToTag());
    }

    private void configureTypeMaps(ModelMapper mm) {
        configureEntityToDto(mm);
        configureDtoToEntity(mm);
    }

    private void configureEntityToDto(ModelMapper mm) {
        mm.typeMap(User.class, UserDTO.class);

        mm.typeMap(Recipe.class, RecipeDTO.class)
                .addMapping(src -> src.getAuthor().getId(), RecipeDTO::setAuthorId);

        Converter<List<RecipeTag>, List<TagDTO>> recipeTagToTagDtoList = ctx -> {
            List<RecipeTag> src = ctx.getSource();
            if (src == null) return null;
            return src.stream()
                    .map(rt -> mm.map(rt.getTag(), TagDTO.class))
                    .collect(Collectors.toList());
        };

        mm.typeMap(Recipe.class, RecipeDTO.class)
                .addMappings(mapper ->
                        mapper.using(recipeTagToTagDtoList)
                                .map(Recipe::getRecipeTags, RecipeDTO::setTags)
                );

        mm.typeMap(RecipeIngredient.class, RecipeIngredientDTO.class)
                .addMapping(src -> src.getIngredient().getId(), RecipeIngredientDTO::setIngredientId)
                .addMapping(src -> src.getRecipe().getId(), RecipeIngredientDTO::setRecipeId);

        mm.typeMap(PantryItem.class, PantryItemDTO.class)
                .addMapping(src -> src.getIngredient().getId(), PantryItemDTO::setIngredientId)
                .addMapping(src -> src.getUser().getId(), PantryItemDTO::setUserId);

        mm.typeMap(Comment.class, CommentDTO.class)
                .addMapping(src -> src.getUser().getId(), CommentDTO::setUserId)
                .addMapping(src -> src.getRecipe().getId(), CommentDTO::setRecipeId);

        mm.typeMap(Rating.class, RatingDTO.class)
                .addMapping(src -> src.getUser().getId(), RatingDTO::setUserId)
                .addMapping(src -> src.getRecipe().getId(), RatingDTO::setRecipeId);

        mm.typeMap(Image.class, ImageDTO.class)
                .addMapping(src -> src.getRecipe().getId(), ImageDTO::setRecipeId);

        mm.typeMap(Tag.class, TagDTO.class);
        mm.typeMap(Ingredient.class, IngredientDTO.class);
    }

    private void configureDtoToEntity(ModelMapper mm) {
        mm.typeMap(CreateRecipeDTO.class, Recipe.class)
                .addMapping(CreateRecipeDTO::getAuthorId, Recipe::setAuthor);

        mm.typeMap(CreateCommentDTO.class, Comment.class)
                .addMapping(CreateCommentDTO::getUserId, Comment::setUser)
                .addMapping(CreateCommentDTO::getRecipeId, Comment::setRecipe);

        mm.typeMap(CreateRatingDTO.class, Rating.class)
                .addMapping(CreateRatingDTO::getUserId, Rating::setUser)
                .addMapping(CreateRatingDTO::getRecipeId, Rating::setRecipe);

        mm.typeMap(CreatePantryItemDTO.class, PantryItem.class)
                .addMapping(CreatePantryItemDTO::getUserId, PantryItem::setUser)
                .addMapping(CreatePantryItemDTO::getIngredientId, PantryItem::setIngredient);

        mm.typeMap(RecipeIngredientCreateDTO.class, RecipeIngredient.class)
                .addMapping(RecipeIngredientCreateDTO::getIngredientId, RecipeIngredient::setIngredient);

        mm.typeMap(CreateImageDTO.class, Image.class)
                .addMapping(CreateImageDTO::getRecipeId, Image::setRecipe);

        mm.typeMap(CreateTagDTO.class, Tag.class);
    }
}