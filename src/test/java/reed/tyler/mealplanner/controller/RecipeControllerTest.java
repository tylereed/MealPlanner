package reed.tyler.mealplanner.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.ResultMatcher.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import reed.tyler.mealplanner.db.Recipe;
import reed.tyler.mealplanner.repository.RecipeRepository;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

	@Autowired
	private RecipeRepository repository;

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	void setUp() throws Exception {
		Recipe recipe1 = new Recipe(1, "test name1", "test directions1", "test location1", 1, 2, 3);
		Recipe recipe2 = new Recipe(2, "test name2", "test directions2", "test location2", 2, 3, 1);
		repository.save(recipe1);
		repository.save(recipe2);
		repository.flush();
	}

	@AfterEach
	void tearDown() throws Exception {
		repository.deleteAll();
		repository.flush();
	}

	private ResultMatcher matchRecipe(int index, int id, String name, String directions, String location, int price,
			int speed, int difficulty) {

		return matchAll(jsonPath("$[%d].id", index).value(id), jsonPath("$[%d].name", index).value(name),
				jsonPath("$[%d].directions", index).value(directions),
				jsonPath("$[%d].location", index).value(location), jsonPath("$[%d].price", index).value(price),
				jsonPath("$[%d].speed", index).value(speed), jsonPath("$[%d].difficulty", index).value(difficulty));
	}

	private ResultMatcher matchRecipe(int id, String name, String directions, String location, int price, int speed,
			int difficulty) {

		return matchAll(jsonPath("$.id").value(id), jsonPath("$.name").value(name),
				jsonPath("$.directions").value(directions), jsonPath("$.location").value(location),
				jsonPath("$.price").value(price), jsonPath("$.speed").value(speed),
				jsonPath("$.difficulty").value(difficulty));
	}

	@Test
	void testRead() throws Exception {
		mvc.perform(get("/api/recipes")).andExpect(status().isOk())
				.andExpect(matchRecipe(1, 1, "test name1", "test directions1", "test location1", 1, 2, 3))
				.andExpect(matchRecipe(2, 2, "test name2", "test directions2", "test location2", 2, 3, 1));
	}

	@Test
	void testReadInt() throws Exception {
		mvc.perform(get("/api/recipes/1"))
			.andExpect(status().isOk())
			.andExpect(matchRecipe(1, 1, "test name1", "test directions1", "test location1", 1, 2, 3));
	}

	@Test
	void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

}
