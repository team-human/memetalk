package memetalk.controller.graphql;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.io.IOException;
import javax.servlet.http.Part;
import memetalk.model.File;

public class FileScalar implements Coercing<File, Void> {
  @Override
  public File parseValue(Object input) {
    try {
      Part part = (Part) input;
      String contentType = part.getContentType();
      byte[] content = part.getInputStream().readAllBytes();
      part.delete();
      return File.builder().type(contentType).content(content).build();
    } catch (IOException e) {
      throw new CoercingParseValueException("Caught IO exception: " + e.getMessage());
    }
  }

  @Override
  public File parseLiteral(Object input) throws CoercingParseLiteralException {
    throw new CoercingParseLiteralException("Unimplemented.");
  }

  @Override
  public Void serialize(Object input) throws CoercingSerializeException {
    throw new CoercingSerializeException("Unimplemented.");
  }

  public static final GraphQLScalarType FILE =
      GraphQLScalarType.newScalar()
          .name("File")
          .description("Generic binary filetype.")
          .coercing(new FileScalar())
          .build();
}
