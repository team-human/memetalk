package memetalk.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Meme {
    @NonNull private String id;
    @NonNull private User author;
    @NonNull private String url;
    @NonNull private List<String> tags;
    @NonNull private String createTime;
    @NonNull private MemeCounter counter;
}
