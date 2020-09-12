package memetalk.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Topic {
    @NonNull String tag;
    @NonNull List<Meme> memes;
}
