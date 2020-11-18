package memetalk.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Part;
import memetalk.controller.graphql.GraphQLExecutor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class GraphQLControllerTest {
  private GraphQLExecutor graphQLExecutor;

  @Before
  public void setUp() {
    this.graphQLExecutor = mock(GraphQLExecutor.class);
  }

  @Test
  public void testHandleGraphQLRequestWithEmptyBody() {
    GraphQLController graphQLController =
        new GraphQLController(graphQLExecutor);
    Object response = graphQLController.handleGraphQLRequest(
        /*body=*/null, /*operations=*/null, /*file=*/null);
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Unable to find valid request content.");
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithInvalidQuery() {
    GraphQLController graphQLController =
        new GraphQLController(graphQLExecutor);
    Object response = graphQLController.handleGraphQLRequest(
        /*body=*/"bad content", /*operations=*/null, /*file=*/null);
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Unable to parse the request and/or executed response.");
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithValidQuery() {
    GraphQLController graphQLController =
        new GraphQLController(graphQLExecutor);
    String query = "query { popularTags }";
    String body = "{\"query\":\"" + query + "\"}";

    Map<String, Object> executionResult = new HashMap<>();
    executionResult.put("bar", "foo");
    when(graphQLExecutor.executeRequest(eq(query), isNull(), isNull()))
        .thenReturn(executionResult);

    Object response = graphQLController.handleGraphQLRequest(
        body, /*operations=*/null, /*file=*/null);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    assertEquals(response,
                 new ResponseEntity<String>("{\"bar\":\"foo\"}",
                                            responseHeaders, HttpStatus.OK));
  }

  @Test
  public void testHandleGraphQLRequestWithFile() throws Exception {
    // File is an variable (with 'Part' object type) that is extracted from the
    // request param separately. That is, the variables we pass into
    // GraphQLExecutor is composed by the variables in the `operations` string
    // and the `file` object from the request param.
    GraphQLController graphQLController =
        new GraphQLController(graphQLExecutor);

    String query =
        "mutation ($file: File!, $tags: [String!]) { createMeme(file: $file, tags: $tags) { tags } }";
    String operations =
        "{ \"query\": \"" + query +
        "\", \"variables\": { \"file\": null, \"tags\": [\"software\", \"humor\"] } }";

    InputStream inputStream = mock(InputStream.class);
    when(inputStream.readAllBytes()).thenReturn(new byte[0]);

    Part file = mock(Part.class);
    when(file.getContentType()).thenReturn("");
    when(file.getInputStream()).thenReturn(inputStream);

    Map<String, Object> expectedVariables = new HashMap<>();
    expectedVariables.put("tags", asList("software", "humor"));
    expectedVariables.put("file", file);

    Map<String, Object> executionResult = new HashMap<>();
    executionResult.put("bar", "foo");
    when(graphQLExecutor.executeRequest(eq(query), eq(expectedVariables),
                                        isNull()))
        .thenReturn(executionResult);

    Object response = graphQLController.handleGraphQLRequest(
        /*body=*/null, /*operations=*/operations, /*file=*/file);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    assertEquals(response,
                 new ResponseEntity<String>("{\"bar\":\"foo\"}",
                                            responseHeaders, HttpStatus.OK));
  }
}
