package memetalk.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class User {
  @NonNull private String id;
  @NonNull private String name;
}
