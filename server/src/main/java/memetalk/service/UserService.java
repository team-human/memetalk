package memetalk.service;

import static java.util.function.Predicate.not;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.DatabaseAdapter;
import memetalk.exception.BadTokenException;
import memetalk.exception.SQLExecutionException;
import memetalk.exception.UserExistsException;
import memetalk.exception.UserNotFoundException;
import memetalk.model.CreateUserInput;
import memetalk.model.JwtUserDetails;
import memetalk.model.LoginUser;
import memetalk.model.User;
import memetalk.security.SecurityProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/*
 * This class is for User related data (User table) and AoP for User authentication.
 * Note: The reason why it is implements UserDetailsService is because we need it for AuthenticationProvider,
 * which will be used for Spring boot security authentication parts.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private static final String USER_AUTHORITY = "USER";
  private static final String ADMIN_AUTHORITY = "ADMIN";
  private final DatabaseAdapter databaseAdapter;
  private final JWTVerifier jwtVerifier;
  private final PasswordEncoder passwordEncoder;
  private final SecurityProperties securityProperties;
  private final Algorithm algorithm;

  public boolean isAdmin() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getAuthorities)
        .stream()
        .flatMap(Collection::stream)
        .map(GrantedAuthority::getAuthority)
        .anyMatch(ADMIN_AUTHORITY::equals);
  }

  // Used in security annotation @PreAuthorize("@userService.isAuthenticated()")
  public static boolean isAuthenticated() {
    final boolean isAuthenticated =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .filter(not(UserService::isAnonymous))
            .isPresent();

    // Add debug here. After we are comfortable with it, we can remove this debug log.
    log.info(
        "isAuthenticated() is called, and it is {}",
        isAuthenticated ? "authenticated" : "not authenticated");
    return isAuthenticated;
  }

  private static boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }

  // Used in security annotation @PreAuthorize("@userService.isAnonymous()")
  public static boolean isAnonymous() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .filter(UserService::isAnonymous)
        .isPresent();
  }

  protected String getToken(User user) {
    Instant now = Instant.now();
    Instant expiry = Instant.now().plus(securityProperties.getTokenExpiration());
    return JWT.create()
        .withIssuer(securityProperties.getTokenIssuer())
        .withIssuedAt(Date.from(now))
        .withExpiresAt(Date.from(expiry))
        .withSubject(user.getId())
        .sign(algorithm);
  }

  @Override
  @Transactional
  public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return findUserByUsername(username)
        .map(user -> getUserDetails(user, getToken(user)))
        .orElseThrow(() -> new UsernameNotFoundException("Username or password didn''t match"));
  }

  private Optional<User> findUserByUsername(@NonNull final String username) {
    try {
      return databaseAdapter.findUserByUsername(username);
    } catch (SQLException ex) {
      log.error("Find user by username encounter errors", ex);
      return Optional.empty();
    }
  }

  @Transactional
  public JwtUserDetails loadUserByToken(String token) {
    return getDecodedToken(token)
        .map(DecodedJWT::getSubject)
        .flatMap(this::findUserByUsername)
        .map(user -> getUserDetails(user, token))
        .orElseThrow(() -> new BadTokenException("Bad Jwt token"));
  }

  @Transactional
  public LoginUser getCurrentUser() {
    final User user =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .flatMap(this::findUserByUsername)
            .orElse(null);

    if (user == null) {
      log.error("can't find user");
      throw new UserNotFoundException("Can't find User in DB");
    } else {
      return LoginUser.builder().token(getToken(user)).user(user).build();
    }
  }

  @Transactional
  public LoginUser createUser(CreateUserInput input) {
    if (!checkUserNameExist(input)) {

      final User user =
          User.builder()
              .password(passwordEncoder.encode(input.getPassword()))
              .roles(ImmutableSet.of(USER_AUTHORITY))
              .username(input.getUsername())
              .name(input.getName())
              .build();

      try {
        databaseAdapter.createUser(user);
      } catch (SQLException ex) {
        throw new SQLExecutionException(ex);
      }

      return LoginUser.builder().token(getToken(user)).user(user).build();

    } else {
      throw new UserExistsException(
          String.format("UserExistsException. Username `%s` exists", input.getUsername()));
    }
  }

  private boolean checkUserNameExist(CreateUserInput input) {
    try {
      return databaseAdapter.checkUserNameExist(input.getUsername());
    } catch (SQLException ex) {
      throw new SQLExecutionException(ex);
    }
  }

  private JwtUserDetails getUserDetails(User user, String token) {
    return JwtUserDetails.builder()
        .username(user.getId())
        .password(user.getPassword())
        .authorities(getSimpleGrantedAuthorities(user.getRoles()))
        .token(token)
        .build();
  }

  private Optional<DecodedJWT> getDecodedToken(String token) {
    try {
      return Optional.of(jwtVerifier.verify(token));
    } catch (JWTVerificationException ex) {
      log.warn("Jwt verification failed", ex);
      return Optional.empty();
    }
  }

  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Set<String> roles) {
    if (CollectionUtils.isEmpty(roles)) {
      return ImmutableList.of();
    } else {
      return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
  }
}
