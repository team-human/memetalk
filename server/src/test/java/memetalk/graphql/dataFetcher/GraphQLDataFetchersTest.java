package memetalk.graphql.dataFetcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphQLDataFetchersTest {
    private GraphQLDataFetchers graphQLDataFetchers;
    private DataFetchingEnvironment dataFetchingEnvironment;

    @BeforeEach
    public void setUp() {
        dataFetchingEnvironment = mock(DataFetchingEnvironment.class);
        graphQLDataFetchers = new GraphQLDataFetchers();
    }

    @Test
    public void testGetPostByIdDataFetcher() throws Exception {
        final String postId = "post-1";
        when(dataFetchingEnvironment.getArgument("id")).thenReturn(postId);
        Map<String, String> actual =
                (Map<String, String>)
                        graphQLDataFetchers.getPostByIdDataFetcher().get(dataFetchingEnvironment);
        Map<String, String> expected =
                GraphQLDataFetchers.posts.stream()
                        .filter(post -> post.get("id").equals(postId))
                        .findFirst()
                        .orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAuthorDataFetcher() throws Exception {
        final String authorId = "author-1";
        ImmutableMap<Object, Object> postArgument =
                ImmutableMap.builder().put("authorId", authorId).build();
        when(dataFetchingEnvironment.getSource()).thenReturn(postArgument);
        Map<String, String> actual =
                (Map<String, String>)
                        graphQLDataFetchers.getAuthorDataFetcher().get(dataFetchingEnvironment);
        Map<String, String> expected =
                GraphQLDataFetchers.authors.stream()
                        .filter(author -> author.get("id").equals(authorId))
                        .findFirst()
                        .orElse(null);
        assertEquals(expected, actual);
    }
}
