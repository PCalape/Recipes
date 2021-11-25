package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.RecipeEntity;
import recipes.model.RecipeResponse;
import recipes.model.UserLoginModel;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RecipesController {

    @Autowired
    PasswordEncoder passwordEncoder;

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;

    @Autowired
    public RecipesController(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public void register(@Valid @RequestBody UserLoginModel user) {
        if (userRepository.existsById(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @PostMapping("/recipe/new")
    public Map<String, Long> postRecipe(@Valid @RequestBody RecipeEntity recipes, Authentication auth) {
        recipes.setEmail(auth.getName());
        Long id = recipeRepository.save(recipes).getId();
        return Collections.singletonMap("id",id);
    }

    @GetMapping("/recipe/{id}")
    public RecipeResponse getOneRecipe(@PathVariable Long id) {
        if (recipeRepository.existsById(id) == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RecipeEntity oneRecipe = recipeRepository.findById(id).get();
        return new RecipeResponse(
                oneRecipe.getName(),
                oneRecipe.getCategory(),
                oneRecipe.getDate(),
                oneRecipe.getDescription(),
                oneRecipe.getIngredients(),
                oneRecipe.getDirections());
    }

    @GetMapping("/recipe/search")
    public List<RecipeResponse> getRecipesWithParams(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {

        List<RecipeEntity> recipesByParam;
        if (category != null) {
            recipesByParam = recipeRepository.findByCategoryIgnoreCase(category);
        }
        else if (name != null) {
            recipesByParam = recipeRepository.findByNameContainingIgnoreCase(name);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        recipesByParam.forEach(a -> {
            recipeResponseList.add(new RecipeResponse(
                    a.getName(),
                    a.getCategory(),
                    a.getDate(),
                    a.getDescription(),
                    a.getIngredients(),
                    a.getDirections()
            ));
        });
        recipeResponseList.sort(Comparator.comparing(RecipeResponse::getDate).reversed());
        return recipeResponseList;
    }

    @PutMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOneRecipe(@PathVariable Long id, @Valid @RequestBody RecipeEntity recipes, Authentication auth) {
        if (recipeRepository.existsById(id) == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!auth.getName().equals(recipeRepository.findById(id).get().getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        RecipeEntity forUpdateRecipe = recipeRepository.findById(id).get();
        forUpdateRecipe.setName(recipes.getName());
        forUpdateRecipe.setCategory(recipes.getCategory());
        forUpdateRecipe.setDate(LocalDateTime.now());
        forUpdateRecipe.setDescription(recipes.getDescription());
        forUpdateRecipe.setIngredients(recipes.getIngredients());
        forUpdateRecipe.setDirections(recipes.getDirections());
        recipeRepository.save(forUpdateRecipe);
    }

    @DeleteMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneRecipe(@PathVariable Long id, Authentication auth) {
        if (recipeRepository.existsById(id) == false) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!auth.getName().equals(recipeRepository.findById(id).get().getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        recipeRepository.deleteById(id);
    }

}
