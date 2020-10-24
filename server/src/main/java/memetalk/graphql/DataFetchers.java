package memetalk.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import java.util.List;
import memetalk.model.File;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.User;
import org.springframework.stereotype.Component;

/* Fetching the data for GraphQL query */
@Component
public class DataFetchers {
  public static List<User> users = generateFakeUsers();
  public static List<String> tags = generateFakeTags();
  public static List<Meme> memes = generateFakeMemes();

  public DataFetcher getAuthorDataFetcher() {
    return dataFetchingEnvironment -> {
      final Meme meme = dataFetchingEnvironment.getSource();
      return meme.getAuthor();
    };
  }

  public DataFetcher getCurrentUserDataFetcher() {
    return dataFetchingEnvironment -> users.get(0);
  }

  public DataFetcher getPopularTagsDataFetcher() {
    return dataFetchingEnvironment -> ImmutableList.of(tags.get(0));
  }

  public DataFetcher getMemesByTagDataFetcher() {
    return dataFetchingEnvironment -> {
      final String tag = dataFetchingEnvironment.getArgument("tag");
      if (!tags.contains(tag)) {
        return ImmutableList.of();
      } else {
        return memes.stream()
            .filter(meme -> meme.getTags().contains(tag))
            .collect(ImmutableList.toImmutableList());
      }
    };
  }

  public DataFetcher getMemesByAuthorIdDataFetcher() {
    return dataFetchingEnvironment -> {
      final String userId = dataFetchingEnvironment.getArgument("userId");
      final boolean validUserId = users.stream().anyMatch(user -> user.getId().equals(userId));
      if (!validUserId) {
        return ImmutableList.of();
      } else {
        return memes.stream()
            .filter(meme -> meme.getAuthor().getId().equals(userId))
            .collect(ImmutableList.toImmutableList());
      }
    };
  }

  public DataFetcher createMemeDataFetcher() {
    return dataFetchingEnvironment -> {
      final File file = dataFetchingEnvironment.getArgument("file");
      final List<String> tags = dataFetchingEnvironment.getArgument("tags");

      Meme meme =
          Meme.builder()
              .id("createMemeId")
              .counter(
                  MemeCounter.builder()
                      .commentCount(123)
                      .dislikeCount(0)
                      .shareCount(321)
                      .likeCount(1)
                      .build())
              .tags(tags)
              .url("randomURL")
              .author(users.get(0))
              .createTime("2020-09-27T03:19:31.107115Z")
              .image(file.getContent())
              .build();

      return meme;
    };
  }
}