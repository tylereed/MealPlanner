package reed.tyler.mealplanner.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import reed.tyler.mealplanner.db.Recipe;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RecipeControllerTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	void setUp() throws Exception {
		Recipe recipe = new Recipe(1, "test name", "test directions", "test location", 1, 2, 3);
		entityManager.persistAndFlush(recipe);
	}

	@AfterEach
	void tearDown() throws Exception {
		entityManager.clear();
		entityManager.flush();
	}

	@Test
	void testRead() {
		mvc.perform(RequestBuilder.get("/api/recipes"));
	}

	@Test
	void testReadInt() {
		fail("Not yet implemented");
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
