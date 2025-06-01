package com.mycookbookapp.recipe_backend.controller; // Ensure this package matches your structure

import com.mycookbookapp.recipe_backend.model.Recipe; // Import your Recipe entity
import com.mycookbookapp.recipe_backend.repository.RecipeRepository; // Import your RecipeRepository
import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
import org.springframework.http.HttpStatus; // For HTTP status codes
import org.springframework.http.ResponseEntity; // For building HTTP responses
import org.springframework.web.bind.annotation.*; // Import all common REST annotations

import java.time.LocalDateTime; // For setting timestamps
import java.util.List;
import java.util.Optional; // For handling optional results from findById

@RestController // Marks this class as a REST Controller, handling web requests
@RequestMapping("/api/recipes") // Base path for all endpoints in this controller
public class RecipeController {

    @Autowired // Spring will automatically inject an instance of RecipeRepository
    private RecipeRepository recipeRepository;

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
}