package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterators;

import reed.tyler.dish.DishInfo;
import reed.tyler.dish.Type;
import reed.tyler.dish.db.iterators.ResultSetIterator;
import reed.tyler.dish.db.iterators.ResultSetIterator.Function;

public class Dish implements AutoCloseable {

	private PreparedStatement insert;

	private PreparedStatement getIds;

	private PreparedStatement select;

	public Dish(Connection connection) throws SQLException {
		this.insert = connection.prepareStatement("INSERT INTO Dish "
				+ "(TypeId, IngredientId, DescriptionId, Name, Price, Speed, Difficulty) " + "VALUES (?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		this.getIds = connection.prepareStatement("SELECT Id FROM Dish WHERE TypeId = ?");

		this.select = connection
				.prepareStatement("SELECT Dish.*, Ingredient.Name as Ingredient, Description.Name as Description "
						+ "FROM Dish " + "	INNER JOIN Ingredient on IngredientId = Ingredient.Id "
						+ "	INNER JOIN Description on DescriptionId = Description.Id " + "WHERE Id = ?");
	}

	public int insert(int typeId, int ingredientId, int descriptionId, String name, int price, int speed,
			int difficulty) throws SQLException {
		insert.clearParameters();

		insert.setInt(1, typeId);
		insert.setInt(2, ingredientId);
		insert.setInt(3, descriptionId);
		insert.setString(4, name);
		insert.setInt(5, price);
		insert.setInt(6, speed);
		insert.setInt(7, difficulty);

		insert.executeUpdate();
		try (ResultSetIterator<Integer> results = new ResultSetIterator<Integer>(insert.getGeneratedKeys(),
				set -> set.getInt(1))) {
			return Iterators.getOnlyElement(results);
		}
	}

	@Override
	public void close() throws SQLException {
		insert.close();
		getIds.close();
	}

	public List<Integer> getIds(Type type) throws SQLException {
		this.getIds.clearParameters();

		this.getIds.setInt(1, type.getId());
		try (ResultSetIterator<Integer> ids = new ResultSetIterator<>(this.getIds.executeQuery(),
				set -> set.getInt(1))) {
			ArrayList<Integer> result = new ArrayList<>();
			Iterators.addAll(result, ids);
			return result;
		}
	}

	public DishInfo select(int selectedId) throws SQLException {
		select.clearParameters();

		select.setInt(1, selectedId);

		try (ResultSetIterator<DishInfo> results = new ResultSetIterator<DishInfo>(select.executeQuery(),
				new DishToDishInfo())) {
			return Iterators.getOnlyElement(results);
		}
	}

	private class DishToDishInfo implements Function<ResultSet, DishInfo> {

		@Override
		public DishInfo apply(ResultSet set) throws SQLException {
			Type type = Type.getById(set.getInt("TypeId"));

			return new DishInfo(set.getInt("Id"), type, set.getString("Name"), set.getInt("Price"), set.getInt("Speed"),
					set.getInt("Difficulty"), set.getString("Description"), set.getString("Ingredient"),
					new ArrayList<String>(), "", "");
		}

	}

}