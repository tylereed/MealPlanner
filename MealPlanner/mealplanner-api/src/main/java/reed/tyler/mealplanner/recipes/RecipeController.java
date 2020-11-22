package reed.tyler.mealplanner.recipes;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reed.tyler.mealplanner.CrudController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController extends CrudController<Recipe, RecipeRepository, Long> {

	@Autowired
	public RecipeController(RecipeRepository repository) {
		super(repository);
	}

	@GetMapping("/random/{count}")
	public List<Recipe> generate(@PathVariable long count) {
		ThreadLocalRandom random = ThreadLocalRandom.current();

		long max = repository.count();

		HashSet<Long> uniqueIds = new HashSet<>();
		while (uniqueIds.size() < count) {
			long nextId = random.nextLong(1, max + 1);
			if (repository.existsById(nextId)) {
				uniqueIds.add(nextId);
			}
		}

		return repository.findAllById(uniqueIds);
	}

}
