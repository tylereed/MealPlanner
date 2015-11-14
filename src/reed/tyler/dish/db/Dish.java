package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterators;

import reed.tyler.dish.Type;
import reed.tyler.dish.db.iterators.ResultSetIterator;

public class Dish implements AutoCloseable {

	private PreparedStatement insert;

	private PreparedStatement getIds;

	private PreparedStatement select;

	public Dish(Connection connection) throws SQLException {
		this.insert = connection.prepareStatement("INSERT INTO Dish "
				+ "(TypeId, IngredientId, DescriptionId, Name, Price, Speed, Difficulty) " + "VALUES (?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		this.getIds = connection.prepareStatement("SELECT Id FROM Dish WHERE TypeId = ?");

		this.select = connection.prepareStatement("SELECT * FROM Dish WHERE Id = ?");
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

	public reed.tyler.dish.Dish select(int selectedId) throws SQLException {
		select.clearParameters();

		select.setInt(1, selectedId);

		try (ResultSetIterator<reed.tyler.dish.Dish> results = new ResultSetIterator<reed.tyler.dish.Dish>(select.executeQuery(),
				set -> new reed.tyler.dish.Dish(set.getString("Name")))) {
			return Iterators.getOnlyElement(results);
		}
	}

}