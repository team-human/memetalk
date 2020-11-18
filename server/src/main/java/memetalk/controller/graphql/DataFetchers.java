package memetalk.controller.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import java.util.List;
import memetalk.ConfigReader;
import memetalk.controller.StaticFileManager;
import memetalk.database.DatabaseAdapter;
import memetalk.model.File;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.User;

/**
 * DataFetchers is a collection of DataFetcher of each GraphQL entry point.
 * DataFetcher parses the request content and accesses database adapter to
 * return the response.
 */
public class DataFetchers {

  public DatabaseAdapter databaseAdapter;
  public StaticFileManager staticFileManager;

  public static List<User> users = generateFakeUsers();
  public static List<String> tags = generateFakeTags();
  public static List<Meme> memes = generateFakeMemes();

  public DataFetchers(DatabaseAdapter databaseAdapter,
                      StaticFileManager staticFileManager) throws Exception {
    this.databaseAdapter = databaseAdapter;
    this.staticFileManager = staticFileManager;
  }

  // TODO: Replace fake data.
  public DataFetcher getCurrentUserDataFetcher() {
    return dataFetchingEnvironment -> users.get(0);
  }

  public DataFetcher<List<String>> getPopularTagsDataFetcher() {
    return dataFetchingEnvironment -> databaseAdapter.getTags();
  }

  public DataFetcher<List<Meme>> getMemesByTagDataFetcher() {
    return dataFetchingEnvironment -> {
      final String tag = dataFetchingEnvironment.getArgument("tag");
      List<Meme> memes = databaseAdapter.getMemesByTag(tag);
      fillUrl(memes);
      return memes;
    };
  }

  // TODO: Replace fake data.
  public DataFetcher getMemesByAuthorIdDataFetcher() {
    return dataFetchingEnvironment -> {
      final String userId = dataFetchingEnvironment.getArgument("userId");
      final boolean validUserId =
          users.stream().anyMatch(user -> user.getId().equals(userId));
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
      Meme meme = Meme.builder().tags(tags).image(file.getContent()).build();
      databaseAdapter.addMeme(meme);
      // TODO: Consider if we need to return a valid meme here or some response
      // status is enough.
      return meme;
    };
  }

  private void fillUrl(List<Meme> memes) throws Exception {
    ConfigReader configReader = ConfigReader.getInstance();
    for (Meme meme : memes) {
      // TODO: Store file extension in Meme and don't assume all of them are png
      // files.
      String url = this.staticFileManager.write(
          configReader, meme.getId() + ".png", meme.getImage());
      meme.setUrl(url);
    }
  }
}
