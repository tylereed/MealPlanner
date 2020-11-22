package reed.tyler.mealplanner.ingredients;

import java.util.List;

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
		return List.of();// repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<Ingredient> read(@PathVariable long id) {
		return ResponseEntity.status(500).build();
//		var ingredient = repository.findById(id);
//		return ResponseEntity.of(ingredient);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated Ingredient ingredient) {
		return ResponseEntity.status(500).build();
	}

	@PutMapping
	public ResponseEntity<?> update(@PathVariable long id, @RequestBody Ingredient updatedIngredient) {
		return ResponseEntity.status(500).build();
	}

}
