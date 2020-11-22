package reed.tyler.mealplanner.ingredients;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class IngredientTest {

	@Test
	void testConstructor() {
		assertDoesNotThrow(() -> new Ingredient(0, "test name", true));
	}

	@Test
	void testConstructor_NullName() {
		assertThrows(NullPointerException.class, () -> new Ingredient(0, null, true));
	}

}
