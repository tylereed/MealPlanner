package reed.tyler.dish.db;

import java.sql.Connection;
import java.sql.SQLException;

public class Method extends NameTable {

	public Method(Connection connection) throws SQLException {
		super(connection, "Method");
	}
}
