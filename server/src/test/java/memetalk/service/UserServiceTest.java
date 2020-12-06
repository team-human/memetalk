package memetalk.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import memetalk.controller.graphql.GraphQLAuthenticator;
import memetalk.database.DatabaseAdapter;
import memetalk.database.UserRepository;
import memetalk.model.CreateUserInput;
import memetalk.model.JwtUserDetails;
import memetalk.model.LoginUser;
import memetalk.model.User;
import memetalk.security.SecurityProperties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Slf4j
public class UserServiceTest {
  private static final String USER_AUTHORITY = "USER";
  private static final String ADMIN_AUTHORITY = "ADMIN";
  private UserRepository userRepository;
  private JWTVerifier jwtVerifier;
  private PasswordEncoder passwordEncoder;
  private SecurityProperties securityProperties;
  private Algorithm algorithm;
  private UserService userService;

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

    when(securityProperties.getSecretKey()).thenReturn(secretKey);
    when(securityProperties.getTokenIssuer()).thenReturn(issuer);
    when(securityProperties.getTokenExpiration()).thenReturn(Duration.ofMinutes(5));

    authentication = mock(Authentication.class);
    securityContext = mock(SecurityContext.class);
  }

  @Test
  public void testIsAdmin() {
    List<GrantedAuthority> authorities =
        ImmutableList.of(ADMIN_AUTHORITY).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    doReturn(authorities).when(authentication).getAuthorities();
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    assertTrue(userService.isAdmin());
  }

  @Test
  public void testIsAuthenticated() {
    PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken =
        new PreAuthenticatedAuthenticationToken(new Object(), new Object());
    preAuthenticatedAuthenticationToken.setAuthenticated(true);
    when(securityContext.getAuthentication()).thenReturn(preAuthenticatedAuthenticationToken);
    SecurityContextHolder.setContext(securityContext);

    assertTrue(userService.isAuthenticated());
  }

  @Test
  public void testIsAnonymous() {
    AnonymousAuthenticationToken anonymousAuthenticationToken =
        new AnonymousAuthenticationToken(
            "key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
    SecurityContextHolder.setContext(securityContext);

    assertTrue(userService.isAnonymous());
  }

  @Test
  public void testGetToken() {
    User user =
        User.builder()
            .id("id")
            .name("name")
            .password("password")
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();

    String actualToken = userService.getToken(user);
    DecodedJWT decodedJWT = jwtVerifier.verify(actualToken);

    assertEquals(user.getId(), decodedJWT.getSubject());
  }

  @Test
  public void testLoadUserByUsername() {
    User user =
        User.builder()
            .id("id")
            .name("name")
            .password("password")
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();

    when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));
    JwtUserDetails jwtUserDetails = userService.loadUserByUsername(user.getId());

    assertEquals(jwtUserDetails.getUsername(), user.getId());
  }

  @Test
  public void testLoadUserByToken() {
    User user =
        User.builder()
            .id("id")
            .name("name")
            .password("password")
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();

    String actualToken = userService.getToken(user);
    when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));

    JwtUserDetails jwtUserDetails = userService.loadUserByToken(actualToken);
    assertEquals(jwtUserDetails.getUsername(), user.getId());
  }

  @Test
  public void testGetCurrentUser() {
    User user =
        User.builder()
            .id("id")
            .name("name")
            .password("password")
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();
    when(userRepository.findUserById(user.getId())).thenReturn(Optional.of(user));

    when(authentication.getName()).thenReturn(user.getId());
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    LoginUser loginUser = userService.getCurrentUser();
    assertEquals(user, loginUser.getUser());
  }

  @Test
  public void testCreateUser() {
    CreateUserInput createUserInput = new CreateUserInput("id", "password", "name");
    User user =
        User.builder()
            .id(createUserInput.getId())
            .name(createUserInput.getName())
            .password(createUserInput.getPassword())
            .roles(ImmutableSet.of(USER_AUTHORITY))
            .build();
    when(userRepository.storeUser(any())).thenReturn(user);

    LoginUser loginUser = userService.createUser(createUserInput);
    assertEquals(user, loginUser.getUser());
    assertTrue(loginUser.getToken() != null);
  }
}