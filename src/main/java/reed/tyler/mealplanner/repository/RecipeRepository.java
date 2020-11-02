package reed.tyler.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reed.tyler.mealplanner.db.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

}
