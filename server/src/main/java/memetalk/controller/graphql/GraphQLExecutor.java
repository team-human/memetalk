package memetalk.controller.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.net.URL;
import java.util.Map;
import memetalk.ConfigReader;
import memetalk.controller.StaticFileManager;
import memetalk.database.DatabaseAdapter;
import org.springframework.stereotype.Component;

/** GraphQLExecutor owns a GraphQL use it to execute the incoming queries. */
@Component
public class GraphQLExecutor {
  private static final String GRAPHQL_SCHEMA_NAME = "schema.graphql";
  private static DataFetchers dataFetchers;

  private GraphQL graphQL;

  public GraphQLExecutor() throws Exception {
    dataFetchers =
        new DataFetchers(new DatabaseAdapter(ConfigReader.getInstance()), new StaticFileManager());
    URL url = Resources.getResource(GRAPHQL_SCHEMA_NAME);
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema graphQLSchema = buildSchema(sdl);
    this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  public Map<String, Object> executeRequest(
      String query, Map<String, Object> variables, String operationName) {
    ExecutionInput input =
        ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .operationName(operationName)
            .build();
    ExecutionResult executionResult = this.graphQL.execute(input);
    return executionResult.toSpecification();
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

    factory.registerScalar(FileScalar.FILE);

    return factory.build();
  }
}
