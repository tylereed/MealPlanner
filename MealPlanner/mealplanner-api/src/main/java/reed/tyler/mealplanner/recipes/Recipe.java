package reed.tyler.mealplanner.recipes;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reed.tyler.mealplanner.ingredients.Ingredient;
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Ingredient> ingredients;

	public Recipe(long id, String name, String directions, String location, int price, int speed, int difficulty) {
		this(id, name, directions, location, price, speed, difficulty, new HashSet<>());
	}

	@Override
	public Long getEntityId() {
		return id;
	}

}
