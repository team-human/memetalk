package memetalk.security;

import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * SecurityProperties is for JWT related setting. We use spring boot ConfigurationProperties
 * annotation to create this object with Environment variables. For example, to give the
 * passwordStrength and tokenSecret, by doing the following before launching the spring boot
 * MEMETALK_SECURITY_JWT_PASSWORDSTRENGTH=10 MEMETALK_SECURITY_JWT_SECRETKEY=myspecialsecret
 * ./gradlew bootRun
 *
 * <p>TODO: A better approach is to allow the key rotation for secretKey. To do that, we can make
 * secretKey as a Map<String,String>, and add a currentKeyID to indicate which secret key we want to
 * use. For Jwt verification, we put the keyID in the JWT token, we use that keyID to find out which
 * secret key we need to use to validate the token, and later create a new token based on the
 * currentKeyID. So, we can rotate the key without use to login again.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "memetalk.security.jwt")
@Getter
@RequiredArgsConstructor
public class SecurityProperties {
  /** Amound of hashing iterations, where formula is 2^passwordStrength iterations */
  private final int passwordStrength;
  /** Secret used to generate and verify JWT tokens */
  private final String secretKey;
  /** Name of the token issuer */
  private final String tokenIssuer = "memetalk";
  /** Duration after which a token will expire */
  private final Duration tokenExpiration = Duration.ofHours(4);
}
