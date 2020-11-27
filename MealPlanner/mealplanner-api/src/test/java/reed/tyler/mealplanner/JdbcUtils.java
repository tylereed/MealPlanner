package reed.tyler.mealplanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUtils {

	private JdbcTemplate jdbc;

	@Autowired
	public JdbcUtils(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public void truncateTables(String... tables) {
		jdbc.execute("SET REFERENTIAL_INTEGRITY FALSE");

		for (String table : tables) {
			jdbc.execute("TRUNCATE TABLE " + table + " RESTART IDENTITY;");
		}

		jdbc.execute("SET REFERENTIAL_INTEGRITY TRUE");
	}

}
