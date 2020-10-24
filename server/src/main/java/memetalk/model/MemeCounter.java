package memetalk.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class MemeCounter {
  @NonNull private Integer likeCount;
  @NonNull private Integer dislikeCount;
  @NonNull private Integer shareCount;
  @NonNull private Integer commentCount;
}
