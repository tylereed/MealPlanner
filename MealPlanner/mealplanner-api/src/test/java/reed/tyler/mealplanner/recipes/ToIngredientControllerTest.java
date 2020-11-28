package reed.tyler.mealplanner.recipes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reed.tyler.mealplanner.ResultMatcherExtension.matchBadRequest;
import static reed.tyler.mealplanner.ingredients.IngredientControllerTest.matchIngredientArray;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reed.tyler.mealplanner.JdbcUtils;
import reed.tyler.mealplanner.ingredients.Ingredient;
import reed.tyler.mealplanner.ingredients.IngredientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ToIngredientControllerTest {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private MockMvc mvc;

	private static String buildArrayJson(int... ids) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(ids);
	}

	@BeforeEach
	void setUp() {
		Ingredient ingredient1 = new Ingredient(0, "ingredient1", false);
		Ingredient ingredient2 = new Ingredient(0, "ingredient2", false);
		Ingredient ingredient3 = new Ingredient(0, "ingredient3", false);
		ingredient1 = ingredientRepository.save(ingredient1);
		ingredient2 = ingredientRepository.save(ingredient2);
		ingredient3 = ingredientRepository.save(ingredient3);

		Recipe recipe1 = new Recipe(1, "test name1", "test directions1", "test location1", 1, 2, 3,
				Set.of(ingredient1, ingredient3));
		Recipe recipe2 = new Recipe(2, "test name2", "test directions2", "test location2", 2, 3, 1,
				Set.of(ingredient2, ingredient3));
		recipe1 = recipeRepository.save(recipe1);
		recipe2 = recipeRepository.save(recipe2);

		recipeRepository.flush();
	}

	@AfterEach
	void tearDown(@Autowired JdbcUtils jdbc) throws Exception {
		jdbc.truncateTables("recipe_ingredients", "ingredient", "recipe");
	}

	@Test
	void testRead_Recipe1() throws Exception {
		mvc.perform(get("/api/recipes/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchIngredientArray(0, 1, "ingredient1", false))
				.andExpect(matchIngredientArray(1, 3, "ingredient3", false));
	}

	@Test
	void testRead_Recipe2() throws Exception {
		mvc.perform(get("/api/recipes/2/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchIngredientArray(0, 2, "ingredient2", false))
				.andExpect(matchIngredientArray(1, 3, "ingredient3", false));
	}

	@Test
	void testRead_404() throws Exception {
		mvc.perform(get("/api/recipes/3/ingredients"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testSet_Overwrite() throws Exception {
		String idArray = buildArrayJson(2);

		mvc.perform(post("/api/recipes/1/ingredients").contentType("application/json").content(idArray))
				.andExpect(status().isNoContent());

		mvc.perform(get("/api/recipes/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(1))
				.andExpect(matchIngredientArray(0, 2, "ingredient2", false));
	}

	@Test
	void testSet_Keep() throws Exception {
		String idArray = buildArrayJson(1, 3);

		mvc.perform(post("/api/recipes/2/ingredients").contentType("application/json").content(idArray))
				.andExpect(status().isNoContent());

		mvc.perform(get("/api/recipes/2/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchIngredientArray(0, 1, "ingredient1", false))
				.andExpect(matchIngredientArray(1, 3, "ingredient3", false));
	}

	@Test
	void testSet_404() throws Exception {
		String idArray = buildArrayJson(1, 3);

		mvc.perform(post("/api/recipes/3/ingredients").contentType("application/json").content(idArray))
				.andExpect(status().isNotFound());
	}

	@Test
	void testSet_400_BadId() throws Exception {
		String idArray = buildArrayJson(1, 4);

		mvc.perform(post("/api/recipes/1/ingredients").contentType("application/json").content(idArray))
				.andExpect(matchBadRequest("An Ingredient with id '4' does not exist"));

		mvc.perform(get("/api/recipes/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchIngredientArray(0, 1, "ingredient1", false))
				.andExpect(matchIngredientArray(1, 3, "ingredient3", false));
	}

}
