package memetalk.graphql.dataFetcher;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GraphQLDataFetchers {
    // fake data. May connect to DB
    private static List<Map<String, String>> posts =
            Arrays.asList(
                    ImmutableMap.of(
                            "id",
                            "post-1",
                            "title",
                            "fun story",
                            "pictureLink",
                            "url-1",
                            "authorId",
                            "author-1"),
                    ImmutableMap.of(
                            "id",
                            "post-2",
                            "title",
                            "happy life",
                            "pictureLink",
                            "url-2",
                            "authorId",
                            "author-2"));

    private static List<Map<String, String>> authors =
            Arrays.asList(
                    ImmutableMap.of(
                            "id", "author-1", "username", "Sam", "email", "sam@memetalk.com"),
                    ImmutableMap.of(
                            "id", "author-2", "username", "Heron", "email", "heron@memetalk.com"));

    public DataFetcher getPostByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String postId = dataFetchingEnvironment.getArgument("id");
            return posts.stream()
                    .filter(post -> post.get("id").equals(postId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> post = dataFetchingEnvironment.getSource();
            String authorId = post.get("authorId");
            return authors.stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }
}
