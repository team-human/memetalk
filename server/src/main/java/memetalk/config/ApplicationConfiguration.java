package memetalk.config;

import memetalk.ConfigReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
  @Bean
  public ConfigReader getConfigReader() throws Exception {
    return ConfigReader.getInstance();
  }
}
