package memetalk.graphql.dataFetcher;

import static memetalk.data.FakeDataGenerator.generateFakeTopic;

import graphql.schema.DataFetcher;
import java.util.List;
import memetalk.model.Meme;
import memetalk.model.Topic;
import org.springframework.stereotype.Component;

/* Fetching the data for GraphQL query */
@Component
public class GraphQLDataFetchers {
    public static List<Topic> topics = generateFakeTopic();

    public DataFetcher getTopicsFetcher() {
        return dataFetchingEnvironment -> topics;
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Meme meme = dataFetchingEnvironment.getSource();
            return meme.getAuthor();
        };
    }
}
