package memetalk.controller;

import static org.junit.Assert.assertEquals;

import memetalk.ConfigReader;
import org.junit.Test;

public class StaticFileManagerTest {
  @Test
  public void testWriteSucceed() throws Exception {
    ConfigReader configReader = ConfigReader.getInstance();
    StaticFileManager staticFileManager = new StaticFileManager();
    String inputString = "Hello World!";
    String url = staticFileManager.write(configReader, "hello.txt", inputString.getBytes());
    assertEquals(configReader.getConfig("static-file-url-prefix") + "hello.txt", url);
  }
}
