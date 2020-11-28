package reed.tyler.mealplanner.recipes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import reed.tyler.mealplanner.ingredients.Ingredient;
import reed.tyler.mealplanner.ingredients.IngredientRepository;
import reed.tyler.mealplanner.utils.ErrorMessages;

@RestController
@RequestMapping("/api/recipes/{recipeId}/ingredients")
public class ToIngredientController {

	private RecipeRepository recipesRepo;
	private IngredientRepository ingredientsRepo;

	@Autowired
	public ToIngredientController(RecipeRepository recipes, IngredientRepository ingredients) {
		this.recipesRepo = recipes;
		this.ingredientsRepo = ingredients;
	}

	@PostMapping
	public ResponseEntity<?> set(@PathVariable long recipeId, @RequestBody Set<Long> ingredientIds) {
		var recipe = recipesRepo.findById(recipeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		var ingredients = ingredientsRepo.findAllById(ingredientIds).stream().collect(Collectors.toSet());

		if (ingredientIds.size() > ingredients.size()) {
			var dbIngredients = ingredients.stream().map(i -> i.getId()).collect(Collectors.toSet());
			ingredientIds.removeAll(dbIngredients);

			boolean plural = ingredientIds.size() > 1;
			String missingIds = ingredientIds.stream().map(String::valueOf).collect(Collectors.joining("', '"));
			
			String message;
			if (plural) {
				message = String.format("Ingredients with ids '%s' do not exist", missingIds);
			} else {
				message = String.format("An Ingredient with id '%s' does not exist", missingIds);
			}
			
			var errors = new ErrorMessages(message);
			return ResponseEntity.badRequest().body(errors);
		}

		recipe.setIngredients(ingredients);
		recipe = recipesRepo.saveAndFlush(recipe);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<Ingredient>> read(@PathVariable long recipeId) {
		var recipe = recipesRepo.findById(recipeId);
		var ingredients = recipe.map(r -> List.copyOf(r.getIngredients()));
		return ResponseEntity.of(ingredients);
	}

}
