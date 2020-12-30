package memetalk.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {
  @NonNull private String id;
  @NonNull String username;
  private String name;
  // TODO: Replace Set<String> to Set<Enum>.
  // TODO: User is currently a data model used as part of GraphQL response. We
  // might want to move the followings to somewhere else so it won't be exposed
  // to GraphQL.
  private Set<String> roles;
  private String password;
}
