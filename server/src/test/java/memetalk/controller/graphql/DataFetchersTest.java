package memetalk.controller.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import memetalk.controller.StaticFileManager;
import memetalk.database.DatabaseAdapter;
import memetalk.model.File;
import memetalk.model.Meme;
import memetalk.model.User;
import memetalk.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;

public class DataFetchersTest {
  private DataFetchers dataFetchers;
  private StaticFileManager staticFileManager;
  private DataFetchingEnvironment dataFetchingEnvironment;
  private DatabaseAdapter databaseAdapter;
  private UserService userService;
  private AuthenticationProvider authenticationProvider;
  private DataFetchersAuth dataFetchersAuth;

  @Before
  public void setUp() throws Exception {
    dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    databaseAdapter = mock(DatabaseAdapter.class);
    staticFileManager = mock(StaticFileManager.class);
    userService = mock(UserService.class);
    authenticationProvider = mock(AuthenticationProvider.class);
    dataFetchersAuth = new DataFetchersAuth(userService, authenticationProvider, databaseAdapter);
    dataFetchers =
        new DataFetchers(databaseAdapter, staticFileManager, dataFetchersAuth, new ObjectMapper());
  }

  @Test
  public void testGetCurrentUserDataFetcher() throws Exception {
    User expectedUser = generateFakeUsers().get(0);
    User actualUser = (User) dataFetchers.getCurrentUserDataFetcher().get(dataFetchingEnvironment);

    assertEquals(expectedUser, actualUser);
  }

  @Test
  public void testGetPopularTagsDataFetcher() throws Exception {
    List<String> expectedTags = ImmutableList.of(generateFakeTags().get(0));
    when(databaseAdapter.getTags()).thenReturn(expectedTags);

    List<String> actualTags = dataFetchers.getPopularTagsDataFetcher().get(dataFetchingEnvironment);
    assertEquals(expectedTags, actualTags);
  }

  @Test
  public void testGetMemesByTagDataFetcherSucceed() throws Exception {
    List<Meme> memesFromDatabase = new ArrayList<>();
    memesFromDatabase.add(Meme.builder().id("id1").build());
    memesFromDatabase.add(Meme.builder().id("id2").build());

    when(dataFetchingEnvironment.getArgument("tag")).thenReturn("fake_tag");
    when(databaseAdapter.getMemesByTag("fake_tag")).thenReturn(memesFromDatabase);
    when(staticFileManager.write(any(), eq("id1.png"), any())).thenReturn("url1");
    when(staticFileManager.write(any(), eq("id2.png"), any())).thenReturn("url2");

    List<Meme> actualMemes = dataFetchers.getMemesByTagDataFetcher().get(dataFetchingEnvironment);
    List<Meme> expectedMemes = new ArrayList<>();
    expectedMemes.add(Meme.builder().id("id1").url("url1").build());
    expectedMemes.add(Meme.builder().id("id2").url("url2").build());

    assertEquals(expectedMemes, actualMemes);
  }

  @Test
  public void testGetMemesByTagDataFetcherNothingMatched() throws Exception {
    when(dataFetchingEnvironment.getArgument("tag")).thenReturn("fake_tag");
    when(databaseAdapter.getMemesByTag("fake_tag")).thenReturn(new ArrayList());
    List<Meme> actualMemes = dataFetchers.getMemesByTagDataFetcher().get(dataFetchingEnvironment);
    assertEquals(new ArrayList(), actualMemes);
  }

  @Test
  public void testGetMemesByAuthorIdDataFetcherValidUserId() throws Exception {
    String argumentUserId = generateFakeUsers().get(0).getId();

    when(dataFetchingEnvironment.getArgument("userId")).thenReturn(argumentUserId);

    List<Meme> actualMemes =
        (List<Meme>) dataFetchers.getMemesByAuthorIdDataFetcher().get(dataFetchingEnvironment);

    List<Meme> expectedMemes =
        generateFakeMemes().stream()
            .filter(meme -> meme.getAuthor().getId().equals(argumentUserId))
            .collect(ImmutableList.toImmutableList());

    assertEquals(expectedMemes, actualMemes);
  }

  @Test
  public void testGetMemesByAuthorIdDataFetcherInvalidUserId() throws Exception {
    String argumentUserId = "fakeRandomWrongUserId";

    when(dataFetchingEnvironment.getArgument("userId")).thenReturn(argumentUserId);

    List<Meme> actualMemes =
        (List<Meme>) dataFetchers.getMemesByAuthorIdDataFetcher().get(dataFetchingEnvironment);

    assertEquals(ImmutableList.of(), actualMemes);
  }

  @Test
  public void testCreateMemeDataFetcher() throws Exception {
    List<String> argumentTags = ImmutableList.of("tag1", "tag2");
    File fakeFile = File.builder().content("dummyData".getBytes()).type("fakeType").build();

    when(dataFetchingEnvironment.getArgument("file")).thenReturn(fakeFile);
    when(dataFetchingEnvironment.getArgument("tags")).thenReturn(argumentTags);

    Meme actualMeme = (Meme) dataFetchers.createMemeDataFetcher().get(dataFetchingEnvironment);
    Meme expectedMeme = Meme.builder().tags(argumentTags).image(fakeFile.getContent()).build();

    assertEquals(expectedMeme, actualMeme);
  }
}
