package reed.tyler.mealplanner.ingredients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reed.tyler.mealplanner.CrudController;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController extends CrudController<Ingredient, IngredientRepository, Long> {

	@Autowired
	public IngredientController(IngredientRepository repository) {
		super(repository);
	}

}
