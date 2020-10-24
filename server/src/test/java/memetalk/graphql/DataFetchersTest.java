package memetalk.graphql;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import memetalk.model.File;
import memetalk.model.Meme;
import memetalk.model.MemeCounter;
import memetalk.model.User;
import org.junit.Before;
import org.junit.Test;

public class DataFetchersTest {
  private DataFetchers dataFetchers;
  private DataFetchingEnvironment dataFetchingEnvironment;

  @Before
  public void setUp() {
    dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
    dataFetchers = new DataFetchers();
  }

  @Test
  public void testGetAuthorDataFetcher() throws Exception {
    Meme meme = generateFakeMemes().get(0);

    when(dataFetchingEnvironment.getSource()).thenReturn(meme);
    User actualAuthor = (User) dataFetchers.getAuthorDataFetcher().get(dataFetchingEnvironment);

    User expectedAuthor = meme.getAuthor();
    assertEquals(expectedAuthor, actualAuthor);
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
    List<String> actualTags =
        (List<String>) dataFetchers.getPopularTagsDataFetcher().get(dataFetchingEnvironment);

    assertEquals(expectedTags, actualTags);
  }

  @Test
  public void testGetMemesByTagDataFetcherValidTag() throws Exception {
    String argumentTag = generateFakeTags().get(0);

    when(dataFetchingEnvironment.getArgument("tag")).thenReturn(argumentTag);

    List<Meme> actualMemes =
        (List<Meme>) dataFetchers.getMemesByTagDataFetcher().get(dataFetchingEnvironment);

    List<Meme> expectedMemes =
        generateFakeMemes().stream()
            .filter(meme -> meme.getTags().contains(argumentTag))
            .collect(ImmutableList.toImmutableList());

    assertEquals(expectedMemes, actualMemes);
  }

  @Test
  public void testGetMemesByTagDataFetcherInvalidTag() throws Exception {
    String argumentTag = "invalidTag";

    when(dataFetchingEnvironment.getArgument("tag")).thenReturn(argumentTag);

    List<Meme> actualMemes =
        (List<Meme>) dataFetchers.getMemesByTagDataFetcher().get(dataFetchingEnvironment);

    assertEquals(ImmutableList.of(), actualMemes);
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

    Meme expectedMeme =
        Meme.builder()
            .id("createMemeId")
            .counter(
                MemeCounter.builder()
                    .commentCount(123)
                    .dislikeCount(0)
                    .shareCount(321)
                    .likeCount(1)
                    .build())
            .tags(argumentTags)
            .url("randomURL")
            .author(generateFakeUsers().get(0))
            .createTime("2020-09-27T03:19:31.107115Z")
            .image(fakeFile.getContent())
            .build();

    assertEquals(expectedMeme, actualMeme);
  }
}
