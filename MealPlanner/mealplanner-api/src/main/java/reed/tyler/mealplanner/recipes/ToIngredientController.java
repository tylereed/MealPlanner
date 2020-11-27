package reed.tyler.mealplanner.recipes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reed.tyler.mealplanner.ingredients.Ingredient;
import reed.tyler.mealplanner.ingredients.IngredientRepository;

@RestController
@RequestMapping("/api/recipes/{recipeId}/ingredients")
public class ToIngredientController {

	private RecipeRepository recipes;
	private IngredientRepository ingredients;

	@Autowired
	public ToIngredientController(RecipeRepository recipes, IngredientRepository ingredients) {
		this.recipes = recipes;
		this.ingredients = ingredients;
	}

	@PostMapping
	public ResponseEntity<?> set(List<String> ingredientNames) {
		return ResponseEntity.status(500).build();
	}

	@GetMapping
	public List<Ingredient> read(@PathVariable long recipeId) {
		return List.of();
	}

}
