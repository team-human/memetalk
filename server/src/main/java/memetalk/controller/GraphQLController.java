package memetalk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import memetalk.controller.graphql.GraphQLExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * GraphQLController passes user's request to GraphQLExecutor to execute, then generates the
 * response.
 */
@Slf4j
@Controller
class GraphQLController {
  private GraphQLExecutor graphQLExecutor;
  private ObjectMapper objectMapper;

  public GraphQLController() throws Exception {
    this.graphQLExecutor = new GraphQLExecutor();
    this.objectMapper = new ObjectMapper();
  }

  // This constructor is for test only.
  protected GraphQLController(GraphQLExecutor graphQLExecutor) {
    this.graphQLExecutor = graphQLExecutor;
    this.objectMapper = new ObjectMapper();
  }

  @PostMapping(value = "/graphql")
  public Object handleGraphQLRequest(
      @Nullable @RequestBody(required = false) String body,
      @RequestParam(value = "operations", required = false) String operations,
      @RequestParam(value = "file", required = false) Part file) {
    try {
      // Files are uploaded through request params instead of the request body.
      if (operations != null && file != null) {
        return handleGraphQLRequestWithFile(operations, file);
      }
      if (body != null) {
        return handleGraphQLRequestWithoutFile(/*operations=*/ body);
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unable to find valid request content.");
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Unable to parse the request and/or executed response.");
    }
  }

  private ResponseEntity<String> handleGraphQLRequestWithFile(String operations, Part file)
      throws JsonProcessingException {
    Map<String, Object> payload = objectMapper.readValue(operations, Map.class);
    Map<String, Object> variables =
        (payload.get("variables") != null)
            ? (Map<String, Object>) payload.get("variables")
            : new HashMap<>();

    // We loads file variable manually as the tools don't parse it by default.
    variables.put("file", file);

    Map<String, Object> result =
        this.graphQLExecutor.executeRequest(
            (String) payload.get("query"), variables, (String) payload.get("operationName"));
    return buildResponse(result);
  }

  private ResponseEntity<String> handleGraphQLRequestWithoutFile(String operations)
      throws JsonProcessingException {
    Map<String, Object> payload = objectMapper.readValue(operations, Map.class);
    Map<String, Object> result =
        this.graphQLExecutor.executeRequest(
            (String) payload.get("query"),
            (Map<String, Object>) payload.get("variables"),
            (String) payload.get("operationName"));
    return buildResponse(result);
  }

  private ResponseEntity<String> buildResponse(Map<String, Object> executionResult)
      throws JsonProcessingException {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<String>(
        objectMapper.writeValueAsString(executionResult), responseHeaders, HttpStatus.OK);
  }
}
