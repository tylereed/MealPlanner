package reed.tyler.mealplanner;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.springframework.test.web.servlet.ResultMatcher;

public class ResultMatcherExtension {

	public static ResultMatcher matchBadRequest(String... errorMsg) {

		ArrayList<ResultMatcher> matchers = new ArrayList<>();
		matchers.add(status().isBadRequest());

		for (int i = 0; i < errorMsg.length; i++) {
			String msg = errorMsg[i];
			matchers.add(jsonPath("$.messages[%d]", i).value(msg));
		}

		return matchAll(matchers.toArray(ResultMatcher[]::new));
	}

}
