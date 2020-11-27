package memetalk.controller.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.ConfigReader;
import memetalk.controller.StaticFileManager;
import memetalk.database.DatabaseAdapter;
import memetalk.model.CreateUserInput;
import memetalk.model.File;
import memetalk.model.LoginUser;
import memetalk.model.Meme;
import memetalk.model.User;
import org.springframework.stereotype.Component;

/**
 * DataFetchers is a collection of DataFetcher of each GraphQL entry point. DataFetcher parses the
 * request content and accesses database adapter to return the response.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataFetchers {

  @NonNull private final DatabaseAdapter databaseAdapter;
  @NonNull private final StaticFileManager staticFileManager;
  @NonNull private final DataFetchersAuth dataFetchersAuth;
  @NonNull private final ObjectMapper objectMapper;

  public static List<User> users = generateFakeUsers();
  public static List<String> tags = generateFakeTags();
  public static List<Meme> memes = generateFakeMemes();

  // TODO: Replace fake data.
  public DataFetcher<User> getCurrentUserDataFetcher() {
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

  public DataFetcher<LoginUser> loginUser() {
    return dataFetchingEnvironment -> {
      final String id = dataFetchingEnvironment.getArgument("id");
      final String password = dataFetchingEnvironment.getArgument("password");
      return dataFetchersAuth.loginUserAuth(id, password);
    };
  }

  public DataFetcher<LoginUser> createUser() {
    return dataFetchingEnvironment -> {
      final CreateUserInput userInput =
          objectMapper.convertValue(
              dataFetchingEnvironment.getArgument("userInfo"), CreateUserInput.class);
      return dataFetchersAuth.createUserAuth(userInput);
    };
  }

  // TODO: Replace fake data.
  public DataFetcher<List<Meme>> getMemesByAuthorIdDataFetcher() {
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

  //  @PreAuthorize("isAuthenticated()") // this has an issue
  public DataFetcher<Meme> createMemeDataFetcher() {
    return dataFetchingEnvironment -> {
      final File file = dataFetchingEnvironment.getArgument("file");
      final List<String> tags = dataFetchingEnvironment.getArgument("tags");
      Meme meme = Meme.builder().tags(tags).image(file.getContent()).build();
      dataFetchersAuth.createMemeAuth(meme);
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
      String url =
          this.staticFileManager.write(configReader, meme.getId() + ".png", meme.getImage());
      meme.setUrl(url);
    }
  }
}
