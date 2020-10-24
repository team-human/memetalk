package memetalk.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class File {
  @NonNull private String type;
  @NonNull private byte[] content;
}
