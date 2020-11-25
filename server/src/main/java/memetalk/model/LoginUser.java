package memetalk.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginUser {
  private final String token;
  private final User user;
}
