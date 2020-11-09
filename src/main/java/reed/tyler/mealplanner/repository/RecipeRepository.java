package reed.tyler.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import reed.tyler.mealplanner.db.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

}
