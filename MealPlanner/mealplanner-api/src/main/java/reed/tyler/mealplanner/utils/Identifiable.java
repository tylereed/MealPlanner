package reed.tyler.mealplanner.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identifiable<T> {

	@JsonIgnore
	T getEntityId();

	@JsonIgnore
	default String getIdName() {
		return "id";
	}

}
