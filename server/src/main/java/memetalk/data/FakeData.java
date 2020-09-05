package memetalk.data;

import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.Topic;
import memetalk.model.User;

public class FakeData {
    public static List<User> generateFakeUsers() {
        return Arrays.asList(User.builder().id("userId1").name("John").build());
    }

    public static List<Meme> generateFakeMemes() {
        MemeCounter memeCounter =
                MemeCounter.builder()
                        .likeCount(100)
                        .dislikeCount(2)
                        .shareCount(2)
                        .commentCount(3)
                        .build();

        return Arrays.asList(
                Meme.builder()
                        .id("memeId1")
                        .author(generateFakeUsers().get(0))
                        .tags(ImmutableList.of("#comic"))
                        .url("testUrl")
                        .createTime(Instant.parse("2020-09-05T23:01:15.010435Z").toString())
                        .counter(memeCounter)
                        .build());
    }

    public static List<Topic> generateFakeTopic() {
        return Arrays.asList(
                Topic.builder()
                        .memes(generateFakeMemes())
                        .tag(generateFakeMemes().get(0).getTags().get(0))
                        .build());
    }
}
