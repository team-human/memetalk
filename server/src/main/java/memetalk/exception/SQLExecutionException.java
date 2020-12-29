package memetalk.exception;

public class SQLExecutionException extends RuntimeException {

  private static final long serialVersionUID = -7952887472093753131L;

  public SQLExecutionException(Throwable cause) {
    super(cause);
  }

  public SQLExecutionException(String message) {
    super(message);
  }
}
