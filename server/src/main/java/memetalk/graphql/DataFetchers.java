package memetalk.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import java.util.List;
import memetalk.database.DatabaseAdapter;
import memetalk.model.File;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.User;

/**
 * DataFetchers is a collection of DataFetcher of each GraphQL entry point. DataFetcher parses the
 * request content and accesses database adapter to return the response.
 */
public class DataFetchers {

  public DatabaseAdapter databaseAdapter;

  public static List<User> users = generateFakeUsers();
  public static List<String> tags = generateFakeTags();
  public static List<Meme> memes = generateFakeMemes();

  public DataFetchers(DatabaseAdapter databaseAdapter) throws Exception {
    this.databaseAdapter = databaseAdapter;
  }

  // TODO: Replace fake data.
  public DataFetcher getCurrentUserDataFetcher() {
    return dataFetchingEnvironment -> users.get(0);
  }

  public DataFetcher<List<String>> getPopularTagsDataFetcher() {
    return dataFetchingEnvironment -> databaseAdapter.getTags();
  }

  // TODO: Replace fake data.
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

  // TODO: Replace fake data.
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

  // TODO: Replace fake data.
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
