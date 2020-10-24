package memetalk.data;

import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.User;

/**
 * FakeDataGenerator generates fake data to service. We use FakeDataGenerator to provide data for
 * the client test against while we are building the real data servered by a database.
 */
public class FakeDataGenerator {
  public static List<User> generateFakeUsers() {
    return Arrays.asList(
        User.builder().id("userId1").name("John").build(),
        User.builder().id("userId2").name("Alice").build(),
        User.builder().id("userId3").name("Bob").build());
  }

  public static List<String> generateFakeTags() {
    return Arrays.asList("#comic", "#movie", "#election");
  }

  public static List<Meme> generateFakeMemes() {
    MemeCounter memeCounter =
        MemeCounter.builder().likeCount(100).dislikeCount(2).shareCount(2).commentCount(3).build();

    return Arrays.asList(
        Meme.builder()
            .id("memeId1")
            .author(generateFakeUsers().get(0))
            .tags(ImmutableList.of(generateFakeTags().get(0)))
            .url("testUrl")
            .createTime(Instant.parse("2020-09-05T23:01:15.010435Z").toString())
            .counter(memeCounter)
            .build(),
        Meme.builder()
            .id("memeId2")
            .author(generateFakeUsers().get(1))
            .tags(ImmutableList.of(generateFakeTags().get(0)))
            .url("testUrl1")
            .createTime(Instant.parse("2020-09-18T23:02:01.010435Z").toString())
            .counter(memeCounter)
            .build(),
        Meme.builder()
            .id("memeId3")
            .author(generateFakeUsers().get(2))
            .tags(ImmutableList.of(generateFakeTags().get(0)))
            .url("testUrl2")
            .createTime(Instant.parse("2020-09-20T23:02:15.010435Z").toString())
            .counter(memeCounter)
            .build());
  }
}
