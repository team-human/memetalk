package memetalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInput {
  @JsonProperty private String username = "test";
  @JsonProperty private String password = "password";
  @JsonProperty private String name = "name";
}
