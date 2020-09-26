package memetalk.graphql.dataFetcher;

import static memetalk.data.FakeDataGenerator.generateFakeMemes;
import static memetalk.data.FakeDataGenerator.generateFakeTags;
import static memetalk.data.FakeDataGenerator.generateFakeTopic;
import static memetalk.data.FakeDataGenerator.generateFakeUsers;

import com.google.common.collect.ImmutableList;
import graphql.schema.DataFetcher;
import java.util.List;
import memetalk.model.Meme;
import memetalk.model.Topic;
import memetalk.model.User;
import org.springframework.stereotype.Component;

/* Fetching the data for GraphQL query */
@Component
public class GraphQLDataFetchers {
    public static List<Topic> topics = generateFakeTopic();
    public static List<User> users = generateFakeUsers();
    public static List<String> tags = generateFakeTags();
    public static List<Meme> memes = generateFakeMemes();

    public DataFetcher getTopicsDataFetcher() {
        return dataFetchingEnvironment -> topics;
    }

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
}
