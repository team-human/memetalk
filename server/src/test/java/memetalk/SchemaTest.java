package memetalk;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.net.URL;
import org.junit.Test;

public class SchemaTest {

    @Test
    public void validateSchema() throws Exception {
        URL url = Resources.getResource("schema.graphql");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        schemaGenerator.makeExecutableSchema(
                typeRegistry, RuntimeWiring.newRuntimeWiring().build());
    }
}
