package com.mycookbookapp.recipe_backend.controller;

import com.mycookbookapp.recipe_backend.model.Recipe;
import com.mycookbookapp.recipe_backend.repository.RecipeRepository;
import com.mycookbookapp.recipe_backend.ai.GeminiService; // Import GeminiService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Import MultipartFile
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired // Inject GeminiService
    private GeminiService geminiService;

    // --- GET All Recipes ---
    // Handles GET requests to /api/recipes
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll(); // Fetches all recipes from the database
    }

    // --- GET Recipe by ID ---
    // Handles GET requests to /api/recipes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id); // Tries to find a recipe by its ID
        if (recipe.isPresent()) {
            return ResponseEntity.ok(recipe.get()); // If found, return 200 OK with the recipe
        } else {
            return ResponseEntity.notFound().build(); // If not found, return 404 Not Found
        }
    }

    // --- CREATE New Recipe ---
    // Handles POST requests to /api/recipes
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        // Set creation and update timestamps before saving
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        Recipe savedRecipe = recipeRepository.save(recipe); // Saves the new recipe to the database
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe); // Return 201 Created with the saved recipe
    }

    // --- UPDATE Existing Recipe ---
    // Handles PUT requests to /api/recipes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipeDetails) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            // Get the existing recipe entity
            Recipe existingRecipe = recipe.get();

            // Update fields from the request body (recipeDetails)
            existingRecipe.setTitle(recipeDetails.getTitle());
            existingRecipe.setDescription(recipeDetails.getDescription());
            existingRecipe.setIngredients(recipeDetails.getIngredients());
            existingRecipe.setInstructions(recipeDetails.getInstructions());
            existingRecipe.setPrepTime(recipeDetails.getPrepTime());
            existingRecipe.setCookTime(recipeDetails.getCookTime());
            existingRecipe.setServings(recipeDetails.getServings());
            existingRecipe.setCategory(recipeDetails.getCategory());
            existingRecipe.setCuisine(recipeDetails.getCuisine());
            existingRecipe.setImageUrl(recipeDetails.getImageUrl());
            existingRecipe.setUpdatedAt(LocalDateTime.now()); // Update timestamp

            Recipe updatedRecipe = recipeRepository.save(existingRecipe); // Save the updated recipe
            return ResponseEntity.ok(updatedRecipe); // Return 200 OK with the updated recipe
        } else {
            return ResponseEntity.notFound().build(); // If recipe not found, return 404
        }
    }

    // --- DELETE Recipe ---
    // Handles DELETE requests to /api/recipes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.delete(recipe.get()); // Delete the recipe if found
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // If not found, return 404
        }
    }

    // NEW ENDPOINT: Parse recipe from image using Gemini
    @PostMapping("/parse-image") // e.g., POST to /api/recipes/parse-image
    public ResponseEntity<Recipe> parseRecipeFromImage(@RequestParam("image") MultipartFile[] imageFiles) {
        try {
            // Pass the array of files to GeminiService
            Recipe parsedRecipe = geminiService.parseRecipeFromImage(imageFiles);
            return ResponseEntity.ok(parsedRecipe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Error parsing image with Gemini: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}