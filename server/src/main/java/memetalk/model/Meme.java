package memetalk.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meme {
  private String id;
  private User author;
  private String url; // DEPRECATED: use `image` instead.
  @Builder.Default private List<String> tags = new ArrayList();
  private String createTime;
  private MemeCounter counter;
  private byte[] image;
}
