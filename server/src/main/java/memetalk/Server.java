package memetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * We exclude DataSourceAutoConfiguration as part of the spring application because we have our own ConfigReader.
 * (Maybe consider deprecate ConfigReader, and use Spring boot configuration only)
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Server {
  public static void main(String[] args) {
    SpringApplication.run(Server.class, args);
  }

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean<CorsFilter> bean =
        new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

  @Configuration
  public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      try {
        String dirPath = ConfigReader.getInstance().getConfig("static-file-dir");
        registry.addResourceHandler("/**").addResourceLocations("file:" + dirPath);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
