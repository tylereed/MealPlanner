package reed.tyler.mealplanner.ingredients;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reed.tyler.mealplanner.recipes.Recipe;
import reed.tyler.mealplanner.utils.Identifiable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Identifiable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50, nullable = false)
	@NonNull
	private String name;

	@Column(nullable = false)
	@NonNull
	private boolean exotic;

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Recipe> recipes;

	public Ingredient(long id, String name, boolean exotic) {
		this(id, name, exotic, new HashSet<>());
	}

	@Override
	public Long getEntityId() {
		return id;
	}

}
