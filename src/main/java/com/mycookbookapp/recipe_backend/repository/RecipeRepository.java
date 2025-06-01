package com.mycookbookapp.recipe_backend.repository; // Ensure this package matches your structure

import com.mycookbookapp.recipe_backend.model.Recipe; // Import your Recipe entity
import org.springframework.data.jpa.repository.JpaRepository; // Import JpaRepository
import org.springframework.stereotype.Repository; // Optional, but good practice for clarity

@Repository // Marks this interface as a Spring repository component
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Spring Data JPA automatically provides CRUD methods (save, findById, findAll, delete, etc.)
    // You can add custom query methods here if needed, e.g.:
    // List<Recipe> findByTitleContainingIgnoreCase(String title);
}