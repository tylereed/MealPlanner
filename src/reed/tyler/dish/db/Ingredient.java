package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.SQLException;

public class Ingredient extends NameTable {

	public Ingredient(Connection connection) throws SQLException {
		super(connection, "Ingredient");
	}

}
