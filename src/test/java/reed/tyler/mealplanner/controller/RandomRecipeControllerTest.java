package reed.tyler.mealplanner.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import reed.tyler.mealplanner.db.Recipe;
import reed.tyler.mealplanner.repository.RecipeRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class RandomRecipeControllerTest {

	@Autowired
	private RecipeRepository repository;

	@Autowired
	private MockMvc mvc;

	private static Recipe generateRecipe(int id) {
		return new Recipe(id, "test name" + id, "test directions" + id, "test location" + id, 1, 1, 1);
	}

	@BeforeEach
	void setUp() throws Exception {
		for (int i = 1; i <= 25; i++) {
			repository.save(generateRecipe(i));
		}
		repository.flush();
	}

	@AfterEach
	void tearDown(@Autowired JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.execute("TRUNCATE TABLE recipe RESTART IDENTITY");
	}

	@Test
	void testRead() throws Exception {
		MvcResult result = mvc.perform(get("/api/recipes/random/7"))
			.andReturn();
		
		String resultString = result.getResponse().getContentAsString();
		List<Integer> results = JsonPath.parse(resultString).read("$.[*].id");
		HashSet<Integer> uniqueIds = new HashSet<Integer>(results);
		assertEquals(7, uniqueIds.size());
	}

}
