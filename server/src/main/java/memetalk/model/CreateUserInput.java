package memetalk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateUserInput {
  private final String id;
  private final String password;
  private final String name;
}
