package memetalk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import memetalk.controller.graphql.Executor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * GraphQLController passes user's request to Executor to execute, then generates the
 * response.
 */
@Controller
class GraphQLController {
  private Executor executor;
  private ObjectMapper objectMapper;

  public GraphQLController() throws Exception {
    this.executor = new Executor();
    this.objectMapper = new ObjectMapper();
  }

  // This constructor is for test only.
  protected GraphQLController(Executor executor) {
    this.executor = executor;
    this.objectMapper = new ObjectMapper();
  }

  @PostMapping(value = "/graphql")
  public Object handleGraphQLRequest(@Nullable @RequestBody(required = false) String body) {
    if (body == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is empty.");
    }

    try {
      Map<String, Object> payload = objectMapper.readValue(body, Map.class);
      Map<String, Object> result =
          this.executor.executeRequest(
              (String) payload.get("query"),
              (Map<String, Object>) payload.get("variables"),
              (String) payload.get("operationName"));
      return buildResponse(result);
    } catch (JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unable to parse the request and/or executed response.");
    }
  }

  private ResponseEntity<String> buildResponse(Map<String, Object> executionResult)
      throws JsonProcessingException {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<String>(
        objectMapper.writeValueAsString(executionResult), responseHeaders, HttpStatus.OK);
  }
}
