package reed.tyler.mealplanner.db;

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

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

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

}
