package reed.tyler.dish.db.iterators;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.common.collect.AbstractIterator;

public class ResultSetIterator<T> extends AbstractIterator<T> implements AutoCloseable {
	
	public interface Function<T extends ResultSet, S> {
		S apply(T set) throws SQLException;
	}
	
	private ResultSet set;
	
	private Function<ResultSet, T> create;
	
	public ResultSetIterator(ResultSet set, Function<ResultSet,T> create) {
		this.set = set;
		this.create = create;
	}

	@Override
	protected T computeNext() {
		try {
			if (set.next()) {
				return create.apply(set);
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return endOfData();
	}

	@Override
	public void close() throws SQLException {
		set.close();
	}

}
