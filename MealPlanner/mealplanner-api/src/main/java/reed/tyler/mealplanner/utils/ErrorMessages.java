package reed.tyler.mealplanner.utils;

import java.util.List;

import lombok.Value;

@Value
public class ErrorMessages {

	private final List<String> messages;
	
	public ErrorMessages(String... messages) {
		this.messages = List.of(messages);
	}
	
}
