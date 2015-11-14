package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.SQLException;

public class Recipe extends NameTable {

	public Recipe(Connection connection) throws SQLException {
		super(connection, "Recipe");
	}
}
