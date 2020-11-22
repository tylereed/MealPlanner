package reed.tyler.mealplanner.ingredients;

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
class IngredientControllerTest {

	@Autowired
	private IngredientRepository repository;

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	void setup() {
		Ingredient ingredient1 = new Ingredient(1, "test name1", false);
		Ingredient ingredient2 = new Ingredient(2, "test name2", true);
		ingredient1 = repository.save(ingredient1);
		ingredient2 = repository.save(ingredient2);
		repository.flush();
	}

	private ResultMatcher matchIngredientArray(int index, long id, String name, boolean exotic) {
		return matchAll(jsonPath("$[%d].id", index).value(id),
				jsonPath("$[%d].name", index).value(name),
				jsonPath("$[%d].exotic", index).value(exotic),
				jsonPath("$[%d].length())", index).value(3));
	}

	private ResultMatcher matchIngredient(long id, String name, boolean exotic) {
		return matchAll(jsonPath("$.id").value(id),
				jsonPath("$.name").value(name),
				jsonPath("$.exotic").value(exotic),
				jsonPath("length($)").value(3));
	}

	private ResultMatcher matchIngredient(Ingredient expected) {
		return matchIngredient(expected.getEntityId(), expected.getName(), expected.isExotic());
	}

	@AfterEach
	void tearDown(@Autowired JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute("TRUNCATE TABLE ingredient RESTART IDENTITY");
	}

	@Test
	void testRead() throws Exception {
		mvc.perform(get("/api/ingredients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("length($)").value(2))
				.andExpect(matchIngredientArray(0, 1, "test name1", false))
				.andExpect(matchIngredientArray(1, 2, "test name2", true));
	}

	@Test
	void testRead_200() throws Exception {
		mvc.perform(get("/api/ingredients/1"))
		.andExpect(status().isOk())
		.andExpect(matchIngredient(1, "test name1", false));
	}

	@Test
	void testReadInt_404() throws Exception {
		mvc.perform(get("/api/ingredients/100"))
			.andExpect(status().isNotFound());
	}

	@Test
	void testCreate() throws Exception {
		Ingredient ingredient = new Ingredient(0, "new ingredient", false);

		ObjectMapper objectMapper = new ObjectMapper();
		String ingredientString = objectMapper.writeValueAsString(ingredient);

		mvc.perform(post("/api/ingredients").contentType("application/json").content(ingredientString))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", endsWith("/api/ingredients/3")));

		ingredient.setId(3L);
		mvc.perform(get("/api/ingredients/3"))
			.andExpect(status().isOk())
			.andExpect(matchIngredient(ingredient));
	}

	@Test
	void testUpdate() throws Exception {
		Ingredient ingredient = new Ingredient(0, "new ingredient", false);

		ObjectMapper mapper = new ObjectMapper();
		String ingredientString = mapper.writeValueAsString(ingredient);

		mvc.perform(put("/api/ingredients/1").contentType("application/json").content(ingredientString))
				.andExpect(status().isNoContent());

		ingredient.setId(1L);
		mvc.perform(get("/api/ingredients/1"))
			.andExpect(status().isOk())
			.andExpect(matchIngredient(ingredient));
	}

}
