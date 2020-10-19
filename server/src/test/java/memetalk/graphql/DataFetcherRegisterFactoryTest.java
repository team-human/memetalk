package memetalk.graphql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.RuntimeWiring;
import org.junit.Test;

public class DataFetcherRegisterFactoryTest {

    @Test
    public void testDataFetcherRegisterFactory() throws Exception {
        final String expectedResult = "Test";
        final String typeName = "Query";
        final String fieldName = "topics";
        DataFetcherRegisterFactory factory = DataFetcherRegisterFactory.getRuntimeWiring();
        factory.registerTypeWiring(typeName, fieldName, dataFetchingEnvironment -> expectedResult);

        final RuntimeWiring runtimeWiring = factory.buildTypeWiring();

        assertEquals(
                expectedResult,
                runtimeWiring
                        .getDataFetchers()
                        .get(typeName)
                        .get(fieldName)
                        .get(mock(DataFetchingEnvironment.class)));
    }
}
