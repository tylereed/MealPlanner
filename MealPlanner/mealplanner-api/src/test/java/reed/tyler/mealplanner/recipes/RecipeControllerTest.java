package reed.tyler.mealplanner.recipes;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reed.tyler.mealplanner.ResultMatcherExtension.matchBadRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reed.tyler.mealplanner.JdbcUtils;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

	@Autowired
	private RecipeRepository repository;

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	void setUp() {
		Recipe recipe1 = new Recipe(1, "test name1", "test directions1", "test location1", 1, 2, 3);
		Recipe recipe2 = new Recipe(2, "test name2", "test directions2", "test location2", 2, 3, 1);
		recipe1 = repository.save(recipe1);
		recipe2 = repository.save(recipe2);
		repository.flush();
	}

	@AfterEach
	void tearDown(@Autowired JdbcUtils jdbc) throws Exception {
		jdbc.truncateTables("recipe");
	}

	private static ResultMatcher matchRecipeArray(int index, long id, String name, String directions, String location, int price,
			int speed, int difficulty) {

		return matchAll(jsonPath("$[%d].id", index).value(id),
				jsonPath("$[%d].name", index).value(name),
				jsonPath("$[%d].directions", index).value(directions),
				jsonPath("$[%d].location", index).value(location),
				jsonPath("$[%d].price", index).value(price),
				jsonPath("$[%d].speed", index).value(speed),
				jsonPath("$[%d].difficulty", index).value(difficulty),
				jsonPath("$[%d].length())", index).value(7));
	}

	private static ResultMatcher matchRecipe(long id, String name, String directions, String location, int price, int speed,
			int difficulty) {

		return matchAll(jsonPath("$.id").value(id),
				jsonPath("$.name").value(name),
				jsonPath("$.directions").value(directions),
				jsonPath("$.location").value(location),
				jsonPath("$.price").value(price),
				jsonPath("$.speed").value(speed),
				jsonPath("$.difficulty").value(difficulty),
				jsonPath("length($)").value(7));
	}

	private static String createRecipeJson(long id, String name, String directions, String location, int price,
			int speed, int difficulty) throws JsonProcessingException {
		Recipe recipe = new Recipe(id, name, directions, location, price, speed, difficulty);

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(recipe);
	}

	@Test
	void testRead() throws Exception {
		mvc.perform(get("/api/recipes")).andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchRecipeArray(0, 1, "test name1", "test directions1", "test location1", 1, 2, 3))
				.andExpect(matchRecipeArray(1, 2, "test name2", "test directions2", "test location2", 2, 3, 1));
	}

	@Test
	void testReadInt_200() throws Exception {
		mvc.perform(get("/api/recipes/1"))
			.andExpect(status().isOk())
			.andExpect(matchRecipe(1, "test name1", "test directions1", "test location1", 1, 2, 3));
	}

	@Test
	void testReadInt_404() throws Exception {
		mvc.perform(get("/api/recipes/100"))
			.andExpect(status().isNotFound());
	}

	@Test
	void testCreate() throws Exception {
		String recipeString = createRecipeJson(0, "unit test name", "unit test directions", "unit test location", 3, 2,
				1);

		mvc.perform(post("/api/recipes").contentType("application/json").content(recipeString))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", endsWith("/api/recipes/3")));

		mvc.perform(get("/api/recipes/3"))
				.andExpect(status().isOk())
				.andExpect(matchRecipe(3, "unit test name", "unit test directions", "unit test location", 3, 2, 1));
	}

	@Test
	void testCreate_BadRequest_DuplicateName() throws Exception {
		String recipeString = createRecipeJson(0, "test name1", "unit test directions", "unit test location", 1, 2, 3);

		mvc.perform(post("/api/recipes").contentType("application/json").content(recipeString))
				.andExpect(matchBadRequest("A Recipe with the name \"test name1\" already exists"));
	}

	@Test
	void testUpdate() throws Exception {
		String recipeString = createRecipeJson(0, "updated name", "updated directions", "updated location", 3, 1, 2);

		mvc.perform(put("/api/recipes/1").contentType("application/json").content(recipeString))
				.andExpect(status().isNoContent());

		mvc.perform(get("/api/recipes/1"))
			.andExpect(status().isOk())
			.andExpect(matchRecipe(1, "updated name", "updated directions", "updated location", 3, 1, 2));
	}

}
