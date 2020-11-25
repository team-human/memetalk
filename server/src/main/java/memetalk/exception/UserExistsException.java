package memetalk.exception;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserExistsException extends RuntimeException {
  private static final long serialVersionUID = 2060508008557739425L;
  @NonNull private final String id;
}
