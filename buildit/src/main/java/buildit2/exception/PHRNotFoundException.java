package buildit2.exception;

public class PHRNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PHRNotFoundException(Long id) {
		super(String.format("PHR Not found! (Plant id: %d)", id));
	}
}
