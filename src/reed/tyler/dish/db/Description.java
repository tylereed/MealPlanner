package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.SQLException;

public class Description extends NameTable {

	public Description(Connection connection) throws SQLException {
		super(connection, "Description");
	}
}
