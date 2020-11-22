package reed.tyler.mealplanner.recipes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.lang.NonNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reed.tyler.mealplanner.utils.Identifiable;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recipe implements Identifiable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50, nullable = false)
	@NonNull
	private String name;

	@Lob
	@Column(nullable = false)
	@NonNull
	private String directions;

	@Column(length = 50, nullable = false)
	@NonNull
	private String location;

	private int price;

	private int speed;

	private int difficulty;
	
	@Override
	public Long getEntityId() {
		return id;
	}

}
