package memetalk;

import static memetalk.ConfigReader.DeploymentEnvironment;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigReaderTest {
    @Test
    public void testGetConfigSucceed() {
        ConfigReader configReader = new ConfigReader(DeploymentEnvironment.TEST);
        assertEquals(configReader.getConfig("foo"), "bar");
    }

    @Test
    public void testGetConfigFailedWithNonexistingKey() {
        ConfigReader configReader = new ConfigReader(DeploymentEnvironment.TEST);
        assertEquals(configReader.getConfig("bar"), null);
    }
}
