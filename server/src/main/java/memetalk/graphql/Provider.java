package memetalk.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import memetalk.ConfigReader;
import memetalk.controller.StaticFileManager;
import memetalk.database.DatabaseAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/* Parsing GraphQL schema and creating runtime wiring to have executable schema */
@Component
public class Provider {
  private static final String GRAPHQL_SCHEMA_NAME = "schema.graphql";

  private static DataFetchers dataFetchers;

  private GraphQL graphQL;

  public Provider() throws Exception {
    dataFetchers =
        new DataFetchers(new DatabaseAdapter(ConfigReader.getInstance()), new StaticFileManager());
  }

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
    RunTimeWiringFactory factory = RunTimeWiringFactory.getInstance();

    factory.registerTypeWiring("Query", "currentUser", dataFetchers.getCurrentUserDataFetcher());
    factory.registerTypeWiring("Query", "popularTags", dataFetchers.getPopularTagsDataFetcher());
    factory.registerTypeWiring("Query", "memesByTag", dataFetchers.getMemesByTagDataFetcher());
    factory.registerTypeWiring(
        "Query", "memesByAuthorId", dataFetchers.getMemesByAuthorIdDataFetcher());
    factory.registerTypeWiring("Mutation", "createMeme", dataFetchers.createMemeDataFetcher());

    factory.registerScalar(FileScalarCoercing.FILE);

    return factory.build();
  }
}
