package memetalk.controller.graphql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.RuntimeWiring;
import org.junit.Test;

public class RunTimeWiringFactoryTest {

  @Test
  public void testRegisterTypeWiring() throws Exception {
    final String expectedResult = "Test";
    final String typeName = "Query";
    final String fieldName = "topics";
    RunTimeWiringFactory factory = RunTimeWiringFactory.getInstance();
    factory.registerTypeWiring(typeName, fieldName, dataFetchingEnvironment -> expectedResult);

    final RuntimeWiring runtimeWiring = factory.build();

    assertEquals(
        expectedResult,
        runtimeWiring
            .getDataFetchers()
            .get(typeName)
            .get(fieldName)
            .get(mock(DataFetchingEnvironment.class)));
  }

  @Test
  public void testRegisterScalar() throws Exception {
    RunTimeWiringFactory factory = RunTimeWiringFactory.getInstance();
    factory.registerScalar(FileScalar.FILE);
    final RuntimeWiring runtimeWiring = factory.build();
    assertTrue(runtimeWiring.getScalars().containsKey("File"));
  }
}
