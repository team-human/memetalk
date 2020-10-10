package memetalk;

import static memetalk.ConfigReader.DeploymentEnvironment;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigReaderTest {
    @Test
    public void testGetConfigSucceed() throws Exception {
        ConfigReader configReader = new ConfigReader(DeploymentEnvironment.TEST);
        assertEquals(configReader.getConfig("foo"), "bar");
    }

    @Test
    public void testGetConfigFailedWithNonexistingKey() throws Exception {
        ConfigReader configReader = new ConfigReader(DeploymentEnvironment.TEST);
        assertEquals(configReader.getConfig("bar"), null);
    }
}
