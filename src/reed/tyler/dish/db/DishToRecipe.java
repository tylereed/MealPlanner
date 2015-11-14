package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterators;

import reed.tyler.dish.db.iterators.ResultSetIterator;

public class DishToRecipe implements AutoCloseable {

	private PreparedStatement insert;

	private PreparedStatement select;

	public DishToRecipe(Connection connection) throws SQLException {
		this.insert = connection.prepareStatement("INSERT INTO DishToRecipe (Id, RecipeId, Page) VALUES (?,?,?)");
		this.select = connection.prepareStatement("SELECT Recipe.Name " + 
			"FROM DishToRecipe " + 
			"	INNER JOIN Recipe on Recipe.Id = RecipeId " + 
			"WHERE Id = ? ");
	}

	public void insert(int dishId, int recipeId, String page) throws SQLException {
		insert.clearParameters();

		insert.setInt(1, dishId);
		insert.setInt(2, recipeId);
		insert.setString(3, page);

		insert.execute();
	}

	public List<String> select(int dishId) throws SQLException {
		select.clearParameters();

		select.setInt(1, dishId);

		try (ResultSetIterator<String> names = new ResultSetIterator<>(select.executeQuery(),
				set -> set.getString(1))) {
			ArrayList<String> result = new ArrayList<>();
			Iterators.addAll(result, names);
			return result;
		}
	}

	@Override
	public void close() throws SQLException {
		insert.close();
		select.close();
	}

}
