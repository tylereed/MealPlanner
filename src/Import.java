
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Strings;
import com.opencsv.CSVReader;

import reed.tyler.dish.Type;
import reed.tyler.dish.db.Description;
import reed.tyler.dish.db.Dish;
import reed.tyler.dish.db.DishToMethod;
import reed.tyler.dish.db.DishToRecipe;
import reed.tyler.dish.db.Ingredient;
import reed.tyler.dish.db.Method;
import reed.tyler.dish.db.Recipe;

/**
 * Imports data from a csv file into the Meal Planner database
 * 
 * @author Tyler
 */
public class Import {

	public static void main(String[] args) {
		final int CSV_NAME = 0;
		final int CSV_PRICE = 1;
		final int CSV_SPEED = 2;
		final int CSV_DIFFICULTY = 3;
		final int CSV_DESCRIPTION = 4;
		final int CSV_INGREDIENT = 5;
		final int CSV_METHODS = 6;
		final int CSV_RECIPE = 7;

		try (CSVReader reader = new CSVReader(new FileReader(args[0]));
				Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:db/mealplanner;ifexists=true",
						"SA", "");
				Ingredient ingredient = new Ingredient(connection);
				Method method = new Method(connection);
				Recipe recipe = new Recipe(connection);
				Description description = new Description(connection);
				Dish dish = new Dish(connection);
				DishToMethod dishToMethod = new DishToMethod(connection);
				DishToRecipe dishToRecipe = new DishToRecipe(connection)) {

			Type type = Type.Main;

			for (String[] columns : reader) {
				for (int i = 0; i < columns.length; ++i) {
					columns[i] = columns[i].trim();
				}

				int ingredientId = ingredient.selectOrInsert(columns[CSV_INGREDIENT]);
				int descriptionId = description.selectOrInsert(columns[CSV_DESCRIPTION]);

				int dishId = dish.insert(type.getId(), ingredientId, descriptionId, columns[CSV_NAME],
						getPrice(columns[CSV_PRICE]), getSpeed(columns[CSV_SPEED]),
						getDifficulty(columns[CSV_DIFFICULTY]));

				String recipeString = columns[CSV_RECIPE];
				if (!Strings.isNullOrEmpty(recipeString)) {
					int recipeId = recipe.selectOrInsert(recipeString);

					dishToRecipe.insert(dishId, recipeId, "");
				}

				List<String> methods = Pattern.compile(",|/").splitAsStream(columns[CSV_METHODS]).map(String::trim)
						.map(WordUtils::capitalizeFully).collect(Collectors.toList());

				for (int i = 0; i < methods.size(); ++i) {
					int methodId = method.selectOrInsert(methods.get(i));
					dishToMethod.insert(dishId, methodId, i);
				}
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts a text string of speed level into an integer
	 * 
	 * @param speed
	 *            The text string to convert
	 * @return An integer corresponding to the speed level
	 * @throws IllegalArgumentException
	 *             If the input string is not understood
	 */
	public static int getSpeed(String speed) {
		switch (speed) {
		case "Speed I":
			return 1;
		case "Speed II":
			return 2;
		case "Speed III":
			return 3;
		default:
			throw new IllegalArgumentException("Unknown string for speed: '" + speed + "'");
		}
	}

	/**
	 * Converts a text string of price level into an integer
	 * 
	 * @param price
	 *            The text string to convert
	 * @return An integer corresponding to the price level
	 * @throws IllegalArgumentException
	 *             If the input string is not understood
	 */
	public static int getPrice(String price) {
		switch (price) {
		case "Price I":
			return 1;
		case "Price II":
			return 2;
		case "Price III":
			return 3;
		default:
			throw new IllegalArgumentException("Unknown string for price: '" + price + "'");
		}
	}

	/**
	 * Converts a text string of difficulty level into an integer
	 * 
	 * @param difficulty
	 *            The text string to convert
	 * @return An integer corresponding to the difficulty level
	 * @throws IllegalArgumentException
	 *             If the input string is not understood
	 */
	public static int getDifficulty(String difficulty) {
		switch (difficulty) {
		case "Difficulty I":
			return 1;
		case "Difficulty II":
			return 2;
		case "Difficulty III":
			return 3;
		default:
			throw new IllegalArgumentException("Unknown string for difficulty: '" + difficulty + "'");
		}
	}

}
