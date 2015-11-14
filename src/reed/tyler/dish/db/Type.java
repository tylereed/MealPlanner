package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.common.collect.Iterators;

import reed.tyler.dish.db.iterators.ResultSetIterator;

public class Type implements AutoCloseable {
	
	private PreparedStatement select;
	
	public Type(Connection connection) throws SQLException {
		this.select = connection.prepareStatement("SELECT Id FROM Type WHERE Name = ?");
	}
	
	public int select(String name) throws SQLException {
		select.clearParameters();
		select.setString(1, name);
		try (ResultSetIterator<Integer> results = new ResultSetIterator<Integer>(select.executeQuery(),
				set -> set.getInt(1))) {

			return Iterators.getOnlyElement(results);
		}
	}

	@Override
	public void close() throws SQLException {
		select.close();
	}

}
