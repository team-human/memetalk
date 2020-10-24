package memetalk;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.net.URL;
import memetalk.graphql.FileScalarCoercing;
import org.junit.Test;

public class SchemaTest {
  private static final String GRAPHQL_SCHEMA_NAME = "schema.graphql";

  @Test
  public void validateSchema() throws Exception {
    URL url = Resources.getResource(GRAPHQL_SCHEMA_NAME);
    String sdl = Resources.toString(url, Charsets.UTF_8);
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    schemaGenerator.makeExecutableSchema(
        typeRegistry, RuntimeWiring.newRuntimeWiring().scalar(FileScalarCoercing.FILE).build());
  }
}
