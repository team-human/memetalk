package memetalk.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meme {
  private String id;
  private User author;
  private String url;
  private List<String> tags;
  private String createTime;
  private MemeCounter counter;
  private byte[] image;
}
