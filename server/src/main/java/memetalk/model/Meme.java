package memetalk.model;

import java.util.List;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meme {
  private String id;
  private User author;
  private String url;
  @Builder.Default
  private List<String> tags = new ArrayList();
  private String createTime;
  private MemeCounter counter;
  private byte[] image;
}
