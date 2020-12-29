package memetalk.controller.graphql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.DatabaseAdapter;
import memetalk.database.UserRepository;
import memetalk.model.CreateUserInput;
import memetalk.model.LoginUser;
import memetalk.model.User;
import memetalk.security.SecurityProperties;
import memetalk.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class GraphQLAuthenticatorTest {
  private static final String USER_AUTHORITY = "USER";
  private UserRepository userRepository;
  private JWTVerifier jwtVerifier;
  private PasswordEncoder passwordEncoder;
  private SecurityProperties securityProperties;
  private Algorithm algorithm;
  private UserService userService;
  private AuthenticationProvider authenticationProvider;
  private DatabaseAdapter databaseAdapter;
  private GraphQLAuthenticator graphQLAuthenticator;
  private Authentication authentication;
  private SecurityContext securityContext;

  @Before
  public void setUp() {
    final String secretKey = "test_123";
    final String issuer = "test issuer";
    userRepository = mock(UserRepository.class);
    passwordEncoder = new BCryptPasswordEncoder(10);
    securityProperties = mock(SecurityProperties.class);
    algorithm = Algorithm.HMAC256(secretKey);
    jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    userService =
        new UserService(
            userRepository, jwtVerifier, passwordEncoder, securityProperties, algorithm);
    authenticationProvider = mock(AuthenticationProvider.class);
    databaseAdapter = mock(DatabaseAdapter.class);
    graphQLAuthenticator =
        new GraphQLAuthenticator(userService, authenticationProvider, databaseAdapter);

    when(securityProperties.getSecretKey()).thenReturn(secretKey);
    when(securityProperties.getTokenIssuer()).thenReturn(issuer);
    when(securityProperties.getTokenExpiration()).thenReturn(Duration.ofMinutes(5));

    authentication = mock(Authentication.class);
    securityContext = mock(SecurityContext.class);
  }

  @Test
  public void testLoginUserAuth() {
    final String id = "test_id";
    final String password = "pass_word";
    final String name = "sam";
    final User expectedUser =
        User.builder()
            .id(id)
            .userName("userName")
            .password(passwordEncoder.encode(password))
            .name(name)
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();

    when(authentication.getName()).thenReturn(id);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findUserByUserName(id)).thenReturn(Optional.of(expectedUser));

    LoginUser loginUser = graphQLAuthenticator.loginUserAuth(id, password);
    assertEquals(expectedUser, loginUser.getUser());
    assertNotNull(loginUser.getToken());
  }

  @Test
  public void testCreateUserAuth() {
    CreateUserInput createUserInput = new CreateUserInput("user_name", "password", "sam");
    User expectedUser =
        User.builder()
            .userName(createUserInput.getUserName())
            .password(createUserInput.getPassword())
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .name(createUserInput.getName())
            .id(createUserInput.getUserName())
            .build();

    LoginUser loginUser = graphQLAuthenticator.createUserAuth(createUserInput);

    checkUserIsEqual(expectedUser, loginUser.getUser());
    assertNotNull(loginUser.getToken());
  }

  private void checkUserIsEqual(@NonNull User expectedUser, @NonNull User actualUser) {
    assertEquals(expectedUser.getId(), actualUser.getId());
    assertEquals(expectedUser.getName(), actualUser.getName());
    assertTrue(passwordEncoder.matches(expectedUser.getPassword(), actualUser.getPassword()));
    assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    assertEquals(expectedUser.getRoles(), actualUser.getRoles());
  }
}
