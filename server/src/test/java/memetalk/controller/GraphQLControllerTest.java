package memetalk.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import memetalk.controller.graphql.Executor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class GraphQLControllerTest {
  private Executor executor;

  @Before
  public void setUp() {
    this.executor = mock(Executor.class);
  }

  @Test
  public void testHandleGraphQLRequestWithEmptyBody() {
    GraphQLController graphQLController = new GraphQLController(executor);
    Object response = graphQLController.handleGraphQLRequest(/*body=*/ null);
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is empty.");
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithInvalidQuery() {
    GraphQLController graphQLController = new GraphQLController(executor);
    Object response = graphQLController.handleGraphQLRequest(/*body=*/ "bad content");
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Unable to parse the request and/or executed response.");
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithValidQuery() {
    GraphQLController graphQLController = new GraphQLController(executor);
    String query = "query { popularTags }";
    String body = "{\"query\":\"" + query + "\"}";

    Map<String, Object> executionResult = new HashMap<>();
    executionResult.put("bar", "foo");
    when(executor.executeRequest(eq(query), isNull(), isNull())).thenReturn(executionResult);

    Object response = graphQLController.handleGraphQLRequest(body);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    assertEquals(
        response, new ResponseEntity<String>("{\"bar\":\"foo\"}", responseHeaders, HttpStatus.OK));
  }
}
