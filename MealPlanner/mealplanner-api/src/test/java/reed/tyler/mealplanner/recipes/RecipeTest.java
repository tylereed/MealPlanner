package reed.tyler.mealplanner.recipes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RecipeTest {

	@Test
	void testConstructor() {
		assertDoesNotThrow(() -> new Recipe(0, "test name", "test directions", "test location", 1, 2, 3));
	}

	@Test
	void testConstructor_NullName() {
		assertThrows(NullPointerException.class,
				() -> new Recipe(0, null, "test directions", "test location", 1, 2, 3));
	}

	@Test
	void testConstructor_NullDirections() {
		assertThrows(NullPointerException.class, () -> new Recipe(0, "test name", null, "test location", 1, 2, 3));
	}

	@Test
	void testConstructor_NullLocation() {
		assertThrows(NullPointerException.class, () -> new Recipe(0, "test name", "test directions", null, 1, 2, 3));
	}

}
