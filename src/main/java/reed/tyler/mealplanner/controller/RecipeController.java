package reed.tyler.mealplanner.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

	@GetMapping("/random/{count}")
	public List<Recipe> generate(@PathVariable int count) {
		return List.of();
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Validated Recipe recipe) {
		recipe = repository.save(recipe);

		URI location = MvcUriComponentsBuilder
				.fromMethodName(RecipeController.class, "read", recipe.getId())
				.build(recipe.getId());

		return ResponseEntity.created(location).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable int id, @RequestBody Recipe updatedRecipe) {
		var recipe = repository.findById(id)
			.map(dbRecipe -> {
				BeanUtils.copyProperties(updatedRecipe, dbRecipe, "id");
				return dbRecipe;
			});
		
		recipe.ifPresent(repository::save);
		
		return ofNoContent(recipe);
	}

	private static ResponseEntity<?> ofNoContent(Optional<?> body) {
		return body.map(x -> ResponseEntity.noContent().build())
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
