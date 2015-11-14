package reed.tyler.dish.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import reed.tyler.dish.SetDish;
import reed.tyler.dish.Type;
import reed.tyler.dish.db.Dish;

public class RollWeekActionListener implements ActionListener {

	private static String CONNECTION_STRING = "jdbc:hsqldb:file:db/mealplanner;ifexists=true";

	private Iterable<SetDish> setters;

	private Random random;

	public RollWeekActionListener(Iterable<SetDish> setters) {
		this.setters = setters;
		this.random = new Random();
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		try (Connection connection = DriverManager.getConnection(CONNECTION_STRING, "SA", "");
				Dish dish = new Dish(connection);) {

			for (SetDish setter : setters) {
				setter.setMain(getRandom(dish, Type.Main));
				setter.setVegetable(getRandom(dish, Type.Vegetable));
				setter.setSide(getRandom(dish, Type.Side));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private reed.tyler.dish.Dish getRandom(Dish selector, Type type) throws SQLException {
		List<Integer> dishIds = selector.getIds(type);
		int index = random.nextInt(dishIds.size());
		int selectedId = dishIds.get(index);
		return selector.select(selectedId);
	}

}
