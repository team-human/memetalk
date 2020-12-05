package memetalk.exception;

public class BadTokenException extends RuntimeException {
  private static final long serialVersionUID = 7624511282335429480L;

  public BadTokenException(String message) {
    super(message);
  }
}
