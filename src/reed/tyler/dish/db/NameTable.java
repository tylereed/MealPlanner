package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.OptionalInt;

import com.google.common.collect.Iterators;

import reed.tyler.dish.db.iterators.ResultSetIterator;

public abstract class NameTable implements AutoCloseable {
	
	private PreparedStatement insert;

	private PreparedStatement select;
	
	public NameTable(Connection connection, String tableName) throws SQLException {
		String selectQuery = String.format("SELECT Id FROM %s WHERE Name = ?", tableName);
		String insertQuery = String.format("INSERT INTO %s (Name) VALUES (?)", tableName);
		
		select = connection.prepareStatement(selectQuery);
		insert = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
	}

	public int selectOrInsert(String name) throws SQLException {
		OptionalInt id = select(name);
		if (id.isPresent()) {
			return id.getAsInt();
		} else {
			return insert(name);
		}
	}

	public OptionalInt select(String name) throws SQLException {
		select.clearParameters();
		select.setString(1, name);
		try (ResultSetIterator<OptionalInt> results = new ResultSetIterator<OptionalInt>(select.executeQuery(),
				set -> OptionalInt.of(set.getInt(1)))) {

			return Iterators.getOnlyElement(results, OptionalInt.empty());
		}
	}

	public int insert(String name) throws SQLException {
		insert.clearParameters();
		insert.setString(1, name);
		
		insert.executeUpdate();
		try (ResultSetIterator<Integer> results = new ResultSetIterator<Integer>(insert.getGeneratedKeys(),
				set -> set.getInt(1))) {

			return Iterators.getOnlyElement(results);
		}
		
	}

	@Override
	public void close() throws SQLException {
		select.close();
		insert.close();
	}
}
