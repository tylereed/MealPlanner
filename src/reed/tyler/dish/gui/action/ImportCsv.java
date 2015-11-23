package reed.tyler.dish.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.Strings;

import reed.tyler.dish.Type;
import reed.tyler.dish.db.Description;
import reed.tyler.dish.db.Dish;
import reed.tyler.dish.db.DishToMethod;
import reed.tyler.dish.db.DishToRecipe;
import reed.tyler.dish.db.Ingredient;
import reed.tyler.dish.db.Method;
import reed.tyler.dish.db.Recipe;

public class ImportCsv implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();

		int selection = chooser.showOpenDialog(null);

		if (selection == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			importFile(file);
		}
	}

	public void importFile(File file) {

		try (CSVParser parser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.EXCEL.withHeader());
				Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:db/mealplanner;ifexists=true",
						"SA", "");
				Ingredient ingredient = new Ingredient(connection);
				Method method = new Method(connection);
				Recipe recipe = new Recipe(connection);
				Description description = new Description(connection);
				Dish dish = new Dish(connection);
				DishToMethod dishToMethod = new DishToMethod(connection);
				DishToRecipe dishToRecipe = new DishToRecipe(connection)) {

			for (CSVRecord record : parser) {

				Type type = Type.valueOf(record.get("Type"));

				int ingredientId = ingredient.selectOrInsert(record.get("Main Ingredient"));
				int descriptionId = description.selectOrInsert(record.get("Notes"));

				int dishId = dish.insert(type.getId(), ingredientId, descriptionId, record.get("Name"),
						getPrice(record.get("Price")), getSpeed(record.get("Speed")),
						getDifficulty(record.get("Difficulty")));

				String recipeString = record.get("Recipe");
				if (!Strings.isNullOrEmpty(recipeString)) {
					int recipeId = recipe.selectOrInsert(recipeString);

					dishToRecipe.insert(dishId, recipeId, "");
				}

				List<String> methods = Pattern.compile(",|/").splitAsStream(record.get("Cooking Method"))
						.map(String::trim).map(WordUtils::capitalizeFully).collect(Collectors.toList());

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
		case "I":
			return 1;
		case "Speed II":
		case "II":
			return 2;
		case "Speed III":
		case "III":
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
		case "I":
			return 1;
		case "Price II":
		case "II":
			return 2;
		case "Price III":
		case "III":
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
		case "I":
			return 1;
		case "Difficulty II":
		case "II":
			return 2;
		case "Difficulty III":
		case "III":
			return 3;
		default:
			throw new IllegalArgumentException("Unknown string for difficulty: '" + difficulty + "'");
		}
	}

}
