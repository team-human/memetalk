package memetalk.service;

import static java.util.function.Predicate.not;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.UserRepository;
import memetalk.exception.BadTokenException;
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
  private final UserRepository userRepository;
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
  public JwtUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return userRepository
        .findUserById(id)
        .map(user -> getUserDetails(user, getToken(user)))
        .orElseThrow(() -> new UsernameNotFoundException("Username or password didn''t match"));
  }

  @Transactional
  public JwtUserDetails loadUserByToken(String token) {
    return getDecodedToken(token)
        .map(DecodedJWT::getSubject)
        .flatMap(userRepository::findUserById)
        .map(user -> getUserDetails(user, token))
        .orElseThrow(BadTokenException::new);
  }

  @Transactional
  public LoginUser getCurrentUser() {
    final User user =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getName)
            .flatMap(userRepository::findUserById)
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
    if (!exists(input)) {

      final User user =
          userRepository.storeUser(
              User.builder()
                  .password(passwordEncoder.encode(input.getPassword()))
                  .roles(ImmutableSet.of(USER_AUTHORITY))
                  .name(input.getName())
                  .id(input.getId())
                  .build());

      if (user == null) {
        throw new UserNotFoundException("Can't Create User in DB");
      } else {
        return LoginUser.builder().token(getToken(user)).user(user).build();
      }

    } else {
      throw new UserExistsException(
          "Creating a new User encounters error. Id `{}` exists" + input.getId());
    }
  }

  private boolean exists(CreateUserInput input) {
    return userRepository.existsById(input.getId());
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
