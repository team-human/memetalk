package memetalk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigReaderTest {
  @Test
  public void testGetConfigSucceed() throws Exception {
    ConfigReader configReader = ConfigReader.getInstance();
    assertEquals(configReader.getConfig("foo"), "bar");
  }

  @Test
  public void testGetConfigFailedWithNonexistingKey() throws Exception {
    ConfigReader configReader = ConfigReader.getInstance();
    assertEquals(configReader.getConfig("bar"), null);
  }
}
