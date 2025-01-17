package memetalk.controller.graphql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.servlet.http.Part;
import memetalk.model.File;
import org.junit.Test;

public class FileScalarTest {
  @Test
  public void testParseValue() throws IOException {
    FileScalar fileScalar = new FileScalar();
    Part part = mock(Part.class);
    InputStream inputStream = new ByteArrayInputStream("fake data".getBytes());
    when(part.getInputStream()).thenReturn(inputStream);
    when(part.getContentType()).thenReturn("fakeType");

    File file = fileScalar.parseValue(part);
    assertTrue(Arrays.equals("fake data".getBytes(), file.getContent()));
    assertEquals("fakeType", file.getType());
  }
}
