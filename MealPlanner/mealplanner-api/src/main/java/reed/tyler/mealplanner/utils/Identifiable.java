package reed.tyler.mealplanner.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable<T> {

	T getId();

	@JsonIgnore
	default String getIdName() {
		return "id";
	}

}
