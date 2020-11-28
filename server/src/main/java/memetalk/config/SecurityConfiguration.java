package memetalk.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import memetalk.security.SecurityProperties;
import memetalk.service.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
// EnableConfigurationProperties instruct Spring boot where to find out SecurityProperties.class is.
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

  @Bean
  public AuthenticationProvider authenticationProvider(
      UserService userService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }
}
