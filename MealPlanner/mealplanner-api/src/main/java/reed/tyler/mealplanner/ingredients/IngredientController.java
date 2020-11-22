package reed.tyler.mealplanner.ingredients;

import java.net.URI;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

	private final IngredientRepository repository;

	@Autowired
	public IngredientController(IngredientRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Ingredient> read() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<Ingredient> read(@PathVariable long id) {
		var ingredient = repository.findById(id);
		return ResponseEntity.of(ingredient);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated Ingredient ingredient) {
		ingredient = repository.save(ingredient);

		URI location = MvcUriComponentsBuilder
				.fromMethodName(IngredientController.class, "read", ingredient.getId())
				.build(ingredient.getId());

		return ResponseEntity.created(location).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody Ingredient updatedIngredient) {
		var ingredient = repository.findById(id)
				.map(dbIngredient -> {
					BeanUtils.copyProperties(updatedIngredient, dbIngredient, "id");
					return dbIngredient;
				});
		
		ingredient.ifPresent(repository::save);
		
		return ingredient.map(x -> ResponseEntity.noContent().build())
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
