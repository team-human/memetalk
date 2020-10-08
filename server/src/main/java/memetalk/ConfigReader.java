package memetalk;

import com.google.common.io.Resources;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * ConfigReader loads configurations based on different deployment environment
 * and exposes interface for components to read the configurations.
 */
public class ConfigReader {

  public enum DeploymentEnvironment {
    DEV, TEST, PROD
  }

  public ConfigReader(DeploymentEnvironment environment) {
    final URL url = Resources.getResource(getConfigFilePath(environment));
    final ByteSource byteSource = Resources.asByteSource(url);
    properties = new Properties();
    try {
      properties.load(byteSource.openBufferedStream());
    } catch (IOException io) {
      io.printStackTrace();
    }
  }

  /**
   * Reads a config value of a given key. Returns `null` if we can't find a
   * corresponding config value.
   */
  public String getConfig(String key) {
    return properties.getProperty(key);
  }

  private String getConfigFilePath(DeploymentEnvironment environment) {
    if (environment == DeploymentEnvironment.TEST) {
      return TEST_CONFIG_FILEPATH;
    }
    if (environment == DeploymentEnvironment.PROD) {
      return PROD_CONFIG_FILEPATH;
    }
    return DEV_CONFIG_FILEPATH;
  }

  private final static String DEV_CONFIG_FILEPATH = "configs/dev.properties";
  private final static String TEST_CONFIG_FILEPATH = "configs/test.properties";
  private final static String PROD_CONFIG_FILEPATH = "configs/prod.properties";

  private Properties properties;
}
