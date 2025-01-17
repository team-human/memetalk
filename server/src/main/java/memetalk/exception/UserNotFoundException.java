package memetalk.exception;

public class UserNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -3321905986321120709L;

  public UserNotFoundException(Throwable cause) {
    super(cause);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
