package memetalk.config;

import memetalk.ConfigReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class applicationConfiguration {
  @Bean
  public ConfigReader getConfigReader() throws Exception {
    return ConfigReader.getInstance();
  }
}
