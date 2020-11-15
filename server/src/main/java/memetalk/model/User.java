package memetalk.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {
  @NonNull private String id; // login userId
  @NonNull private String name; // user real name
  @NonNull private Set<String> roles;
  @NonNull private String email;
  @NonNull private String password;
}
