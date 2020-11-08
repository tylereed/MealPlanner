package reed.tyler.mealplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reed.tyler.mealplanner.db.Recipe;
import reed.tyler.mealplanner.repository.RecipeRepository;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

	private final RecipeRepository repository;

	@Autowired
	public RecipeController(RecipeRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Recipe> read() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	public ResponseEntity<Recipe> read(@PathVariable int id) {
		var recipe = repository.findById(id);
		return ResponseEntity.of(recipe);
	}

	@PostMapping
	public ResponseEntity<Recipe> create(@RequestBody Recipe recipe) {
		return null;
	}

	@PutMapping("{id}")
	public ResponseEntity<Recipe> update(@PathVariable int id, @RequestBody Recipe recipe) {
		return null;
	}

}
