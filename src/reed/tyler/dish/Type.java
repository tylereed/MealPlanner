package reed.tyler.dish;

public enum Type {
	Main("Main", 0), Side("Side", 1), Vegetable("Vegetable", 2);

	private int id;

	private String name;

	Type(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public static Type getById(int id) {
		switch (id) {
		case 0:
			return Main;
		case 1:
			return Side;
		case 2:
			return Vegetable;
		default:
			throw new RuntimeException("Unknown Type.Id " + id);
		}
	}

}
