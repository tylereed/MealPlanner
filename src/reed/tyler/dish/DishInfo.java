package reed.tyler.dish;

import com.google.common.collect.ImmutableList;

public class DishInfo {

	private int id;

	private Type type;

	private String name;

	private int price;

	private int speed;

	private int difficulty;

	private String description;

	private String ingredient;

	private ImmutableList<String> methods;

	private String recipe;

	private String page;

	public DishInfo(int id, Type type, String name, int price, int speed, int difficulty, String description,
			String ingredient, Iterable<String> methods, String recipe, String page) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.price = price;
		this.speed = speed;
		this.difficulty = difficulty;
		this.description = description;
		this.ingredient = ingredient;
		this.methods = ImmutableList.copyOf(methods);
		this.recipe = recipe;
		this.page = page;
	}

	public DishInfo(int id, Type type, String name, int price, int speed, int difficulty, String description,
			String ingredient, String recipe, String page, String... methods) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.price = price;
		this.speed = speed;
		this.difficulty = difficulty;
		this.description = description;
		this.ingredient = ingredient;
		this.methods = ImmutableList.copyOf(methods);
		this.recipe = recipe;
		this.page = page;
	}

	public DishInfo(String name) {
		this(0, Type.Main, name, 0, 0, 0, "", "", "", "");
	}

	public String getDescription() {
		return description;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getId() {
		return id;
	}

	public String getIngredient() {
		return ingredient;
	}

	public ImmutableList<String> getMethods() {
		return methods;
	}

	public String getName() {
		return name;
	}

	public String getPage() {
		return page;
	}

	public int getPrice() {
		return price;
	}

	public String getRecipe() {
		return recipe;
	}

	public int getSpeed() {
		return speed;
	}

	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(getName());
		result.append(" ");
		
		for (int i = 0; i < getPrice(); ++i) {
			result.append("$");
		}
		
		result.append(" ");
		for (int i = 0; i < getSpeed(); ++i) {
			result.append("\u231B");
		}
		
		switch (getDifficulty()) {
		case 1:
			result.append("\u263A");
			break;
		case 3:
			result.append("\u2639");
			break;
		}
		
		return result.toString();
	}

}
