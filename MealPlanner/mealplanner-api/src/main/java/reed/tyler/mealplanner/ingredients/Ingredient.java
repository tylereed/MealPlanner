package reed.tyler.mealplanner.ingredients;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reed.tyler.mealplanner.utils.Identifiable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Identifiable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	@NonNull
	private String name;

	@Column(nullable = false)
	@NonNull
	private boolean exotic;

}
