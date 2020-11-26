package memetalk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class CreateUserInput {
  @JsonProperty
  private final String id;
  @JsonProperty
  private final String password;
  @JsonProperty
  private final String name;
}
