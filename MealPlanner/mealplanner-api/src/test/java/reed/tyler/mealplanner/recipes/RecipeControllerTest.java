package reed.tyler.mealplanner.recipes;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	void tearDown(@Autowired JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.execute("TRUNCATE TABLE recipe RESTART IDENTITY");
	}

	private ResultMatcher matchRecipeArray(int index, long id, String name, String directions, String location, int price,
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

	private ResultMatcher matchRecipe(long id, String name, String directions, String location, int price, int speed,
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
	
	private ResultMatcher matchRecipe(Recipe expected) {
		return matchRecipe(expected.getEntityId(), expected.getName(), expected.getDirections(), expected.getLocation(),
				expected.getPrice(), expected.getSpeed(), expected.getDifficulty());
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
		Recipe recipe = new Recipe(0, "unit test name", "unit test directions", "unit test location", 3, 2, 1);

		ObjectMapper objectMapper = new ObjectMapper();
		String recipeString = objectMapper.writeValueAsString(recipe);

		mvc.perform(post("/api/recipes").contentType("application/json").content(recipeString))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", endsWith("/api/recipes/3")));

		recipe.setId(3);
		mvc.perform(get("/api/recipes/3"))
			.andExpect(status().isOk())
			.andExpect(matchRecipe(recipe));
	}

	@Test
	void testUpdate() throws Exception {
		Recipe recipe = new Recipe(0, "updated name", "updated directions", "updated location", 3, 1, 2);

		ObjectMapper mapper = new ObjectMapper();
		String recipeString = mapper.writeValueAsString(recipe);

		mvc.perform(put("/api/recipes/1").contentType("application/json").content(recipeString))
				.andExpect(status().isNoContent());

		recipe.setId(1);
		mvc.perform(get("/api/recipes/1"))
			.andExpect(status().isOk())
			.andExpect(matchRecipe(recipe));
	}

}