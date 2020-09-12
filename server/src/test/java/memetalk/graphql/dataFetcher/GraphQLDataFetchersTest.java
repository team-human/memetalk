package memetalk.graphql.dataFetcher;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTopic;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import memetalk.model.Meme;
import memetalk.model.Topic;
import memetalk.model.User;
import org.junit.Before;
import org.junit.Test;

public class GraphQLDataFetchersTest {
    private GraphQLDataFetchers graphQLDataFetchers;
    private DataFetchingEnvironment dataFetchingEnvironment;

    @Before
    public void setUp() {
        dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
        graphQLDataFetchers = new GraphQLDataFetchers();
    }

    @Test
    public void testGetTopicsDataFetcher() throws Exception {
        List<Topic> expected = generateFakeTopic();
        List<Topic> actual =
                (List<Topic>) graphQLDataFetchers.getTopicsFetcher().get(dataFetchingEnvironment);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetAuthorDataFetcher() throws Exception {
        Meme meme = generateFakeMemes().get(0);

        when(dataFetchingEnvironment.getSource()).thenReturn(meme);
        User actual =
                (User) graphQLDataFetchers.getAuthorDataFetcher().get(dataFetchingEnvironment);

        User expected = meme.getAuthor();
        assertEquals(expected, actual);
    }
}
