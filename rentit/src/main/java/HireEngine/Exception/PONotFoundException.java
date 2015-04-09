package HireEngine.Exception;

public class PONotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PONotFoundException(Long id) {
		super(String.format("Plant not found! (Plant id: %d)", id));
	}
}
