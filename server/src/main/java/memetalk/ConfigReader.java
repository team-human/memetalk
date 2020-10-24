package memetalk;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ConfigReader loads configurations based on different deployment environment and exposes interface
 * for components to read the configurations.
 */
public class ConfigReader {

  public enum DeploymentEnvironment {
    DEV,
    TEST,
    PROD
  }

  private static final Map<DeploymentEnvironment, String> config_filepath =
      new HashMap<DeploymentEnvironment, String>() {
        {
          put(DeploymentEnvironment.DEV, "configs/dev.properties");
          put(DeploymentEnvironment.TEST, "configs/test.properties");
          put(DeploymentEnvironment.PROD, "configs/prod.properties");
        }
      };

  private Properties properties;

  public ConfigReader(DeploymentEnvironment environment) throws Exception {
    final URL url = Resources.getResource(config_filepath.get(environment));
    final ByteSource byteSource = Resources.asByteSource(url);
    properties = new Properties();
    properties.load(byteSource.openBufferedStream());
  }

  /**
   * Reads a config value of a given key. Returns `null` if we can't find a corresponding config
   * value.
   */
  public String getConfig(String key) {
    return properties.getProperty(key);
  }
}
