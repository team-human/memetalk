package memetalk.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import memetalk.graphql.dataFetcher.GraphQLDataFetchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/* Parsing GraphQL schema and creating runtime wiring to have executable schema */
@Component
public class GraphQLProvider {
    private static final String GRAPHQL_SCHEMA_NAME = "schema.graphql";

    @Autowired GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource(GRAPHQL_SCHEMA_NAME);
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(
                        TypeRuntimeWiring.newTypeWiring("Query")
                                .dataFetcher("topics", graphQLDataFetchers.getTopicsDataFetcher())
                                .dataFetcher(
                                        "currentUser",
                                        graphQLDataFetchers.getCurrentUserDataFetcher())
                                .dataFetcher(
                                        "popularTags",
                                        graphQLDataFetchers.getPopularTagsDataFetcher())
                                .dataFetcher(
                                        "memesByTag",
                                        graphQLDataFetchers.getMemesByTagDataFetcher())
                                .dataFetcher(
                                        "memesByAuthorId",
                                        graphQLDataFetchers.getMemesByAuthorIdDataFetcher()))
                .type(
                        TypeRuntimeWiring.newTypeWiring("Meme")
                                .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .type(
                        TypeRuntimeWiring.newTypeWiring("Mutation")
                                .dataFetcher(
                                        "createMeme", graphQLDataFetchers.createMemeDataFetcher()))
                .build();
    }
}
