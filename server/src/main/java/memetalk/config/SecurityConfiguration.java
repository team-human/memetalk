package memetalk.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import memetalk.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// In order to let Spring Boot injection to find out SecurityProperties
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {
  @Bean
  public Algorithm jwtAlgorithm(SecurityProperties properties) {
    return Algorithm.HMAC256(properties.getSecretKey());
  }

  @Bean
  public JWTVerifier jwtVerifierProvider(SecurityProperties properties, Algorithm algorithm) {
    return JWT.require(algorithm).withIssuer(properties.getTokenIssuer()).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder(SecurityProperties properties) {
    return new BCryptPasswordEncoder(properties.getPasswordStrength());
  }
}
