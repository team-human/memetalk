package memetalk.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
<<<<<<< HEAD
import memetalk.controller.graphql.Executor;
=======
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class GraphQLControllerTest {
<<<<<<< HEAD
  private Executor executor;

  @Before
  public void setUp() {
    this.executor = mock(Executor.class);
=======
  private GraphQLExecutor graphQLExecutor;

  @Before
  public void setUp() {
    this.graphQLExecutor = mock(GraphQLExecutor.class);
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
  }

  @Test
  public void testHandleGraphQLRequestWithEmptyBody() {
<<<<<<< HEAD
    GraphQLController graphQLController = new GraphQLController(executor);
    Object response = graphQLController.handleGraphQLRequest(/*body=*/null);
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Request body is empty.");
=======
    GraphQLController graphQLController = new GraphQLController(graphQLExecutor);
    Object response = graphQLController.handleGraphQLRequest(/*body=*/ null);
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is empty.");
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithInvalidQuery() {
<<<<<<< HEAD
    GraphQLController graphQLController = new GraphQLController(executor);
    Object response =
        graphQLController.handleGraphQLRequest(/*body=*/"bad content");
=======
    GraphQLController graphQLController = new GraphQLController(graphQLExecutor);
    Object response = graphQLController.handleGraphQLRequest(/*body=*/ "bad content");
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
    ResponseEntity<String> expectedResponse =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Unable to parse the request and/or executed response.");
    assertEquals(response, expectedResponse);
  }

  @Test
  public void testHandleGraphQLRequestWithValidQuery() {
<<<<<<< HEAD
    GraphQLController graphQLController = new GraphQLController(executor);
=======
    GraphQLController graphQLController = new GraphQLController(graphQLExecutor);
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
    String query = "query { popularTags }";
    String body = "{\"query\":\"" + query + "\"}";

    Map<String, Object> executionResult = new HashMap<>();
    executionResult.put("bar", "foo");
<<<<<<< HEAD
    when(executor.executeRequest(eq(query), isNull(), isNull()))
        .thenReturn(executionResult);
=======
    when(graphQLExecutor.executeRequest(eq(query), isNull(), isNull())).thenReturn(executionResult);
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e

    Object response = graphQLController.handleGraphQLRequest(body);

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
<<<<<<< HEAD
    assertEquals(response,
                 new ResponseEntity<String>("{\"bar\":\"foo\"}",
                                            responseHeaders, HttpStatus.OK));
=======
    assertEquals(
        response, new ResponseEntity<String>("{\"bar\":\"foo\"}", responseHeaders, HttpStatus.OK));
>>>>>>> b7f269932ed02dd594ab7da8c874fefe84ee6b2e
  }
}
