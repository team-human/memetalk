package memetalk.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {
  @NonNull private String id; // login username
  @NonNull private String name;
  @NonNull private Set<String> roles;
  @NonNull private String password; // login password
}
